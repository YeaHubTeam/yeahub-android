package ru.yeahub.core_utils.pager

interface YeaHubPagerLoader<ResponseData : Any, RequestData : Any> {
    suspend fun loadPage(request: RequestData): ResponseData
    fun updatePage(request: RequestData, page: Int): RequestData
}