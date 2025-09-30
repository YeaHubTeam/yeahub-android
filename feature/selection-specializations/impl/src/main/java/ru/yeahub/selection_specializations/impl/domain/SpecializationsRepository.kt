package ru.yeahub.selection_specializations.impl.domain

interface SpecializationsRepository {
    suspend fun getSpecializationList(
        request: SpecializationsRequest,
    ): DomainSpecilializationListResponse
}