package ru.yeahub.interview_trainer.impl.createQuiz.domain

interface GetSpecializationsListUseCase {
    suspend operator fun invoke(request: SpecializationsRequest): DomainSpecializationListResponse
}