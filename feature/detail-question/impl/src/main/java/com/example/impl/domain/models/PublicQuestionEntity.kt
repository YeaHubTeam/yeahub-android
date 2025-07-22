package com.example.impl.domain.models

data class PublicQuestionEntity(
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
    val questionSpecializations: List<NestedSpecializationEntity>,
    val questionSkills: List<NestedSkillEntity>,
    val createdAt: String,
    val updatedAt: String,
    val createdBy: NestedUserReferenceEntity,
    val updatedBy: NestedUserReferenceEntity?,
    val guru: GuruEntity
)