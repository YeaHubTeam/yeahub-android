package ru.yeahub.profile_edit.impl.domain.models

data class DomainProfileEditData(
    val email: String?,
    val avatarUrl: String,
    val nickname: String,
    val specialization: String?,
    val specializationList: List<String>,
    val location: String,
    val socialLinks: Map<DomainProfileEditSocialPlatform, String>,
    val aboutMe: String,
    val selectedSkills: List<DomainProfileEditSkill>,
    val allSkills: List<DomainProfileEditSkill>,
)
