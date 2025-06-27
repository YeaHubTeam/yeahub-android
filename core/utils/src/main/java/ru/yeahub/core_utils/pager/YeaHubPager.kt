package ru.yeahub.core_utils.pager

import kotlinx.coroutines.flow.StateFlow
import ru.yeahub.core_utils.pagerImpl.YeaHubPagerState

interface YeaHubPager<T : Any, ItemType : Any> {
    val state: StateFlow<YeaHubPagerState<ItemType>> // Состояние пагинатора
    suspend fun load() // Загрузка страниц данных
    fun reset() // Сброс пагинатора
}