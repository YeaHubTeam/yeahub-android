package ru.yeahub.network_api.models

/**
 * Вопрос тренажёра собеседований (AdvancedQuestionDto из API).
 * Описаны только используемые поля, остальные Gson игнорирует.
 */
data class AdvancedQuestionResponse(
    val id: Long,
    val questionId: Long,
    val title: String,
    val description: String,
    val complexity: Long?,
    val isLearned: Boolean?
)
