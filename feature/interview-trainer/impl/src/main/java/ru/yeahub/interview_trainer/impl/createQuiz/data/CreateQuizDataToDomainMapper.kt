package ru.yeahub.interview_trainer.impl.createQuiz.data

import ru.yeahub.interview_trainer.impl.createQuiz.domain.DomainSpecialization
import ru.yeahub.interview_trainer.impl.createQuiz.domain.DomainSpecializationListResponse
import ru.yeahub.network_api.models.GetSpecializationResponse
import ru.yeahub.network_api.models.GetSpecializationsResponse

class CreateQuizDataToDomainMapper {
    fun mapDataListToDomainList(
        dataResponse: GetSpecializationsResponse,
    ): DomainSpecializationListResponse =
        DomainSpecializationListResponse(
            data = dataResponse.data.map { dataItem -> dataToDomain(dataItem) },
            total = dataResponse.total
        )

    private fun dataToDomain(data: GetSpecializationResponse): DomainSpecialization =
        DomainSpecialization(
            id = data.id,
            title = data.title
        )
}