package ru.yeahub.core_ui.example.dynamicPreview

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel

@Immutable
open class CountViewModel() : ViewModel() {

    val counterState = mutableIntStateOf(0)

    fun increment() {
        counterState.intValue++
    }

    fun decrement() {
        counterState.intValue--
    }
}