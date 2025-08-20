package ru.yeahub.selection_specializations.impl.domain

import ru.yeahub.selection_specializations.impl.model.DomainSpecilializationListResponse
import ru.yeahub.selection_specializations.impl.model.SpecializationsRequest

class GetSpecializationListUseCaseImpl(
    private val repository: SpecializationsRepository,
) : GetSpecializationListUseCase {
    override suspend fun invoke(
        request: SpecializationsRequest,
    ): DomainSpecilializationListResponse =
        repository.getSpecializationList(request = request)
}