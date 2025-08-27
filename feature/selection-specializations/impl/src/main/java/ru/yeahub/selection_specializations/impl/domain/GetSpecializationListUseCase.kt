package ru.yeahub.selection_specializations.impl.domain

interface GetSpecializationListUseCase {
    suspend operator fun invoke(
        request: SpecializationsRequest,
    ): DomainSpecilializationListResponse
}

data class SpecializationsRequest(
    val page: Int,
    val limit: Int
)