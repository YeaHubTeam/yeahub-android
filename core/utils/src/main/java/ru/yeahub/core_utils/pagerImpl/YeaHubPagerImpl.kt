package ru.yeahub.core_utils.pagerImpl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.yeahub.core_utils.pager.YeaHubPager
import ru.yeahub.core_utils.pager.YeaHubPagerLoader

class YeaHubPagerImpl<T : Any, ItemType : Any, RequestType : Any>(
    private val pagerLoader: YeaHubPagerLoader<T, RequestType>,      // Загрузка страницы
    private val requestData: RequestType,
    private val data: (T) -> List<ItemType>, // Извлечение списка
    private val total: (T) -> Long,         // Получение общего числа элементов
    private val limit: (T) -> Long?         // Количество элементов на странице
) : YeaHubPager<T, ItemType> {

    private val _state = MutableStateFlow<YeaHubPagerState<ItemType>>(YeaHubPagerState.Initial)
    override val state: StateFlow<YeaHubPagerState<ItemType>> = _state
    private var currentRequest: RequestType = requestData
    private var currentPage: Int = 1
    private var currentItems = emptyList<ItemType>()

    override suspend fun load() {
        if (state.value is YeaHubPagerState.Loading) return
        if ((state.value as? YeaHubPagerState.Loaded)?.isEndReached == true) return

        _state.value = YeaHubPagerState.Loading(currentItems)
        try {
            val response = pagerLoader.loadPage(currentRequest)
            val newItems = data(response)
            val totalItems = total(response)
            val limit = limit(response) ?: 0
            currentItems = currentItems + newItems
            val isEndReached = currentPage * limit >= totalItems || newItems.isEmpty()

            _state.value = YeaHubPagerState.Loaded(currentItems, isEndReached)

            if (!isEndReached) {
                currentPage++
                currentRequest = pagerLoader.updatePage(requestData, currentPage)
            }
        } catch (e: Exception) {
            _state.value = YeaHubPagerState.Error(e)
        }
    }

    override fun reset() {
        currentPage = 1
        currentItems = emptyList()
        _state.value = YeaHubPagerState.Initial
    }

}