package ru.yeahub.feature_toggle_impl.data

import ru.yeahub.network_api.ApiService
import ru.yeahub.network_api.models.GetFeatureFlagResponse
import ru.yeahub.network_api.models.GetFeatureFlagsResponse

internal class FeatureFlagsRemoteDataSource(
    private val apiService: ApiService
) {

    suspend fun loadAndroidFeatureFlags(): List<GetFeatureFlagResponse> {
        val featureFlags = mutableListOf<GetFeatureFlagResponse>()
        var currentPage = INITIAL_PAGE

        while (true) {
            val response = loadAndroidFeatureFlagsPage(
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

    private suspend fun loadAndroidFeatureFlagsPage(
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
private const val INITIAL_PAGE = 1
private const val PAGE_LIMIT = 100
