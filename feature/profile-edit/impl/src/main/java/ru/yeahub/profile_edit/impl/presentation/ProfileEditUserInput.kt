package ru.yeahub.profile_edit.impl.presentation

import kotlinx.collections.immutable.PersistentList
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSocialLink

internal data class ProfileEditUserInput(
    val avatarUrl: String?,
    val nickname: String,
    val specialization: String,
    val location: String,
    val socialLinks: Map<DomainProfileEditSocialLink, String>,
    val aboutMe: String,
    val chosenSkills: PersistentList<DomainProfileEditSkill>,
    val showUnsavedChangesDialog: Boolean,
)
