package ru.yeahub.interview_trainer.impl.interviewQuiz.data

import ru.yeahub.interview_trainer.impl.interviewQuiz.domain.DomainQuestion
import ru.yeahub.interview_trainer.impl.interviewQuiz.domain.DomainQuestionsListResponse
import ru.yeahub.network_api.models.GetPublicQuestionsResponse
import ru.yeahub.network_api.models.GetQuestionResponse

class InterviewQuizDataToDomainMapper {

    fun mapDataListToDomainList(
        dataResponse: GetPublicQuestionsResponse
    ): DomainQuestionsListResponse {
        return DomainQuestionsListResponse(
            total = dataResponse.total,
            data = dataResponse.data.map { item -> dataToDomain(item) }
        )
    }

    private fun dataToDomain(data: GetQuestionResponse): DomainQuestion {
        return DomainQuestion(
            id = data.id,
            title = data.title,
            shortAnswer = data.shortAnswer ?: ""
        )
    }
}