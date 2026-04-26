package ru.yeahub.profile_edit.impl.presentation.intents

import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSocialPlatform
import ru.yeahub.profile_edit.impl.presentation.ProfileEditImageValidationError

sealed interface ProfileEditScreenEvent {

    data object ErrorScreenRetryClicked : ProfileEditScreenEvent
    data object BackClicked : ProfileEditScreenEvent
    data object DiscardChangesClicked : ProfileEditScreenEvent
    data object UnsavedChangesDialogDismissed : ProfileEditScreenEvent
    data object SaveProfileClicked : ProfileEditScreenEvent

    data object UploadAvatarClicked : ProfileEditScreenEvent
    class AvatarSelected(
        val previewUrl: String,
        val avatarBytes: ByteArray,
    ) : ProfileEditScreenEvent

    data object DeleteAvatarClicked : ProfileEditScreenEvent
    data class NicknameChanged(val nickname: String) : ProfileEditScreenEvent
    data class SpecializationSelected(val specialization: String) : ProfileEditScreenEvent
    data object ChangeSpecializationClicked : ProfileEditScreenEvent
    data class LocationChanged(val location: String) : ProfileEditScreenEvent
    data class SocialLinkChanged(
        val platform: DomainProfileEditSocialPlatform,
        val url: String,
    ) : ProfileEditScreenEvent

    data class AboutMeChanged(val text: String) : ProfileEditScreenEvent

    data class AddSkillClicked(val skillName: String) : ProfileEditScreenEvent
    data class RemoveSkillClicked(val skill: DomainProfileEditSkill) : ProfileEditScreenEvent

    data class ImageValidationFailed(
        val error: ProfileEditImageValidationError,
    ) : ProfileEditScreenEvent

    data object SnackbarRetryClicked : ProfileEditScreenEvent
    data object ErrorSnackbarDismissed : ProfileEditScreenEvent
}
