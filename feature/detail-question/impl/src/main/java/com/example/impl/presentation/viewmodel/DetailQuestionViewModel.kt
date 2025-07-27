package com.example.impl.presentation.viewmodel

import com.example.impl.domain.usecase.GetQuestionByIdUseCase
import com.example.impl.presentation.intents.DetailQuestionCommand
import com.example.impl.presentation.intents.DetailQuestionEvent
import com.example.impl.presentation.mapper.DetailQuestionScreenMapper
import com.example.impl.presentation.state.DetailQuestionState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.yeahub.core_utils.BaseViewModel

class DetailQuestionViewModel(
    private val detailQuestionScreenMapper: DetailQuestionScreenMapper,
    private val getQuestionByIdUseCase: GetQuestionByIdUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<DetailQuestionState>(DetailQuestionState.Initial)
    val uiState: StateFlow<DetailQuestionState> get() = _uiState

    private val _commands = MutableSharedFlow<DetailQuestionCommand>()
    internal val commands: SharedFlow<DetailQuestionCommand> get() = _commands

    internal fun handleEvents(event: DetailQuestionEvent) {
        when (event) {
            is DetailQuestionEvent.LoadQuestion -> getQuestionById(event.id)
            DetailQuestionEvent.OnBackClick -> handleBackClick()
            DetailQuestionEvent.OnNextClick -> handleNextClick()
            DetailQuestionEvent.OnPrevClick -> handlePrevClick()
            is DetailQuestionEvent.OnTelegramClick -> handleTelegramClick(event.url)
            is DetailQuestionEvent.OnYoutubeClick -> handleYoutubeClick(event.url)
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun getQuestionById(id: Long) {
        viewModelScopeSafe.launch {
            _uiState.value = DetailQuestionState.LoadingState
            try {
                val question = getQuestionByIdUseCase(id)
                _uiState.value = detailQuestionScreenMapper.getScreenState(question)
            } catch (e: Exception) {
                _uiState.value = DetailQuestionState.ErrorState(e.message)
            }
        }
    }

    private fun handleBackClick() {
        viewModelScopeSafe.launch {
            _commands.emit(DetailQuestionCommand.NavigateBack)
        }
    }

    private fun handleNextClick() {
        viewModelScopeSafe.launch {
            _commands.emit(DetailQuestionCommand.NavigateNextPage)
        }
    }

    private fun handlePrevClick() {
        viewModelScopeSafe.launch {
            _commands.emit(DetailQuestionCommand.NavigatePrevPage)
        }
    }

    private fun handleTelegramClick(url: String) {
        viewModelScopeSafe.launch {
            _commands.emit(DetailQuestionCommand.OpenUrl(url))
        }
    }

    private fun handleYoutubeClick(url: String) {
        viewModelScopeSafe.launch {
            _commands.emit(DetailQuestionCommand.OpenUrl(url))
        }
    }
}