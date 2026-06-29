package ru.yeahub.interview_trainer.impl.interviewQuiz.domain

data class DomainQuestionsListResponse(
    val fullCount: Int,
    val questions: List<DomainQuestion>
)
