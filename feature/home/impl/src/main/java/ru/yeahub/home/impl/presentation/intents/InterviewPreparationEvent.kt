package ru.yeahub.home.impl.presentation.intents

sealed class InterviewPreparationEvent {
    data object Retry : InterviewPreparationEvent()
}
