package ru.yeahub.public_questions.impl.presentation.event

sealed class QuestionsScreenEvent {

    /** Первичная загрузка */

    data object LoadInitial : QuestionsScreenEvent()

    /** Подгрузка следующей страницы */

    data object LoadNextPage : QuestionsScreenEvent()

    /** Обновление данных */

    data object Refresh : QuestionsScreenEvent()

    /** Клик по карточке вопроса(подробнее) */

    data class OnMoreClick(val id: String, val title: String) : QuestionsScreenEvent()

    /** Клик назад */

    data object OnBackClick : QuestionsScreenEvent()
}