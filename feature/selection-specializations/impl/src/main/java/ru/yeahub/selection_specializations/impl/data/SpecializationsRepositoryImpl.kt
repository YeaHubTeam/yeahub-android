package ru.yeahub.selection_specializations.impl.data

import ru.yeahub.selection_specializations.impl.model.DomainSpecilializationListResponse
import ru.yeahub.selection_specializations.impl.model.SpecializationsRequest

class SpecializationsRepositoryImpl(
    private val remoteRepository: SpecializationsRemoteRepository,
    private val mapper: SpecializationSelectionDataToDomainMapper
) : SpecializationsRepository {
    //without responses cache impl, only online
    override suspend fun getSpecializationsList(
        request: SpecializationsRequest
    ): DomainSpecilializationListResponse =
        mapper
            .dataListToDomainList(
                remoteRepository.requestSpecializationsList(request)
            )
}