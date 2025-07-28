package ru.yeahub.public_questions.impl.presentation.viewmodel

import androidx.lifecycle.ViewModel
import ru.yeahub.public_questions.impl.presentation.event.QuestionsScreenEvent

class QuestionsViewModel : ViewModel() {

    fun onEvent(event: QuestionsScreenEvent) {
        when (event) {
            is QuestionsScreenEvent.LoadInitial -> loadInitial()
            is QuestionsScreenEvent.LoadNextPage -> loadNextPage()
            is QuestionsScreenEvent.Refresh -> refresh()
            is QuestionsScreenEvent.OnMoreClick -> onMoreClick(event.id, event.title)
            is QuestionsScreenEvent.OnBackClick -> onBackClick()
        }
    }

    private fun loadInitial() {
        TODO()
    }

    private fun loadNextPage() {
        TODO()
    }

    private fun refresh() {
        TODO()
    }

    private fun onMoreClick(id: String, title: String) {
        TODO()
    }

    private fun onBackClick() {
        TODO()
    }
}
