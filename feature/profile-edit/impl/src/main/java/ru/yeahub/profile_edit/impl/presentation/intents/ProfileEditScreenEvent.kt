package ru.yeahub.profile_edit.impl.presentation.intents

import android.net.Uri
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState

sealed interface ProfileEditScreenEvent {

    data object LoadData : ProfileEditScreenEvent
    data class SwitchTab(val tab: ProfileEditState.ProfileEditTabs) : ProfileEditScreenEvent
    data object BackPressed : ProfileEditScreenEvent
    data object DiscardChanges : ProfileEditScreenEvent
    data object SaveProfile : ProfileEditScreenEvent

    data object UploadAvatar : ProfileEditScreenEvent
    data class OnAvatarSelected(val uri: Uri) : ProfileEditScreenEvent
    data object DeleteAvatar : ProfileEditScreenEvent
    data class OnNicknameChanged(val nickname: String) : ProfileEditScreenEvent
    data class ChooseSpecialization(val specialization: String) : ProfileEditScreenEvent
    data object CannotChangeSpecializationToast : ProfileEditScreenEvent
    data class OnLocationChanged(val location: String) : ProfileEditScreenEvent
    data class OnSocialLinkChanged(
        val link: ProfileEditState.SocialLinks,
        val url: String,
    ) : ProfileEditScreenEvent

    data class OnAboutMeChanged(val text: String) : ProfileEditScreenEvent

    data class AddSkill(val skill: ProfileEditState.Skill) : ProfileEditScreenEvent
    data class RemoveSkill(val skill: ProfileEditState.Skill) : ProfileEditScreenEvent
}