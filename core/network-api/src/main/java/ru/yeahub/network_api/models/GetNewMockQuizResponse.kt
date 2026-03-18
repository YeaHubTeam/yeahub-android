package ru.yeahub.network_api.models

data class GetNewMockQuizResponse(
    val id: String,
    val startDate: String,
    val fullCount: Int,
    val skills: List<String>,
    val response: List<QuestionAnswerDto>,
    val questions: List<GetQuestionResponse>
)