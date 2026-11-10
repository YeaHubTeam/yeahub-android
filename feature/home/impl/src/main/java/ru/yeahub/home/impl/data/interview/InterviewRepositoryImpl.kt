package ru.yeahub.home.impl.data.interview

import ru.yeahub.home.impl.domain.interview.InterviewData
import ru.yeahub.home.impl.domain.interview.InterviewRepository

class InterviewRepositoryImpl(
    private val remoteDataSource: InterviewRemoteDataSource,
    private val mapper: InterviewDataToDomainMapper
) : InterviewRepository {

    override suspend fun getInterviewData(): InterviewData =
        mapper.mapToDomain(
            remoteDataSource.requestQuestionsForLearn(page = FIRST_PAGE, limit = PAGE_LIMIT)
        )

    private companion object {
        const val FIRST_PAGE = 1
        const val PAGE_LIMIT = 10
    }
}
