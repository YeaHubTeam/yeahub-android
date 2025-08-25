package ru.yeahub.public_collections.impl.presentation

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import ru.yeahub.core_utils.BaseViewModel

class PublicCollectionsViewModel(
    private val publicCollectionsScreenMapper: PublicCollectionsScreenMapper,
) : BaseViewModel() {

    val screenState: StateFlow<PublicCollectionsScreenState> =
        flow<PublicCollectionsScreenState> {
            publicCollectionsScreenMapper.getScreenState()
        }.stateIn(
            scope = viewModelScopeSafe,
            started = TODO(),
            initialValue = PublicCollectionsScreenState.Initial
        )
}