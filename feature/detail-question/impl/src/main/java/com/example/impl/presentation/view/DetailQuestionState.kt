package com.example.impl.presentation.view

import com.example.impl.presentation.viewmodel.PublicQuestion

sealed class DetailQuestionState {
    data object LoadingState : DetailQuestionState()
    class LoadedState(val data: PublicQuestion): DetailQuestionState()
    data object Initial : DetailQuestionState()
    class ErrorState(val message: String?): DetailQuestionState()
}