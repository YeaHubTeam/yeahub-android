package ru.yeahub.example_questions.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.yeahub.core_utils.pager.YeaHubPagerLoader
import ru.yeahub.core_utils.pagerImpl.YeaHubPager
import ru.yeahub.core_utils.pagerImpl.YeaHubPagerState
import ru.yeahub.example_questions.api.RequestQuestionsData
import ru.yeahub.example_questions.impl.domain.QuestionsUseCase
import ru.yeahub.network_api.models.GetPublicQuestionsResponse
import ru.yeahub.network_api.models.GetQuestionResponse

class QuestionsViewModel(questionsUseCase: QuestionsUseCase) : ViewModel() {

    private val loader = object :
        YeaHubPagerLoader<GetPublicQuestionsResponse, RequestQuestionsData> {
        override suspend fun loadPage(request: RequestQuestionsData): GetPublicQuestionsResponse {
            return questionsUseCase.invoke(request)
        }

        override fun updatePage(request: RequestQuestionsData, page: Int): RequestQuestionsData {
            return request.copy(page = page)
        }
    }

    private val pager = YeaHubPager(
        pagerLoader = loader,
        requestData = RequestQuestionsData(1, 10),
        data = { it.data },
        total = { it.total },
        limit = { it.limit }
    )

    private val _state = pager.state
    val state: StateFlow<YeaHubPagerState<GetQuestionResponse>> = _state

    fun loadNext() = viewModelScope.launch(Dispatchers.IO) { pager.load() }
    fun refresh() {
        pager.reset()
        viewModelScope.launch(Dispatchers.IO) {
            pager.load()
        }
    }
}