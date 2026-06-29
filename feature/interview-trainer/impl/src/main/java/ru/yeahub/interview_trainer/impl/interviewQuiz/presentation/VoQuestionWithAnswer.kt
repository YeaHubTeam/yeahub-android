package ru.yeahub.interview_trainer.impl.interviewQuiz.presentation

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class VoQuestionWithAnswer(
    val id: Long,
    val title: String,
    val shortAnswer: String,
    val userAnswer: QuizAnswerResult
) : Parcelable

@Parcelize
enum class QuizAnswerResult : Parcelable {
    KNOWN,
    UNKNOWN
}
