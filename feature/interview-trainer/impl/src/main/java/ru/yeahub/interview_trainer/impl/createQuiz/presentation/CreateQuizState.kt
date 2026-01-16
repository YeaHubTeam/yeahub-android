package ru.yeahub.interview_trainer.impl.createQuiz.presentation

import androidx.compose.runtime.Immutable

sealed interface CreateQuizState {
    //Изначальный
    data object Loading : CreateQuizState

    data class Loaded(
        val specializations: List<VoSpecialization>,
        val selectedSpecializationId: Long = 11,
        val questionsCount: Int = 1,
    ) : CreateQuizState {
        @Immutable
        data class VoSpecialization(
            val id: Long,
            val title: String,
        )
    }

    data class Error(val throwable: Throwable) : CreateQuizState
}