package ru.yeahub.example_questions.impl.presentation.intents

import ru.yeahub.example_questions.impl.presentation.model.QuestionMainUiModel

sealed class QuestionMainScreenEvent {
    data class OnItemClick(val item: QuestionMainUiModel) : QuestionMainScreenEvent()
}
