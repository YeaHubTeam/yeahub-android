package ru.yeahub.public_collections.impl.presentation.intents

sealed interface PublicCollectionsScreenEvent {
    /** Первичная загрузка */

    data object LoadInitial : PublicCollectionsScreenEvent

    /** Подгрузка следующей страницы */

    data object LoadNextPage : PublicCollectionsScreenEvent

    /** Обновление данных */

    data object Refresh : PublicCollectionsScreenEvent

    /** Клик по карточке коллекции(подробнее) */

    data class OnQuestionsListClick(val collectionId: Int, val title: String) : PublicCollectionsScreenEvent

    /** Клик назад */

    data object OnBackClick : PublicCollectionsScreenEvent
}