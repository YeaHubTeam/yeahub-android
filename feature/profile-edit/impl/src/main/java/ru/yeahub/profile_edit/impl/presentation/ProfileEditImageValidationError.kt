package ru.yeahub.profile_edit.impl.presentation

sealed interface ProfileEditImageValidationError {
    data object CannotRead : ProfileEditImageValidationError
    data object FileTooLarge : ProfileEditImageValidationError
    data object CropFailed : ProfileEditImageValidationError
}

internal class ProfileEditImageValidationException(
    val error: ProfileEditImageValidationError,
) : Exception()
