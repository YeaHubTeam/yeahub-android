package ru.yeahub.selection_specializations.impl.data

import ru.yeahub.selection_specializations.impl.model.DomainSpecilializationListResponse
import ru.yeahub.selection_specializations.impl.model.SpecializationsRequest

interface SpecializationsRepository {
    suspend fun getSpecilizationsList(
        request: SpecializationsRequest
    ): DomainSpecilializationListResponse
}