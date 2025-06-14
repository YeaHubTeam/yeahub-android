package ru.yeahub.network_api.models

data class GetQuestionsResponse(
    val page: Long?,
    val limit: Long?,
    val data: List<GetQuestionWithProfileDataResponse>,
    val total: Long
)