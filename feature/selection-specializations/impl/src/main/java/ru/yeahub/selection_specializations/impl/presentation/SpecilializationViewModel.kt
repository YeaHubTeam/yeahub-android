package ru.yeahub.selection_specializations.impl.presentation

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
import ru.yeahub.selection_specializations.impl.data.SpecializationsRepository
import ru.yeahub.selection_specializations.impl.model.DomainSpecilialization
import ru.yeahub.selection_specializations.impl.model.DomainSpecilializationListResponse
import ru.yeahub.selection_specializations.impl.model.SpecializationsRequest
import ru.yeahub.selection_specializations.impl.presentation.DomainToVoMapper.toVoList
import ru.yeahub.selection_specializations.impl.presentation.OnSpecialScreenCommand.OnBackClick
import ru.yeahub.selection_specializations.impl.presentation.OnSpecialScreenCommand.OnSpecialClick

private const val TIME_TO_CLEAN_UP_RESOURCES = 500L

class SpecilializationViewModel(
    private val repository: SpecializationsRepository
) : BaseViewModel() {
    private val pagerLoader =
        object : YeaHubPagerLoader<DomainSpecilializationListResponse, SpecializationsRequest> {
            override suspend fun loadPage(
                request: SpecializationsRequest
            ): DomainSpecilializationListResponse =
                repository.getSpecilizationsList(request)

            override fun updatePage(
                request: SpecializationsRequest,
                page: Int
            ): SpecializationsRequest =
                request.copy(page = page)
        }

    private val pager = YeaHubPager(
        pagerLoader = pagerLoader,
        requestData = SpecializationsRequest(page = 1, limit = 10),
        data = { domainResponse -> domainResponse.data },
        total = { domainResponse -> domainResponse.total },
        limit = { domainResponse -> domainResponse.limit },
    )

    private val screenState: StateFlow<SpecilializationScreenState> = pager
        .state
        .map { pagerState ->
            when (pagerState) {
                is YeaHubPagerState.Initial -> SpecilializationScreenState.InitLoading
                is YeaHubPagerState.Loading<DomainSpecilialization> -> {
                    if (pagerState.items.isEmpty()) {
                        SpecilializationScreenState.PagerLoading
                    } else {
                        SpecilializationScreenState.Loaded(
                            resultList = pagerState.items.toVoList(),
                            isEndReached = false,
                            isLoadingNextPage = true,
                        )
                    }
                }
                is YeaHubPagerState.Loaded<DomainSpecilialization> -> SpecilializationScreenState.Loaded(
                    resultList = pagerState.items.toVoList(),
                    isEndReached = pagerState.isEndReached,
                    isLoadingNextPage = false,
                )
                is YeaHubPagerState.Error<DomainSpecilialization> -> SpecilializationScreenState.Error(
                    throwable = pagerState.throwable,
                    currentList = pagerState.items.toVoList()
                )
            }
        }.distinctUntilChanged()
        .stateIn(
            scope = viewModelScopeSafe,
            started = SharingStarted.WhileSubscribed(TIME_TO_CLEAN_UP_RESOURCES),
            initialValue = SpecilializationScreenState.InitLoading
        )

    private val _pagerState = pager.state
    val pagerState: StateFlow<YeaHubPagerState<DomainSpecilialization>>
        get() = _pagerState

    val uiStatus: StateFlow<SpecilializationScreenState>
        get() = screenState

    private val _commands = MutableSharedFlow<OnSpecialScreenCommand>()
    internal val commands: Flow<OnSpecialScreenCommand> = _commands.asSharedFlow()

    fun onEvent(event: SpecilializationScreenEvent) {
        when (event) {
            is SpecilializationScreenEvent.LoadInitial -> initLoad()
            is SpecilializationScreenEvent.LoadNextPage -> loadNextPage()
            is SpecilializationScreenEvent.OnBackClick -> backClick()
            is SpecilializationScreenEvent.OnSpecialClick -> onSpecialClick(event.currentUseCase, event.id)
            is SpecilializationScreenEvent.Refresh -> refresh()
        }
    }

    private fun initLoad() =
        viewModelScopeSafe.launch(Dispatchers.IO) { pager.load() }

    private fun loadNextPage() =
        viewModelScopeSafe.launch(Dispatchers.IO) { pager.load() }

    private fun backClick() =
        viewModelScopeSafe.launch(Dispatchers.IO) {
            _commands.emit(OnBackClick)
        }

    private fun onSpecialClick(
        currentUseCase: OnSpecialFeatureUseCase,
        id: Int
    ) = viewModelScopeSafe.launch(Dispatchers.IO) {
        _commands.emit(
            OnSpecialClick(
                useCase = currentUseCase,
                onClickedSpecId = id
            )
        )
    }

    private fun refresh() {
        pager.reset()
        viewModelScopeSafe.launch(Dispatchers.IO) { pager.load() }
    }
}