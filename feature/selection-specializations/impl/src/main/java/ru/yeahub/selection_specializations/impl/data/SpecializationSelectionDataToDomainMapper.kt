package ru.yeahub.selection_specializations.impl.data

import ru.yeahub.network_api.models.GetSpecializationResponse
import ru.yeahub.network_api.models.GetSpecializationsResponse
import ru.yeahub.selection_specializations.impl.model.DomainSpecilialization
import ru.yeahub.selection_specializations.impl.model.DomainSpecilializationListResponse

object SpecializationSelectionDataToDomainMapper {
   fun dataToDomain(data: GetSpecializationResponse): DomainSpecilialization =
        DomainSpecilialization(
            id = data.id,
            title = data.title
        )

    fun dataListToDomainList(
        dataResponce: GetSpecializationsResponse
    ): DomainSpecilializationListResponse =
        DomainSpecilializationListResponse(
            page = dataResponce.page,
            limit = dataResponce.limit,
            data = dataResponce.data.map { dataItem -> dataToDomain(dataItem) },
            total = dataResponce.total
        )
}