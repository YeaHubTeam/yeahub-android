package ru.yeahub.home.impl.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.yeahub.home.impl.presentation.demo.HomeDemoData
import ru.yeahub.home.impl.presentation.state.HomeScreenState

class HomeViewModel : ViewModel() {
    val state: StateFlow<HomeScreenState> = MutableStateFlow<HomeScreenState>(HomeDemoData.content).asStateFlow()
}
