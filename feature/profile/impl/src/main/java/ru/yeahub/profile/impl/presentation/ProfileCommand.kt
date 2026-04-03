package ru.yeahub.profile.impl.presentation

sealed interface ProfileCommand {

    data class OpenSocialNetwork(
        val code: String,
        val url: String
    ) : ProfileCommand

    object NavigateBack : ProfileCommand
}