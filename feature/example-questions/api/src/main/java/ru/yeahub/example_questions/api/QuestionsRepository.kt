package ru.yeahub.example_questions.api

import ru.yeahub.network_api.models.GetPublicQuestionsResponse

interface QuestionsRepository {
    suspend fun getQuestions(request: RequestQuestionsData): GetPublicQuestionsResponse
}