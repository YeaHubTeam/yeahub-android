package ru.yeahub.public_questions.impl.presentation.intents

sealed class PublicQuestionsScreenEvent {

    /** Первичная загрузка */

    data object LoadInitial : PublicQuestionsScreenEvent()

    /** Подгрузка следующей страницы */

    data object LoadNextPage : PublicQuestionsScreenEvent()

    /** Обновление данных */

    data object Refresh : PublicQuestionsScreenEvent()

    /** Клик по карточке вопроса(подробнее) */

    data class OnMoreClick(
        val questionIds: List<String>,
        val currentIndex: Int
    ) : PublicQuestionsScreenEvent()

    /** Клик назад */

    data object OnBackClick : PublicQuestionsScreenEvent()
}