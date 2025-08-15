package ru.yeahub.public_questions.impl.presentation.screen

sealed class PublicQuestionsScreenState() {
    data object Initial : PublicQuestionsScreenState()

    data object Loading : PublicQuestionsScreenState()

    data class Loaded(
        val questions: List<PublicQuestionUiModel>,
        val isEndReached: Boolean,
        val isLoadingNextPage: Boolean
    ) : PublicQuestionsScreenState()

    data class Error(
        val questions: List<PublicQuestionUiModel>,
        val throwable: Throwable
    ) : PublicQuestionsScreenState()
}