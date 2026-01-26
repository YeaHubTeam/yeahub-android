package ru.yeahub.interview_trainer.impl.createQuiz.domain

interface CreateQuizRepositoryApi {
    suspend fun getSpecializationsList(): DomainSpecializationListResponse
}