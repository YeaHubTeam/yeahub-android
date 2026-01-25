package ru.yeahub.interview_trainer.api

import androidx.compose.runtime.Composable

/**
 * API интерфейс для экрана тренажера собеседований.
 *
 * Демонстрирует:
 * - Передачу параметров между экранами
 */
interface InterviewTrainerApi {
    /**
     * Экран тренажера собеседований.
     *
     * @param onBackClick Действие при нажатии кнопки "Назад"
     */
    @Composable
    fun InterviewTrainerScreen(
        onBackClick: () -> Unit,
    )
}