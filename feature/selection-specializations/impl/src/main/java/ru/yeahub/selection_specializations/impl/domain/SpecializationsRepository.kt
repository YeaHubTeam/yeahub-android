package ru.yeahub.selection_specializations.impl.domain

import ru.yeahub.selection_specializations.impl.model.DomainSpecilializationListResponse
import ru.yeahub.selection_specializations.impl.model.SpecializationsRequest

interface SpecializationsRepository {
    suspend fun invoke(
        request: SpecializationsRequest,
    ): DomainSpecilializationListResponse
}