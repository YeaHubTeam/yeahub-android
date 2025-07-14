package ru.yeahub.core_ui.component.textInput

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

open class SuggestionsViewModel() : ViewModel() {

    private val _text = mutableStateOf("")
    val text: State<String> = _text

    private val _suggestions = mutableStateOf(emptyList<String>())
    val suggestions: State<List<String>> = _suggestions

    fun onTextChanged(newTextPreview: String) {
        _text.value = newTextPreview
    }

    fun onQueryChange(newTextPreview: String, suggestionsPreview: List<String>) {
        _suggestions.value =  suggestionsPreview.filter { it.startsWith(newTextPreview) }
    }
}