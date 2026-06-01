package ru.yeahub.network_api.models

data class GetNewMockQuizResponse(
    val id: String,
    val startDate: String,
    val fullCount: Int,
    val skills: List<String>,
    val response: QuizAnswersWrapperDto,
    val questions: List<GetQuestionResponse>
)