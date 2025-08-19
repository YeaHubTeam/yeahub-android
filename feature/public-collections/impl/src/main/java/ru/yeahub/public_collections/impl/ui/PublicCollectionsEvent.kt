package ru.yeahub.public_collections.impl.ui

sealed class PublicCollectionsEvent {

    /** Первичная загрузка */

    data object LoadInitial : PublicCollectionsEvent()

    /** Подгрузка следующей страницы */

    data object LoadNextPage : PublicCollectionsEvent()

    /** Обновление данных */

    data object Refresh : PublicCollectionsEvent()

    /** Клик по карточке собеса (перейти к вопросам) */

    data class OpenDetailScreen(val id: String) : PublicCollectionsEvent()

    /** Клик назад */

    data object OnBackClick : PublicCollectionsEvent()
}