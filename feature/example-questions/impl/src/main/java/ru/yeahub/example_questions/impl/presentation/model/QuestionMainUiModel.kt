package ru.yeahub.example_questions.impl.presentation.model

import ru.yeahub.core_utils.common.TextOrResource

data class QuestionMainUiModel(
    val type: QuestionMainItemType,
    val title: TextOrResource,
    val description: TextOrResource,
    val imageRes: Int
)
