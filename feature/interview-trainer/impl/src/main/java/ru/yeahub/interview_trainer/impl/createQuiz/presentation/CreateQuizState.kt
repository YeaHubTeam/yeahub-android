package ru.yeahub.interview_trainer.impl.createQuiz.presentation

sealed interface CreateQuizState {
    //Изначальный
    data object Loading : CreateQuizState

    data class Loaded(
        val specializations: List<VoSpecialization>,
        val selectedSpecializationId: Long = 11,
        val questionsCount: Int = 1,
    ) : CreateQuizState

    data class Error(val throwable: Throwable) : CreateQuizState
}