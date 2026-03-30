package ru.yeahub.interview_trainer.impl.createQuiz.presentation

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
sealed interface CreateQuizState {
    //Изначальный
    data object Loading : CreateQuizState

    data class Loaded(
        val specializations: ImmutableList<VoSpecialization>,
        val selectedSpecializationId: Long,
        val questionsCount: Int,
    ) : CreateQuizState {
        @Immutable
        data class VoSpecialization(
            val id: Long,
            val title: String,
        )
    }

    data class Error(val throwable: Throwable) : CreateQuizState
}