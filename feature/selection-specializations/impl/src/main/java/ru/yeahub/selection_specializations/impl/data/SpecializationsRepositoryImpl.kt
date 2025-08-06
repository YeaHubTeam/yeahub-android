package ru.yeahub.selection_specializations.impl.data

import ru.yeahub.selection_specializations.impl.model.DomainSpecilializationListResponse
import ru.yeahub.selection_specializations.impl.model.SpecializationsRequest

class SpecializationsRepositoryImpl(
    val remoteRepository: SpecializationsRemoteRepository,
    val mapper: MapperDataToDomain
) : SpecializationsRepository {
    //without responses cache impl, only online
    override suspend fun getSpecilizationsList(
        request: SpecializationsRequest
    ): DomainSpecilializationListResponse =
        mapper
            .mapListResponceToDomain(
                remoteRepository.requestSpecilizationsList(request)
            )
}