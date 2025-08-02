package ru.yeahub.public_questions.impl.data.repository.remote

import ru.yeahub.network_api.NetworkProvider
import ru.yeahub.network_api.models.GetPublicQuestionsResponse

class QuestionsRemoteDataSource(private val apiService: NetworkProvider) : QuestionsRemoteDataSourceApi {

    override suspend fun requestQuestionsApi(
        page: Int,
        limit: Int,
        skills: List<String>?,
        skillFilterMode: String?,
    ): GetPublicQuestionsResponse {
        return apiService.apiService.getQuestions(
            page = page,
            limit = limit,
            skills = skills,
            skillFilterMode = skillFilterMode,
        )
    }
}