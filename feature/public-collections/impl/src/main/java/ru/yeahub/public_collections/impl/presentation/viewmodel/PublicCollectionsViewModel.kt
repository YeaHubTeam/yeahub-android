package ru.yeahub.public_collections.impl.presentation.viewmodel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.yeahub.core_utils.BaseViewModel
import ru.yeahub.core_utils.common.TextOrResource
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

    val screenState: StateFlow<PublicCollectionsScreenState> =
        flow {
            emit(publicCollectionsScreenMapper.getScreenState())
        }.stateIn(
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
