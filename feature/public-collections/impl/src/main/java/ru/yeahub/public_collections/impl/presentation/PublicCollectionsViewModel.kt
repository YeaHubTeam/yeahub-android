package ru.yeahub.public_collections.impl.presentation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import ru.yeahub.core_utils.BaseViewModel
import ru.yeahub.core_utils.common.TextOrResource

class PublicCollectionsViewModel(
    private val publicCollectionsScreenMapper: PublicCollectionsScreenMapper,
) : BaseViewModel() {

    val screenState: StateFlow<PublicCollectionsScreenState> =
        flow<PublicCollectionsScreenState> {
            publicCollectionsScreenMapper.getScreenState()
        }.stateIn(
            scope = viewModelScopeSafe,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PublicCollectionsScreenState.Loading(header = TextOrResource.Text(""))
        )

    private val _commands = MutableSharedFlow<PublicCollectionsScreenCommand>()
    internal val commands: SharedFlow<PublicCollectionsScreenCommand> = _commands.asSharedFlow()

    fun onEvent(event: PublicCollectionsScreenEvent) {
        when (event) {
            PublicCollectionsScreenEvent.TODO -> TODO()
        }
    }
}
