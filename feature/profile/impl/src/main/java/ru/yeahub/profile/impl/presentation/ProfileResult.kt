package ru.yeahub.profile.impl.presentation

sealed interface ProfileResult {

    data class OpenSocialNetwork(
        val code: String,
        val url: String
    ) : ProfileResult

    object NavigateBack : ProfileResult
}