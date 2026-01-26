package ru.yeahub.interview_trainer.impl.createQuiz.domain

class GetSpecializationsListUseCase(
    private val repository: CreateQuizRepositoryApi,
) {
    suspend operator fun invoke(
        request: SpecializationsRequest,
    ): DomainSpecializationListResponse = repository.getSpecializationsList(request = request)
}

data class SpecializationsRequest(
    val page: Int,
    val limit: Int,
)