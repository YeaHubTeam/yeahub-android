package ru.yeahub.public_questions.impl.presentation.command

sealed class QuestionsScreenCommand {
    data object OnBackClick : QuestionsScreenCommand()
    data class OnMoreClick(val id: String, val title: String) : QuestionsScreenCommand()
}