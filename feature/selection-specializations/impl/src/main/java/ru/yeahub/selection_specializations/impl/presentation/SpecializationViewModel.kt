package ru.yeahub.selection_specializations.impl.presentation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
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
import ru.yeahub.selection_specializations.impl.presentation.SpecializationSelectionDomainToVoMapper.toVoList
import ru.yeahub.selection_specializations.impl.presentation.SpecializationSelectionScreenCommand.OnBackClick
import ru.yeahub.selection_specializations.impl.presentation.SpecializationSelectionScreenCommand.SpecializationSelectionClick

private const val TIME_TO_CLEAN_UP_RESOURCES = 500L

class SpecializationViewModel(
    private val repository: SpecializationsRepository,
    private val parentRoute: String
) : BaseViewModel() {
    private val pagerLoader =
        object : YeaHubPagerLoader<DomainSpecilializationListResponse, SpecializationsRequest> {
            override suspend fun loadPage(
                request: SpecializationsRequest
            ): DomainSpecilializationListResponse =
                repository.getSpecializationsList(request)

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

    private val screenState: StateFlow<SpecializationScreenState> = pager
        .state
        .map { pagerState ->
            when (pagerState) {
                is YeaHubPagerState.Initial -> SpecializationScreenState.InitLoading
                is YeaHubPagerState.Loading<DomainSpecilialization> -> {
                    if (pagerState.items.isEmpty()) {
                        SpecializationScreenState.InitLoading
                    } else {
                        SpecializationScreenState.Loaded(
                            resultList = pagerState.items.toVoList(),
                            isEndReached = false,
                            isLoadingNextPage = true,
                        )
                    }
                }
                is YeaHubPagerState.Loaded<DomainSpecilialization> -> SpecializationScreenState.Loaded(
                    resultList = pagerState.items.toVoList(),
                    isEndReached = pagerState.isEndReached,
                    isLoadingNextPage = false,
                )
                is YeaHubPagerState.Error<DomainSpecilialization> -> SpecializationScreenState.Error(
                    throwable = pagerState.throwable,
                    currentList = pagerState.items.toVoList()
                )
            }
        }.distinctUntilChanged()
        .stateIn(
            scope = viewModelScopeSafe,
            started = SharingStarted.WhileSubscribed(TIME_TO_CLEAN_UP_RESOURCES),
            initialValue = SpecializationScreenState.InitLoading
        )

    private val _pagerState = pager.state
    val pagerState: StateFlow<YeaHubPagerState<DomainSpecilialization>>
        get() = _pagerState

    val uiStatus: StateFlow<SpecializationScreenState>
        get() = screenState

    private val _commands = MutableSharedFlow<SpecializationSelectionScreenCommand>()
    internal val commands: SharedFlow<SpecializationSelectionScreenCommand> = _commands.asSharedFlow()

    fun onEvent(event: SpecializationScreenEvent) {
        when (event) {
            is SpecializationScreenEvent.LoadInitial -> initLoad()
            is SpecializationScreenEvent.LoadNextPage -> loadNextPage()
            is SpecializationScreenEvent.OnBackClick -> backClick()
            is SpecializationScreenEvent.OnSpecialClick -> onSpecialClick(parentRoute, event.id)
            is SpecializationScreenEvent.Refresh -> refresh()
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
        parentRoute: String,
        id: Int
    ) = viewModelScopeSafe.launch(Dispatchers.IO) {
        _commands.emit(
            SpecializationSelectionClick(
                parentRoute = parentRoute,
                onClickedSpecId = id
            )
        )
    }

    private fun refresh() {
        pager.reset()
        viewModelScopeSafe.launch(Dispatchers.IO) { pager.load() }
    }
}