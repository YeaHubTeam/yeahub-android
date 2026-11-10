package ru.yeahub.home.impl.presentation.viewmodel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.yeahub.core_utils.BaseViewModel
import ru.yeahub.home.impl.domain.interview.GetInterviewDataUseCase
import ru.yeahub.home.impl.presentation.intents.InterviewPreparationEvent
import ru.yeahub.home.impl.presentation.screen.InterviewPreparationState

class InterviewPreparationViewModel(
    private val getInterviewData: GetInterviewDataUseCase
) : BaseViewModel() {

    private val _state =
        MutableStateFlow<InterviewPreparationState>(InterviewPreparationState.Initial)
    val state: StateFlow<InterviewPreparationState> = _state.asStateFlow()

    init {
        load()
    }

    fun onEvent(event: InterviewPreparationEvent) {
        when (event) {
            is InterviewPreparationEvent.Retry -> load()
        }
    }

    private fun load() {
        _state.value = InterviewPreparationState.Loading
        viewModelScopeSafe.launch(Dispatchers.IO) {
            runCatching { getInterviewData() }
                .onSuccess { interview ->
                    _state.value = InterviewPreparationState.Loaded(interview)
                }
                .onFailure { throwable ->
                    _state.value = InterviewPreparationState.Error(throwable)
                }
        }
    }
}
