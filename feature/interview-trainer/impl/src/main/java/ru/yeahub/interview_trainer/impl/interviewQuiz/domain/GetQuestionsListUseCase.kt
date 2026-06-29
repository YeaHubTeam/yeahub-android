package ru.yeahub.interview_trainer.impl.interviewQuiz.domain

interface GetQuestionsListUseCase {

    suspend operator fun invoke(request: QuestionsRequest): DomainQuestionsListResponse
}