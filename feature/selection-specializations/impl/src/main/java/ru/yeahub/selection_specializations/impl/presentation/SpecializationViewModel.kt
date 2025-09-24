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
import ru.yeahub.selection_specializations.impl.domain.DomainSpecilialization
import ru.yeahub.selection_specializations.impl.domain.DomainSpecilializationListResponse
import ru.yeahub.selection_specializations.impl.domain.GetSpecializationListUseCase
import ru.yeahub.selection_specializations.impl.domain.SpecializationsRequest
import ru.yeahub.selection_specializations.impl.presentation.SpecializationSelectionScreenCommand.OnBackClick
import ru.yeahub.selection_specializations.impl.presentation.SpecializationSelectionScreenCommand.SpecializationSelectionClick
import ru.yeahub.selection_specializations.impl.presentation.SpecializationSelectionScreenMapper.getScreenState
import timber.log.Timber

private const val TIME_TO_CLEAN_UP_RESOURCES = 500L

class SpecializationViewModel(
    private val getSpecializationListUseCase: GetSpecializationListUseCase,
) : BaseViewModel() {

    private val pagerLoader =
        object : YeaHubPagerLoader<DomainSpecilializationListResponse, SpecializationsRequest> {
            override suspend fun loadPage(
                request: SpecializationsRequest
            ): DomainSpecilializationListResponse =
                getSpecializationListUseCase(request)

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

    private val _screenState: StateFlow<SpecializationScreenState> = pager
        .state
        .map { pagerState ->
            Timber.tag("SpecViewModel").d("pagerState = $pagerState")
            when (pagerState) {
                is YeaHubPagerState.Initial -> SpecializationScreenState.InitLoading
                is YeaHubPagerState.Loading<DomainSpecilialization> -> {
                    if (pagerState.items.isEmpty()) {
                        SpecializationScreenState.InitLoading
                    } else {
                        SpecializationScreenState.Loaded(
                            resultList = getScreenState(pagerState.items),
                            isEndReached = false,
                            isLoadingNextPage = true,
                        )
                    }
                }
                is YeaHubPagerState.Loaded<DomainSpecilialization> -> SpecializationScreenState.Loaded(
                    resultList = getScreenState(pagerState.items),
                    isEndReached = pagerState.isEndReached,
                    isLoadingNextPage = false,
                )
                is YeaHubPagerState.Error<DomainSpecilialization> -> SpecializationScreenState.Error(
                    throwable = pagerState.throwable,
                    currentList = getScreenState(pagerState.items)
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

    val screenState: StateFlow<SpecializationScreenState>
        get() = _screenState

    private val _commands = MutableSharedFlow<SpecializationSelectionScreenCommand>()
    internal val commands: SharedFlow<SpecializationSelectionScreenCommand> = _commands.asSharedFlow()

    fun onEvent(event: SpecializationScreenEvent) {
        when (event) {
            is SpecializationScreenEvent.LoadInitial -> pagerLoad()
            is SpecializationScreenEvent.LoadNextPage -> pagerLoad()
            is SpecializationScreenEvent.OnBackClick -> backClick()
            is SpecializationScreenEvent.OnSpecialClick ->
                onSpecialClick(id = event.id, title = event.title)
            is SpecializationScreenEvent.Refresh -> refresh()
        }
    }

    //important order
    init {
        pagerLoad()
    }

    private fun pagerLoad() {
        viewModelScopeSafe.launch(Dispatchers.IO) { pager.load() }
    }

    private fun backClick() =
        viewModelScopeSafe.launch(Dispatchers.IO) {
            _commands.emit(OnBackClick)
        }

    private fun onSpecialClick(
        id: Long,
        title: String
    ) = viewModelScopeSafe.launch(Dispatchers.IO) {
        _commands.emit(
            SpecializationSelectionClick(
                onClickedSpecId = id,
                onClickedSpecTitle = title
            )
        )
    }

    private fun refresh() {
        pager.reset()
        viewModelScopeSafe.launch(Dispatchers.IO) { pager.load() }
    }
}