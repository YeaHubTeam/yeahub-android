package ru.yeahub.selection_specializations.impl.data

import ru.yeahub.network_api.NetworkProvider
import ru.yeahub.network_api.models.GetSpecializationsResponse
import ru.yeahub.selection_specializations.impl.model.SpecializationsRequest

class SpecializationsRemoteRepositoryImpl(
    private val apiService: NetworkProvider
) : SpecializationsRemoteRepository {
    override suspend fun requestSpecializationsList(
        request: SpecializationsRequest
    ): GetSpecializationsResponse =
        apiService.apiService.getSpecializations(
            page = request.page,
            limit = request.limit
        )
}