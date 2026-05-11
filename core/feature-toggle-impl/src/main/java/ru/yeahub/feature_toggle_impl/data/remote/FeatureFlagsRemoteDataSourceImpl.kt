package ru.yeahub.feature_toggle_impl.data.remote

import ru.yeahub.network_api.ApiService
import ru.yeahub.network_api.models.GetFeatureFlagsResponse

internal class FeatureFlagsRemoteDataSourceImpl(
    private val apiService: ApiService
) : FeatureFlagsRemoteDataSource {

    override suspend fun getFeatureFlags(
        page: Int,
        limit: Int
    ): GetFeatureFlagsResponse {
        return apiService.getFeatureFlags(
            page = page,
            limit = limit,
            search = null,
            enabled = null,
            roleIds = null,
            clientType = ANDROID_CLIENT_TYPE
        )
    }
}

private const val ANDROID_CLIENT_TYPE = "ANDROID"