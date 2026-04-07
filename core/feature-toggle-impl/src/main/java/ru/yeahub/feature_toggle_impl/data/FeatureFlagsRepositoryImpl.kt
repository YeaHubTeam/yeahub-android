package ru.yeahub.feature_toggle_impl.data

import ru.yeahub.feature_toggle_api.FeatureAvailability
import ru.yeahub.feature_toggle_api.FeatureFlagsSnapshot
import ru.yeahub.feature_toggle_api.FeatureKey
import ru.yeahub.feature_toggle_api.FeatureKeys
import ru.yeahub.feature_toggle_impl.resolver.FeatureKeyBackendMapper
import ru.yeahub.network_api.models.GetFeatureFlagResponse
import timber.log.Timber

internal class FeatureFlagsRepositoryImpl(
    private val remoteDataSource: FeatureFlagsRemoteDataSource,
    private val featureKeyBackendMapper: FeatureKeyBackendMapper
) : FeatureFlagsRepository {

    override suspend fun getFeatureFlagsSnapshot(): FeatureFlagsSnapshot {
        val featureAvailabilityByKey = remoteDataSource.loadAndroidFeatureFlags()
            .mapNotNull(::mapFeatureAvailabilityByKey)
            .toMap()

        logMissingBackendFeatureFlags(featureAvailabilityByKey = featureAvailabilityByKey)

        return FeatureFlagsSnapshot(
            featureAvailabilityByKey = featureAvailabilityByKey
        )
    }

    private fun logMissingBackendFeatureFlags(
        featureAvailabilityByKey: Map<FeatureKey, FeatureAvailability>
    ) {
        val missingBackendFeatureFlags = FeatureKeys.all - featureAvailabilityByKey.keys

        if (missingBackendFeatureFlags.isEmpty()) {
            return
        }

        val missingBackendValues = missingBackendFeatureFlags.joinToString { featureKey ->
            featureKeyBackendMapper.getBackendValue(featureKey = featureKey)
        }

        Timber.tag(FEATURE_TOGGLE_LOG_TAG)
            .w(MISSING_BACKEND_FEATURE_FLAGS_LOG_MESSAGE, missingBackendValues)
    }

    private fun mapFeatureAvailabilityByKey(
        remoteFeatureFlag: GetFeatureFlagResponse
    ): Pair<FeatureKey, FeatureAvailability>? {
        val featureKey = featureKeyBackendMapper.fromBackendValue(
            backendValue = remoteFeatureFlag.flag
        )

        if (featureKey == null) {
            Timber.tag(FEATURE_TOGGLE_LOG_TAG)
                .w(UNKNOWN_BACKEND_FEATURE_FLAG_LOG_MESSAGE, remoteFeatureFlag.flag)
            return null
        }

        return featureKey to FeatureAvailability.fromBoolean(
            isAvailable = remoteFeatureFlag.enabled
        )
    }
}

private const val FEATURE_TOGGLE_LOG_TAG = "FeatureToggle"
private const val UNKNOWN_BACKEND_FEATURE_FLAG_LOG_MESSAGE = "Unknown backend feature flag: %s"
private const val MISSING_BACKEND_FEATURE_FLAGS_LOG_MESSAGE =
    "Feature flags are missing in backend response: %s"
