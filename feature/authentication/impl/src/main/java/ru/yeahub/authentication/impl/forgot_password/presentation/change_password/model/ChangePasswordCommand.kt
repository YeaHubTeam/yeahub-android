package ru.yeahub.authentication.impl.forgot_password.presentation.change_password.model

import ru.yeahub.core_utils.common.TextOrResource

sealed interface ChangePasswordCommand {
    data object NavigateToProfile : ChangePasswordCommand
    data class ShowSnackbar(val message: TextOrResource) : ChangePasswordCommand
}
