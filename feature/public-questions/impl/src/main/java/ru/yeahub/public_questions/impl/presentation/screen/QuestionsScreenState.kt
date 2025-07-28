package ru.yeahub.public_questions.impl.presentation.screen

sealed class QuestionsScreenState() {
    data object Initial : QuestionsScreenState()

    data object Loading : QuestionsScreenState()

    data class Loaded(
        val questions: List<QuestionUiModel>,
        val isEndReached: Boolean
    ) : QuestionsScreenState()

    data class Error(
        val questions: List<QuestionUiModel>,
        val throwable: Throwable
    ) : QuestionsScreenState()
}