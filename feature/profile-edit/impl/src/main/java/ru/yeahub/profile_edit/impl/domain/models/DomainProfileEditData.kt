package ru.yeahub.profile_edit.impl.domain.models

internal data class DomainProfileEditData(
    val email: String,
    val avatarUrl: String?,
    val nickname: String,
    val specialization: String?,
    val specializationList: List<String>,
    val location: String,
    val socialLinks: Map<DomainProfileEditSocialLink, String>,
    val aboutMe: String,
    val chosenSkills: List<DomainProfileEditSkill>,
    val allSkills: List<DomainProfileEditSkill>,
)
