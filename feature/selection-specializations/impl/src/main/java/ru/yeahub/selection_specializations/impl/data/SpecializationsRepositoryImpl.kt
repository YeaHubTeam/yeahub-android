package ru.yeahub.selection_specializations.impl.data

import ru.yeahub.network_api.NetworkProvider
import ru.yeahub.selection_specializations.impl.domain.SpecializationsRepository
import ru.yeahub.selection_specializations.impl.model.DomainSpecilializationListResponse
import ru.yeahub.selection_specializations.impl.model.SpecializationsRequest

class SpecializationsRepositoryImpl(
    private val apiService: NetworkProvider,
    private val mapper: SpecializationSelectionDataToDomainMapper
) : SpecializationsRepository {

    override suspend fun invoke(
        request: SpecializationsRequest
    ): DomainSpecilializationListResponse =
        mapper.dataListToDomainList(
            apiService.apiService.getSpecializations(
                page = request.page,
                limit = request.limit,
            )
        )
}