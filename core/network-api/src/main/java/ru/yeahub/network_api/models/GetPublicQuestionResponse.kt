package ru.yeahub.network_api.models

data class GetPublicQuestionResponse(
    val id: Long,
    val title: String,
    val description: String,
    val code: String?,
    val imageSrc: String?,
    val keywords: List<String>?,
    val longAnswer: String?,
    val shortAnswer: String?,
    val status: String?,
    val rate: Int?,
    val complexity: Long?,
    val createdById: String,
    val updatedById: String?,
    val questionSpecializations: List<NestedSpecializationResponse>,
    val questionSkills: List<NestedSkillResponse>,
    val createdAt: String,
    val updatedAt: String,
    val createdBy: NestedUserReferenceDto,
    val updatedBy: NestedUserReferenceDto?
)