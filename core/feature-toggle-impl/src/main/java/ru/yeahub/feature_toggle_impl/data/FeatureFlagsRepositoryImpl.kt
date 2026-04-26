package ru.yeahub.feature_toggle_impl.data

import ru.yeahub.feature_toggle_api.FeatureFlagsSnapshot
import ru.yeahub.feature_toggle_impl.data.remote.FeatureFlagsRemoteDataSource
import ru.yeahub.feature_toggle_impl.registry.FeatureToggleRegistry
import ru.yeahub.network_api.models.GetFeatureFlagResponse
import timber.log.Timber

internal class FeatureFlagsRepositoryImpl(
    private val remoteDataSource: FeatureFlagsRemoteDataSource,
    private val featureToggleRegistry: FeatureToggleRegistry
) : FeatureFlagsRepository {

    override suspend fun getFeatureFlagsSnapshot(): FeatureFlagsSnapshot {
        val featureValueByKey = loadAndroidFeatureFlags()
            .mapNotNull(::mapFeatureValueByKey)
            .toMap()

        logMissingBackendFeatureFlags(featureValueByKey = featureValueByKey)

        return FeatureFlagsSnapshot(
            featureValueByKey = featureValueByKey
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
        featureValueByKey: Map<String, Boolean>
    ) {
        val missingBackendFeatureFlags = featureToggleRegistry.getFeatureToggles()
            .filter { featureToggle ->
                featureValueByKey.containsKey(featureToggle.key).not()
            }

        if (missingBackendFeatureFlags.isEmpty()) {
            return
        }

        val missingBackendValues = missingBackendFeatureFlags.joinToString { featureKey ->
            featureKey.key
        }

        Timber.tag(FEATURE_TOGGLE_LOG_TAG)
            .w(MISSING_BACKEND_FEATURE_FLAGS_LOG_MESSAGE, missingBackendValues)
    }

    private fun mapFeatureValueByKey(
        remoteFeatureFlag: GetFeatureFlagResponse
    ): Pair<String, Boolean>? {
        val featureToggle = featureToggleRegistry.getFeatureToggle(key = remoteFeatureFlag.flag)

        if (featureToggle == null) {
            Timber.tag(FEATURE_TOGGLE_LOG_TAG)
                .w(UNKNOWN_BACKEND_FEATURE_FLAG_LOG_MESSAGE, remoteFeatureFlag.flag)
            return null
        }

        return featureToggle.key to remoteFeatureFlag.enabled
    }
}

private const val FEATURE_TOGGLE_LOG_TAG = "FeatureToggle"
private const val UNKNOWN_BACKEND_FEATURE_FLAG_LOG_MESSAGE = "Unknown backend feature flag: %s"
private const val MISSING_BACKEND_FEATURE_FLAGS_LOG_MESSAGE =
    "Feature flags are missing in backend response: %s"
private const val INITIAL_PAGE = 1
private const val PAGE_LIMIT = 100
