package ru.yeahub.interview_trainer.impl.createQuiz.data

import ru.yeahub.interview_trainer.impl.createQuiz.domain.CreateQuizRepositoryApi
import ru.yeahub.interview_trainer.impl.createQuiz.domain.DomainSpecializationListResponse
import ru.yeahub.interview_trainer.impl.createQuiz.domain.SpecializationsRequest
import ru.yeahub.network_api.NetworkProvider

class CreateQuizRepositoryImpl(
    private val apiService: NetworkProvider,
    private val mapper: CreateQuizDataToDomainMapper,
) : CreateQuizRepositoryApi {
    override suspend fun getSpecializationsList(
        request: SpecializationsRequest,
    ): DomainSpecializationListResponse = mapper.mapDataListToDomainList(
        dataResponse = apiService.apiService.getSpecializations(
            page = request.page,
            limit = request.limit
        )
    )
}