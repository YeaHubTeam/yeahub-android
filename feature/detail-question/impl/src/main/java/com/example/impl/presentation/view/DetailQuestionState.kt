package com.example.impl.presentation.view

import ru.yeahub.core_ui.component.GuruData

sealed class DetailQuestionState {
    data object LoadingState : DetailQuestionState()
    class Success(val data: PublicQuestion) : DetailQuestionState() {
        data class PublicQuestion(
            val id: Long,
            val title: String,
            val description: String,
            val code: String,
            val imageSrc: String,
            val longAnswer: String,
            val shortAnswer: String,
            val guruData: GuruData
        )
    }
    data object Initial : DetailQuestionState()
    class ErrorState(val message: String?) : DetailQuestionState()
}