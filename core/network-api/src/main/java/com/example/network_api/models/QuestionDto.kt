package com.example.network_api.models

data class QuestionDto(
    val id: Long,
    val title: String,
    val description: String,
    val code: String?,
    val imageSrc: String?,
    val keywords: List<String>,
    val longAnswer: String?,
    val shortAnswer: String?,
    val status: String,
    val rate: Int,
    val complexity: Int,
    val createdById: String,
    val updatedById: String,
    val questionSpecializations: List<SpecializationDto>,
    val questionSkills: List<SkillsDto>,
    val createdAt: String,
    val updatedAt: String,
    val createdBy: UserDto,
    val updatedBy: UserDto,
    val checksCount: Int,
    val isLearned: Boolean,
    val isFavorite: Boolean,
    val profileId: String,
    val lastUpdate: String
)