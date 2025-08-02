package ru.yeahub.public_questions.impl.data.repository

import ru.yeahub.public_questions.impl.data.mapper.DataToDomainMapper
import ru.yeahub.public_questions.impl.data.repository.remote.QuestionsRemoteDataSourceApi
import ru.yeahub.public_questions.impl.domain.entity.QuestionsModel
import ru.yeahub.public_questions.impl.domain.repository.QuestionsRepositoryApi

class QuestionsRepositoryImpl(
    val remoteDataSource: QuestionsRemoteDataSourceApi,
    val mapper: DataToDomainMapper
) : QuestionsRepositoryApi {

    override suspend fun getQuestion(
        page: Int,
        limit: Int,
        skills: List<String>?,
        skillFilter: String?
    ): QuestionsModel {
        val response = remoteDataSource.requestQuestionsApi(
                page = page,
                limit = limit,
                skills = skills,
                skillFilterMode = skillFilter
            )
        return mapper.mapGetPublicQuestionsResponseToDomain(response)
    }
}
