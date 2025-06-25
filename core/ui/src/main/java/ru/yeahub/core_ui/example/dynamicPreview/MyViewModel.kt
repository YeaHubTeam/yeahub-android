package ru.yeahub.core_ui.example.dynamicPreview

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel

open class MyViewModel() : ViewModel() {

    private var _textState = mutableIntStateOf(0)
    val textState: State<Int> = _textState

    fun increment(number: Int = 0) {
        _textState.intValue = _textState.intValue + number + 1
    }
}