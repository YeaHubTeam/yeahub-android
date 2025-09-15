package ru.yeahub.public_collections.impl.presentation.viewmodel

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
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.core_utils.pager.YeaHubPagerLoader
import ru.yeahub.core_utils.pagerImpl.YeaHubPager
import ru.yeahub.public_collections.impl.domain.entity.GetCollectionsResponseEntity
import ru.yeahub.public_collections.impl.domain.usecase.GetPublicCollectionsUseCase
import ru.yeahub.public_collections.impl.presentation.PublicCollectionsScreenState
import ru.yeahub.public_collections.impl.presentation.intents.PublicCollectionsScreenCommand
import ru.yeahub.public_collections.impl.presentation.intents.PublicCollectionsScreenEvent
import ru.yeahub.public_collections.impl.presentation.mapper.PublicCollectionsScreenMapper

private const val TIME_TO_CLEAN_UP_RESOURCES = 5000L

class PublicCollectionsViewModel(
    private val publicCollectionsScreenMapper: PublicCollectionsScreenMapper,
    private val getPublicCollectionsUseCase: GetPublicCollectionsUseCase,
    private val specializationsId: Long,
    private val header: String,
) : BaseViewModel() {

    private val loader =
        object : YeaHubPagerLoader<GetCollectionsResponseEntity, PublicCollectionsRequest> {
            override suspend fun loadPage(request: PublicCollectionsRequest): GetCollectionsResponseEntity {
                return getPublicCollectionsUseCase.invoke(request)
            }

            override fun updatePage(
                request: PublicCollectionsRequest,
                page: Int
            ): PublicCollectionsRequest {
                return request.copy(page = page)
            }
        }

    private val pager = YeaHubPager(
        pagerLoader = loader,
        requestData = PublicCollectionsRequest(
            page = 1,
            limit = 10,
            specializationsId = specializationsId,
        ),
        data = { it.data },
        total = { it.total.toLong() },
        limit = { it.limit?.toLong() }
    )

    val screenState: StateFlow<PublicCollectionsScreenState> = pager.state
        .map { publicCollectionsScreenMapper.getScreenState(it, header) }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScopeSafe,
            started = SharingStarted.WhileSubscribed(TIME_TO_CLEAN_UP_RESOURCES),
            initialValue = PublicCollectionsScreenState.Loading(header = TextOrResource.Text(""))
        )

    private val _commands = MutableSharedFlow<PublicCollectionsScreenCommand>()
    internal val commands: SharedFlow<PublicCollectionsScreenCommand> = _commands.asSharedFlow()

    fun onEvent(event: PublicCollectionsScreenEvent) {
        when (event) {
            PublicCollectionsScreenEvent.LoadInitial -> loadPage()
            PublicCollectionsScreenEvent.LoadNextPage -> loadPage()
            PublicCollectionsScreenEvent.OnBackClick -> onBackClick()
            is PublicCollectionsScreenEvent.OnQuestionsListClick -> OnQuestionsListClick(event.collectionId)
            PublicCollectionsScreenEvent.Refresh -> refresh()
        }
    }
    init {
        loadPage()
    }
    private fun loadPage() {
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

    private fun onBackClick() {
        viewModelScopeSafe.launch((Dispatchers.IO)) {
            _commands.emit(PublicCollectionsScreenCommand.OnBackClick)
        }
    }

    private fun OnQuestionsListClick(id: Int) {
        viewModelScopeSafe.launch(Dispatchers.IO) {
            _commands.emit(PublicCollectionsScreenCommand.OnQuestionsListClick(id))
        }
    }
}
