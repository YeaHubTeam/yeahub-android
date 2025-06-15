package ru.yeahub.network_api.models

data class GetPublicQuestionsResponse(
    val page: Long?,
    val limit: Long?,
    val data: List<GetQuestionResponse>,
    val total: Long
)