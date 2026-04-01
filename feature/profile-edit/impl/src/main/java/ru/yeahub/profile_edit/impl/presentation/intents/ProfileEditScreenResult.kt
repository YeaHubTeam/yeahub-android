package ru.yeahub.profile_edit.impl.presentation.intents

internal sealed interface ProfileEditScreenResult {
    data object NavigateBack : ProfileEditScreenResult
    data object NavigateToProfile : ProfileEditScreenResult
}
