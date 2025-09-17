package ru.yeahub.public_questions.impl.data.repository.remote

import ru.yeahub.network_api.NetworkProvider
import ru.yeahub.network_api.models.GetPublicQuestionsResponse

class PublicQuestionsRemoteDataSource(private val apiService: NetworkProvider) : PublicQuestionsRemoteDataSourceApi {

    override suspend fun requestPublicQuestionsApi(
        page: Int,
        limit: Int,
        skills: List<String>?,
        skillFilterMode: String?,
        idCollection: Int?
    ): GetPublicQuestionsResponse {
        return apiService.apiService.getQuestions(
            page = page,
            limit = limit,
            skills = skills,
            skillFilterMode = skillFilterMode,
            collection = idCollection
        )
    }
}