package ru.yeahub.example_home.impl.presentation.state

import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.example_home.impl.presentation.model.QuestionMainUiModel

sealed class QuestionMainScreenState {
    object Loading : QuestionMainScreenState()

    data class Content(
        val items: List<QuestionMainUiModel>
    ) : QuestionMainScreenState()

    data class Error(val message: TextOrResource) : QuestionMainScreenState()
}
