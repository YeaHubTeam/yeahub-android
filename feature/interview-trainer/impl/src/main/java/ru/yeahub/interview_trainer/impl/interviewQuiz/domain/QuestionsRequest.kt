package ru.yeahub.interview_trainer.impl.interviewQuiz.domain

data class QuestionsRequest(
    val limit: Int,
    val specialization: Int,
)