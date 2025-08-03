package ru.yeahub.public_questions.impl.data.repository.remote

import ru.yeahub.network_api.models.GetPublicQuestionsResponse

interface PublicQuestionsRemoteDataSourceApi {

    suspend fun requestPublicQuestionsApi(
        page: Int,
        limit: Int,
        skills: List<String>? = null,
        skillFilterMode: String? = null,
    ): GetPublicQuestionsResponse
}