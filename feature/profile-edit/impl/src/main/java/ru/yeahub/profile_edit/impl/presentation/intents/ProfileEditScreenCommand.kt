package ru.yeahub.profile_edit.impl.presentation.intents

import ru.yeahub.core_utils.common.TextOrResource

internal sealed interface ProfileEditScreenCommand {
    data object ShowPhotoPicker : ProfileEditScreenCommand
    data class ShowError(val message: TextOrResource) : ProfileEditScreenCommand
    data object NavigateBack : ProfileEditScreenCommand
    data object NavigateToProfile : ProfileEditScreenCommand
    data object ShowCannotChangeSpecializationToast : ProfileEditScreenCommand
}