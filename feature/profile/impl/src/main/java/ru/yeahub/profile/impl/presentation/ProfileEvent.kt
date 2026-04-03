package ru.yeahub.profile.impl.presentation

sealed interface ProfileEvent {

    data class OnSocialNetworkClicked(
        val code: String,
        val url: String
    ) : ProfileEvent

    data object OnBackClick : ProfileEvent
}