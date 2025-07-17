package ru.yeahub.example_details.api

import androidx.compose.runtime.Composable

/**
 * API интерфейс для экрана деталей.
 * 
 * Демонстрирует:
 * - Передачу параметров между экранами
 * - Поддержку DeepLink
 */
interface DetailsScreenApi {
    /**
     * Экран деталей.
     * 
     * @param itemId ID элемента для отображения
     * @param title Заголовок элемента
     * @param currentPath Текущий путь навигации (для демонстрации)
     * @param onBackClick Действие при нажатии кнопки "Назад"
     */
    @Composable
    fun DetailsScreen(
        itemId: String,
        title: String,
        currentPath: String,
        onBackClick: () -> Unit
    )
} 