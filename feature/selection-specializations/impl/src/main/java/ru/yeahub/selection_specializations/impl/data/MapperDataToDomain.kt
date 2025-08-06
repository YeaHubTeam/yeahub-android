package ru.yeahub.selection_specializations.impl.data

import ru.yeahub.network_api.models.GetSpecializationResponse
import ru.yeahub.network_api.models.GetSpecializationsResponse
import ru.yeahub.selection_specializations.impl.model.DomainSpecilialization
import ru.yeahub.selection_specializations.impl.model.DomainSpecilializationListResponse

class MapperDataToDomain {
   fun mapToDomain(data: GetSpecializationResponse): DomainSpecilialization =
        DomainSpecilialization(
            id = data.id,
            title = data.title
        )

    fun mapListResponceToDomain(
        dataResponce: GetSpecializationsResponse
    ): DomainSpecilializationListResponse =
        DomainSpecilializationListResponse(
            page = dataResponce.page,
            limit = dataResponce.limit,
            data = dataResponce.data.map { dataItem -> mapToDomain(dataItem) },
            total = dataResponce.total
        )
}