package ru.yeahub.detail_question.impl.domain.models

data class GuruEntity(
    val title: String,
    val name: String,
    val specializationId: Long,
    val photoUrl: String,
    val telegramUrl: String,
    val youtubeUrl: String
)