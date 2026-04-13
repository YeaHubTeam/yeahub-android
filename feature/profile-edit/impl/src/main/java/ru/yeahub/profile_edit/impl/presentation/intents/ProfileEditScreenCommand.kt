package ru.yeahub.profile_edit.impl.presentation.intents

sealed interface ProfileEditScreenCommand {
    data object ShowPhotoPicker : ProfileEditScreenCommand
    data object NavigateBack : ProfileEditScreenCommand
    data object NavigateToProfile : ProfileEditScreenCommand
    data object ShowCannotChangeSpecializationToast : ProfileEditScreenCommand
}