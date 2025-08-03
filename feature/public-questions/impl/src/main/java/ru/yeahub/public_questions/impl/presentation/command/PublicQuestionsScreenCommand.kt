package ru.yeahub.public_questions.impl.presentation.command

sealed class PublicQuestionsScreenCommand {
    data object OnBackClick : PublicQuestionsScreenCommand()
    data class OnMoreClick(val id: String) : PublicQuestionsScreenCommand()
}