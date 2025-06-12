package ru.yeahub.network_api.models

data class SkillsDto(
    val id: Int,
    val title: String,
    val description: String,
    val imageSrc: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val specializations: List<SpecializationDto>?
)
