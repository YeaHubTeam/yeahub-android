package ru.yeahub.profile_edit.impl.presentation

import kotlinx.collections.immutable.PersistentList
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSocialPlatform

internal data class ProfileEditUserInput(
    val avatarUrl: String?,
    val nickname: String,
    val specialization: String,
    val location: String,
    val socialLinks: Map<DomainProfileEditSocialPlatform, String>,
    val aboutMe: String,
    val selectedSkills: PersistentList<DomainProfileEditSkill>,
    val showUnsavedChangesDialog: Boolean,
)
