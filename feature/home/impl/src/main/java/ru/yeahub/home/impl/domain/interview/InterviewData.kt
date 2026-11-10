package ru.yeahub.home.impl.domain.interview

/**
 * Данные плашки тренажёра собеседований на главной.
 *
 * @param currentProgress номер текущего вопроса
 * @param allProgress всего вопросов в наборе
 * @param currentQuestion текст вопроса
 */
data class InterviewData(
    val currentProgress: Int,
    val allProgress: Int,
    val currentQuestion: String
)
