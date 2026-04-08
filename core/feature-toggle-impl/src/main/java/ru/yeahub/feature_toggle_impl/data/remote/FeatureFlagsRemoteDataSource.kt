package ru.yeahub.feature_toggle_impl.data.remote

import ru.yeahub.network_api.models.GetFeatureFlagsResponse

internal interface FeatureFlagsRemoteDataSource {

    suspend fun getFeatureFlags(
        page: Int,
        limit: Int
    ): GetFeatureFlagsResponse
}
