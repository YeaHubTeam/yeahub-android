package ru.yeahub.example_questions.impl.domain

import ru.yeahub.example_questions.impl.presentation.RequestQuestionsData
import ru.yeahub.network_api.models.GetPublicQuestionsResponse

interface QuestionsRepository {
    suspend fun getQuestions(request: RequestQuestionsData): GetPublicQuestionsResponse
}