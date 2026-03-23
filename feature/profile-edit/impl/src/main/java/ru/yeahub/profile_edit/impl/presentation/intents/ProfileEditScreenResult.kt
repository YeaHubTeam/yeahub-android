package ru.yeahub.profile_edit.impl.presentation.intents

sealed interface ProfileEditScreenResult {
    data object NavigateBack : ProfileEditScreenResult
    data object NavigateToProfile : ProfileEditScreenResult
}
