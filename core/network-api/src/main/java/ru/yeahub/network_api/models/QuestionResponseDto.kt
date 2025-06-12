package ru.yeahub.network_api.models

data class QuestionResponseDto(
    val page: Int?,
    val limit: Int?,
    val total: Int?,
    val data: List<QuestionDto>?
)