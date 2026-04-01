package ru.yeahub.profile_edit.impl.presentation.intents

import android.net.Uri
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSocialLink

internal sealed interface ProfileEditScreenEvent {

    data object LoadData : ProfileEditScreenEvent
    data object BackPressed : ProfileEditScreenEvent
    data object DiscardChanges : ProfileEditScreenEvent
    data object UnsavedChangesDialogDismissed : ProfileEditScreenEvent
    data object SaveProfile : ProfileEditScreenEvent

    data object UploadAvatar : ProfileEditScreenEvent
    data class AvatarSelected(val uri: Uri) : ProfileEditScreenEvent
    data object DeleteAvatar : ProfileEditScreenEvent
    data class NicknameChanged(val nickname: String) : ProfileEditScreenEvent
    data class SpecializationSelected(val specialization: String) : ProfileEditScreenEvent
    data object ChangeSpecializationClicked : ProfileEditScreenEvent
    data class LocationChanged(val location: String) : ProfileEditScreenEvent
    data class SocialLinkChanged(
        val link: DomainProfileEditSocialLink,
        val url: String,
    ) : ProfileEditScreenEvent

    data class AboutMeChanged(val text: String) : ProfileEditScreenEvent

    data class AddSkill(val skillName: String) : ProfileEditScreenEvent
    data class RemoveSkill(val skill: DomainProfileEditSkill) : ProfileEditScreenEvent
}