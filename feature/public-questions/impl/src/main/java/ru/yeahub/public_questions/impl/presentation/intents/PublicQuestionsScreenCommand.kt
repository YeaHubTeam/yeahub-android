package ru.yeahub.public_questions.impl.presentation.intents

sealed class PublicQuestionsScreenCommand {
    data object OnBackClick : PublicQuestionsScreenCommand()
    data class OnMoreClick(
        val questionIds: List<String>,
        val currentIndex: Int
    ) : PublicQuestionsScreenCommand()
}