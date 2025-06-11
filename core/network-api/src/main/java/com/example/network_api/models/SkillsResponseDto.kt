package com.example.network_api.models

data class SkillsResponseDto(
    val page: Int,
    val limit: Int,
    val total: Int,
    val data: List<SkillsDto>
)
