package ru.yeahub.public_questions.impl.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.yeahub.core_utils.BaseViewModel
import ru.yeahub.core_utils.pager.YeaHubPagerLoader
import ru.yeahub.core_utils.pagerImpl.YeaHubPager
import ru.yeahub.core_utils.pagerImpl.YeaHubPagerState
import ru.yeahub.public_questions.impl.domain.entity.PublicQuestionsModel
import ru.yeahub.public_questions.impl.domain.usecase.GetPublicQuestionsUseCase
import ru.yeahub.public_questions.impl.presentation.intents.PublicQuestionsScreenCommand
import ru.yeahub.public_questions.impl.presentation.intents.PublicQuestionsScreenEvent
import ru.yeahub.public_questions.impl.presentation.mapper.PublicQuestionDomainToPresentationMapper
import ru.yeahub.public_questions.impl.presentation.screen.PublicQuestionsScreenState

class PublicQuestionsViewModel(
    private val getPublicQuestionsUseCase: GetPublicQuestionsUseCase,
    private val mapper: PublicQuestionDomainToPresentationMapper,
    private val skillFilter: String?,
    private val idCollection: Int?,
    private val idSpecialization: Int?
) : BaseViewModel() {
    private val loader =
        object : YeaHubPagerLoader<PublicQuestionsModel, RequestPublicQuestionsData> {
            override suspend fun loadPage(request: RequestPublicQuestionsData): PublicQuestionsModel {
                return getPublicQuestionsUseCase.invoke(request)
            }

            override fun updatePage(
                request: RequestPublicQuestionsData,
                page: Int
            ): RequestPublicQuestionsData {
                return request.copy(page = page)
            }
        }

    private val pager = YeaHubPager(
        pagerLoader = loader,
        requestData = RequestPublicQuestionsData(
            page = 1,
            limit = 10,
            skillFilter = skillFilter,
            idSpecialization = idSpecialization,
            idCollection = idCollection
        ),
        data = { it.data },
        total = { it.total },
        limit = { it.limit }
    )
    private val state1 = pager.state
        .map { pagerState ->
            when (pagerState) {
                is YeaHubPagerState.Initial -> PublicQuestionsScreenState.Initial

                is YeaHubPagerState.Loading -> {
                    if (pagerState.items.isEmpty()) {
                        PublicQuestionsScreenState.Loading
                    } else {
                        PublicQuestionsScreenState.Loaded(
                            questions = mapper.mapQuestionModelListToUiModelList(
                                pagerState.items
                            ),
                            isEndReached = false,
                            isLoadingNextPage = true
                        )
                    }
                }

                is YeaHubPagerState.Loaded -> {
                    PublicQuestionsScreenState.Loaded(
                        questions = mapper.mapQuestionModelListToUiModelList(
                            pagerState.items
                        ),
                        isEndReached = pagerState.isEndReached,
                        isLoadingNextPage = false
                    )
                }

                is YeaHubPagerState.Error -> {
                    PublicQuestionsScreenState.Error(
                        questions = mapper.mapQuestionModelListToUiModelList(
                            pagerState.items
                        ),
                        throwable = pagerState.throwable
                    )
                }
            }
        }.distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIME_TO_CLEAN_UP_RESOURCES),
            initialValue = PublicQuestionsScreenState.Initial
        )
    val screenState: StateFlow<PublicQuestionsScreenState> = state1
    private val _commandState = MutableSharedFlow<PublicQuestionsScreenCommand>()
    val commandState: Flow<PublicQuestionsScreenCommand> = _commandState.asSharedFlow()

    fun onEvent(event: PublicQuestionsScreenEvent) {
        when (event) {
            is PublicQuestionsScreenEvent.LoadInitial -> loadInitial()
            is PublicQuestionsScreenEvent.LoadNextPage -> loadNextPage()
            is PublicQuestionsScreenEvent.Refresh -> refresh()
            is PublicQuestionsScreenEvent.OnMoreClick -> onMoreClick(
                event.questionIds,
                event.currentIndex
            )
            is PublicQuestionsScreenEvent.OnBackClick -> onBackClick()
        }
    }

    init {
        loadInitial()
    }

    private fun loadInitial() {
        viewModelScopeSafe.launch(Dispatchers.IO) {
            pager.load()
        }
    }

    private fun loadNextPage() {
        viewModelScopeSafe.launch(Dispatchers.IO) {
            pager.load()
        }
    }

    private fun refresh() {
        pager.reset()
        viewModelScopeSafe.launch(Dispatchers.IO) {
            pager.load()
        }
    }

    private fun onMoreClick(questionIds: List<String>, currentIndex: Int) {
        viewModelScopeSafe.launch(Dispatchers.IO) {
            _commandState.emit(PublicQuestionsScreenCommand.OnMoreClick(questionIds, currentIndex))
        }
    }

    private fun onBackClick() {
        viewModelScopeSafe.launch((Dispatchers.IO)) {
            _commandState.emit(PublicQuestionsScreenCommand.OnBackClick)
        }
    }
}

private const val TIME_TO_CLEAN_UP_RESOURCES = 500L