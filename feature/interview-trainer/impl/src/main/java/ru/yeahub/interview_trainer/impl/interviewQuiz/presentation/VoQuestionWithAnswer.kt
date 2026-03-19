package ru.yeahub.interview_trainer.impl.interviewQuiz.presentation

import androidx.compose.runtime.Immutable

@Immutable
data class VoQuestionWithAnswer(
    val id: Long,
    val title: String,
    val shortAnswer: String,
    val userAnswer: QuizAnswerResult
)

enum class QuizAnswerResult {
    KNOWN,
    UNKNOWN
}
