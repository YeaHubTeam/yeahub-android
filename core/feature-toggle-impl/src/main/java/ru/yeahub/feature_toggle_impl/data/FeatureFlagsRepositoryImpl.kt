package ru.yeahub.feature_toggle_impl.data

import ru.yeahub.feature_toggle_api.FeatureAvailability
import ru.yeahub.feature_toggle_api.FeatureFlagsSnapshot
import ru.yeahub.feature_toggle_api.FeatureKey
import ru.yeahub.feature_toggle_api.FeatureKeys
import ru.yeahub.feature_toggle_impl.data.remote.FeatureFlagsRemoteDataSource
import ru.yeahub.network_api.models.GetFeatureFlagResponse
import timber.log.Timber

internal class FeatureFlagsRepositoryImpl(
    private val remoteDataSource: FeatureFlagsRemoteDataSource
) : FeatureFlagsRepository {

    private val featureKeysByValue = FeatureKeys.all.associateBy(FeatureKey::value)

    override suspend fun getFeatureFlagsSnapshot(): FeatureFlagsSnapshot {
        val featureAvailabilityByKey = loadAndroidFeatureFlags()
            .mapNotNull(::mapFeatureAvailabilityByKey)
            .toMap()

        logMissingBackendFeatureFlags(featureAvailabilityByKey = featureAvailabilityByKey)

        return FeatureFlagsSnapshot(
            featureAvailabilityByKey = featureAvailabilityByKey
        )
    }

    private suspend fun loadAndroidFeatureFlags(): List<GetFeatureFlagResponse> {
        val featureFlags = mutableListOf<GetFeatureFlagResponse>()
        var currentPage = INITIAL_PAGE

        while (true) {
            val response = remoteDataSource.getFeatureFlags(
                page = currentPage,
                limit = PAGE_LIMIT
            )

            featureFlags.addAll(response.data)

            if (featureFlags.size >= response.total || response.data.isEmpty()) {
                break
            }

            currentPage += 1
        }

        return featureFlags
    }

    //Логирование зарегистрированных, но не пришедших с бэкенда фичей
    private fun logMissingBackendFeatureFlags(
        featureAvailabilityByKey: Map<FeatureKey, FeatureAvailability>
    ) {
        val missingBackendFeatureFlags = FeatureKeys.all - featureAvailabilityByKey.keys

        if (missingBackendFeatureFlags.isEmpty()) {
            return
        }

        val missingBackendValues = missingBackendFeatureFlags.joinToString { featureKey ->
            featureKey.value
        }

        Timber.tag(FEATURE_TOGGLE_LOG_TAG)
            .w(MISSING_BACKEND_FEATURE_FLAGS_LOG_MESSAGE, missingBackendValues)
    }

    private fun mapFeatureAvailabilityByKey(
        remoteFeatureFlag: GetFeatureFlagResponse
    ): Pair<FeatureKey, FeatureAvailability>? {
        val featureKey = featureKeysByValue[remoteFeatureFlag.flag]

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
private const val INITIAL_PAGE = 1
private const val PAGE_LIMIT = 100
