package ru.yeahub.network_api.models

data class GetSkillsResponse(
    val page: Long?,
    val limit: Long?,
    val data: List<GetSkillResponse>,
    val total: Long
)
