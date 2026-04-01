package ru.yeahub.profile_edit.impl.presentation

import kotlinx.collections.immutable.PersistentList
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill

internal data class ProfileEditStaticData(
    val email: String,
    val specializationList: PersistentList<String>,
    val isSpecializationEditable: Boolean,
    val allSkills: PersistentList<DomainProfileEditSkill>,
)
