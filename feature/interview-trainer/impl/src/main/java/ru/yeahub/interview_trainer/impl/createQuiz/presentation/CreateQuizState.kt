package ru.yeahub.interview_trainer.impl.createQuiz.presentation

import androidx.compose.runtime.Immutable

sealed interface CreateQuizState {
    //Изначальный
    data object Loading : CreateQuizState

    data class Loaded(
        val specializations: List<VoSpecialization>,
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