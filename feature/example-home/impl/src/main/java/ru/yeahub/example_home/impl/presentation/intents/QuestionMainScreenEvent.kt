package ru.yeahub.example_home.impl.presentation.intents

import ru.yeahub.example_home.impl.presentation.model.QuestionMainUiModel

sealed class QuestionMainScreenEvent {
    data class OnItemClick(val item: QuestionMainUiModel) : QuestionMainScreenEvent()
}
