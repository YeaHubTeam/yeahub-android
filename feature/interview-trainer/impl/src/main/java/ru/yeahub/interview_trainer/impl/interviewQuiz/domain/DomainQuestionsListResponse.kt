package ru.yeahub.interview_trainer.impl.interviewQuiz.domain

data class DomainQuestionsListResponse(
    val total: Long,
    val data: List<DomainQuestion>
)
