package ru.yeahub.home.impl.presentation.screen

import ru.yeahub.home.impl.domain.interview.InterviewData

sealed class InterviewPreparationState {
    data object Initial : InterviewPreparationState()
    data object Loading : InterviewPreparationState()
    data class Loaded(val interview: InterviewData) : InterviewPreparationState()
    data class Error(val throwable: Throwable) : InterviewPreparationState()
}
