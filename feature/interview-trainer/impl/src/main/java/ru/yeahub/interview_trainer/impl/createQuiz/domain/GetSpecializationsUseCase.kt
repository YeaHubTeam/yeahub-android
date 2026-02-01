package ru.yeahub.interview_trainer.impl.createQuiz.domain

interface GetSpecializationsUseCase {
    suspend operator fun invoke(request: SpecializationsRequest): DomainSpecializationListResponse
}