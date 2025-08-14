package ru.yeahub.selection_specializations.impl.data

import ru.yeahub.network_api.models.GetSpecializationsResponse
import ru.yeahub.selection_specializations.impl.model.SpecializationsRequest

interface SpecializationsRemoteRepository {
    suspend fun requestSpecializationsList(
        request: SpecializationsRequest
    ): GetSpecializationsResponse
}