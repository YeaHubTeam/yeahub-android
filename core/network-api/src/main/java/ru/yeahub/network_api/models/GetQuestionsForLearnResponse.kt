package ru.yeahub.network_api.models

data class GetQuestionsForLearnResponse(
    val data: List<AdvancedQuestionResponse>,
    val page: Long?,
    val limit: Long?,
    val total: Long
)
