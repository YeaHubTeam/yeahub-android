package ru.yeahub.public_questions.impl.data.repository

import ru.yeahub.public_questions.impl.data.mapper.PublicQuestionsDataToDomainMapper
import ru.yeahub.public_questions.impl.data.repository.remote.PublicQuestionsRemoteDataSourceApi
import ru.yeahub.public_questions.impl.domain.entity.PublicQuestionsModel
import ru.yeahub.public_questions.impl.domain.repository.PublicQuestionsRepositoryApi

class PublicQuestionsRepositoryImpl(
    val remoteDataSource: PublicQuestionsRemoteDataSourceApi,
    val mapper: PublicQuestionsDataToDomainMapper
) : PublicQuestionsRepositoryApi {

    override suspend fun getPublicQuestionsQuestion(
        page: Int,
        limit: Int,
        skillFilter: String?,
        idCollection: Int?,
        idSpecialization: Int?
    ): PublicQuestionsModel {
        val response = remoteDataSource.requestPublicQuestionsApi(
            page = page,
            limit = limit,
            skillFilterMode = skillFilter,
            idSpecialization = idSpecialization,
            idCollection = idCollection
        )
        return mapper.mapGetPublicQuestionsResponseToDomain(response)
    }
}
