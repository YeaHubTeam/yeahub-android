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
     * Главный/первый экран тренажера собеседований (CreateQuizScreen).
     * Является entry-point'ом в фичу
     * С него начинается вся внутренняя навигация
     *
     * @param onBackClick Действие при нажатии кнопки "Назад"
     * @param onStartTrainingClick Действие при нажатии на старт тренировки в тренажере
     */
    @Composable
    fun CreateQuizScreen(
        onBackClick: () -> Unit,
        onStartTrainingClick: (specializationId: String, questionsCount: String) -> Unit,
    )
}