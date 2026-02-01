package ru.yeahub.interview_trainer.impl.createQuiz.domain

class GetSpecializationsListUseCaseImpl(
    private val repository: CreateQuizRepositoryApi,
) : GetSpecializationsUseCase {
    override suspend fun invoke(
        request: SpecializationsRequest,
    ): DomainSpecializationListResponse = repository.getSpecializationsList(request = request)
}