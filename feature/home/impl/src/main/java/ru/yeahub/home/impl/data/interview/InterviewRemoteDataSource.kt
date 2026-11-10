package ru.yeahub.home.impl.data.interview

import ru.yeahub.network_api.ApiService
import ru.yeahub.network_api.ProfileIdProvider
import ru.yeahub.network_api.models.GetQuestionsForLearnResponse

class InterviewRemoteDataSource(
    private val apiService: ApiService,
    private val profileIdProvider: ProfileIdProvider
) {

    suspend fun requestQuestionsForLearn(
        page: Int,
        limit: Int
    ): GetQuestionsForLearnResponse = apiService.getQuestionsForLearn(
        profileId = profileIdProvider.profileId(),
        page = page,
        limit = limit
    )
}
