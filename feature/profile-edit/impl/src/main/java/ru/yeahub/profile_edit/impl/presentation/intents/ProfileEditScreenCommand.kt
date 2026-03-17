package ru.yeahub.profile_edit.impl.presentation.intents

import ru.yeahub.core_utils.common.TextOrResource

sealed interface ProfileEditScreenCommand {
    data object ShowUnsavedChangesDialog : ProfileEditScreenCommand
    data object ShowPhotoPicker : ProfileEditScreenCommand
    data class ShowError(val message: TextOrResource) : ProfileEditScreenCommand
}