package ru.yeahub.public_questions.impl.presentation.intents

sealed class PublicQuestionsResult {
    data object NavigateBack : PublicQuestionsResult()
    data class NavigateToDetail(val id: String) : PublicQuestionsResult()
}