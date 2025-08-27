package ru.yeahub.selection_specializations.impl.data

import ru.yeahub.network_api.models.GetSpecializationResponse
import ru.yeahub.network_api.models.GetSpecializationsResponse
import ru.yeahub.selection_specializations.impl.domain.DomainSpecilialization
import ru.yeahub.selection_specializations.impl.domain.DomainSpecilializationListResponse

class SpecializationSelectionDataToDomainMapper {
    fun dataListToDomainList(
        dataResponce: GetSpecializationsResponse
    ): DomainSpecilializationListResponse =
        DomainSpecilializationListResponse(
            page = dataResponce.page ?: 1L,
            limit = dataResponce.limit ?: 10L,
            data = dataResponce.data.map { dataItem -> dataToDomain(dataItem) },
            total = dataResponce.total
        )

    //none private for using in unit-tests
    fun dataToDomain(data: GetSpecializationResponse): DomainSpecilialization =
        DomainSpecilialization(
            id = data.id,
            title = data.title
        )
}