package ru.yeahub.selection_specializations.impl.domain

class GetSpecializationListUseCaseImpl(
    private val repository: SpecializationsRepository,
) : GetSpecializationListUseCase {
    override suspend fun invoke(
        request: SpecializationsRequest,
    ): DomainSpecilializationListResponse =
        repository.getSpecializationList(request = request)
}