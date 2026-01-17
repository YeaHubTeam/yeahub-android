package ru.yeahub.interview_trainer.impl.createQuiz.domain

interface GetSpecializationListUseCase {

    suspend operator fun invoke(): DomainSpecializationListResponse
}