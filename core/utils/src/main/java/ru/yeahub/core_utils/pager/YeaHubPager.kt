package ru.yeahub.core_utils.pager

import kotlinx.coroutines.flow.StateFlow
import ru.yeahub.core_utils.pagerImpl.YeaHubPagerState

interface YeaHubPager<T : Any, ItemType : Any> {
    // Текущее состояние пагинатора
    val state: StateFlow<YeaHubPagerState<ItemType>>

    // Загрузка следующей страницы данных
    suspend fun load()

    // Сброс состояния пагинатора
    fun reset()
}