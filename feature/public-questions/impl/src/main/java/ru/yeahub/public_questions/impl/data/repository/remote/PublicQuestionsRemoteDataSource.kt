package ru.yeahub.public_questions.impl.data.repository.remote

import ru.yeahub.network_api.NetworkProvider
import ru.yeahub.network_api.models.GetPublicQuestionsResponse

class PublicQuestionsRemoteDataSource(private val apiService: NetworkProvider) : PublicQuestionsRemoteDataSourceApi {

    override suspend fun requestPublicQuestionsApi(
        page: Int,
        limit: Int,
        skillFilterMode: String?,
        idCollection: Int?,
        idSpecialization: Int?
    ): GetPublicQuestionsResponse {
        return apiService.apiService.getQuestions(
            page = page,
            limit = limit,
            skillFilterMode = skillFilterMode,
            collection = idCollection,
            specializationId = idSpecialization
        )
    }
}