package ru.yeahub.public_questions.impl.presentation.intents

sealed class PublicQuestionsScreenCommand {
    data object OnBackClick : PublicQuestionsScreenCommand()
    data class OnMoreClick(val id: String) : PublicQuestionsScreenCommand()
}