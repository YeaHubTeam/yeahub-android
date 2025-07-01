package ru.yeahub.core_utils.pagerImpl

sealed class YeaHubPagerState<out ItemType> {
    data object Initial : YeaHubPagerState<Nothing>()
    data class Loading<ItemType>(
        val items: List<ItemType>
    ) : YeaHubPagerState<ItemType>()

    data class Loaded<ItemType>(
        val items: List<ItemType>,
        val isEndReached: Boolean
    ) : YeaHubPagerState<ItemType>()

    data class Error<ItemType>(
        val items: List<ItemType>,
        val throwable: Throwable
    ) : YeaHubPagerState<ItemType>()
}