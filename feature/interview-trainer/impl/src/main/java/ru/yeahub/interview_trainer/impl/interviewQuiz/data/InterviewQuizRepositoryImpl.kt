package ru.yeahub.interview_trainer.impl.interviewQuiz.data

import ru.yeahub.interview_trainer.impl.interviewQuiz.domain.DomainQuestionsListResponse
import ru.yeahub.interview_trainer.impl.interviewQuiz.domain.InterviewQuizRepositoryApi
import ru.yeahub.interview_trainer.impl.interviewQuiz.domain.QuestionsRequest
import ru.yeahub.network_api.NetworkProvider

class InterviewQuizRepositoryImpl(
    private val networkProvider: NetworkProvider,
    private val mapper: InterviewQuizDataToDomainMapper
) : InterviewQuizRepositoryApi {

    override suspend fun getQuestionsList(
        request: QuestionsRequest
    ): DomainQuestionsListResponse = mapper.mapDataListToDomainList(
        dataResponse = networkProvider.apiService.getQuestions(
            page = request.page,
            limit = request.limit
        )
    )
}