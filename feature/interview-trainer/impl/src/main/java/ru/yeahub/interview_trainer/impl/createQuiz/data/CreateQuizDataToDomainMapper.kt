package ru.yeahub.interview_trainer.impl.createQuiz.data

import ru.yeahub.interview_trainer.impl.createQuiz.domain.DomainSpecialization
import ru.yeahub.interview_trainer.impl.createQuiz.domain.DomainSpecializationListResponse
import ru.yeahub.network_api.models.GetSpecializationResponse
import ru.yeahub.network_api.models.GetSpecializationsResponse

class CreateQuizDataToDomainMapper {
    fun dataListToDomainList(
        dataResponse: GetSpecializationsResponse,
    ): DomainSpecializationListResponse =
        DomainSpecializationListResponse(
            page = dataResponse.page ?: 1L,
            limit = dataResponse.limit ?: 10L,
            data = dataResponse.data.map { dataItem -> dataToDomain(dataItem) },
            total = dataResponse.total
        )

    private fun dataToDomain(data: GetSpecializationResponse): DomainSpecialization =
        DomainSpecialization(
            id = data.id,
            title = data.title
        )
}