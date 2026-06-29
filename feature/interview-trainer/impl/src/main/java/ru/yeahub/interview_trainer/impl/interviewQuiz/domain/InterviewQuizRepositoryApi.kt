package ru.yeahub.interview_trainer.impl.interviewQuiz.domain

interface InterviewQuizRepositoryApi {

    suspend fun getQuestionsList(
        request: QuestionsRequest
    ): DomainQuestionsListResponse
}