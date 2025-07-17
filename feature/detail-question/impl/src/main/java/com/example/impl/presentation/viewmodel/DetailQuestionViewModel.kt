package com.example.impl.presentation.viewmodel

import com.example.impl.presentation.mapper.DomainToVOMapper
import com.example.impl.presentation.view.DetailQuestionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.yeahub.core_utils.BaseViewModel

class DetailQuestionViewModel() : BaseViewModel() {

    private val _uiState =  MutableStateFlow<DetailQuestionState>(DomainToVOMapper.toVO())
    val uiState: StateFlow<DetailQuestionState> get() = _uiState
}