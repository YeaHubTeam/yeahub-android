package ru.yeahub.interview_trainer.impl.interviewQuiz.presentation

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.interview_trainer.impl.R

sealed interface InterviewQuizState {

    val titleTopAppBar: TextOrResource

    /** Изначальное состояние */
    data object Loading : InterviewQuizState {

        override val titleTopAppBar = TextOrResource.Resource(R.string.create_quiz_top_bar_header_text)
    }

    @Immutable
    data class Loaded(
        override val titleTopAppBar: TextOrResource,
        val questions: PersistentList<VoQuestion>,
        val questionsCount: Int,
        val questionIndex: Int,
        val question: VoQuestion,
        val isAnswerVisible: Boolean,
        val answers: PersistentMap<Long, QuizAnswer>,
        val canGoPrev: Boolean,
        val canGoNext: Boolean,
        val selectedAnswer: QuizAnswer,
        val isLastQuestion: Boolean
    ) : InterviewQuizState {

        enum class QuizAnswer { KNOWN, UNKNOWN, NONE }

        @Immutable
        data class VoQuestion(
            val id: Long,
            val title: String,
            val shortAnswer: String
        )
    }

    data class Error(val throwable: Throwable) : InterviewQuizState {

        override val titleTopAppBar = TextOrResource.Resource(R.string.create_quiz_top_bar_header_text)
    }
}