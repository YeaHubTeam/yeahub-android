package ru.yeahub.home.impl.presentation.model

data class InterviewHistoryUiModel(
    val interviewDate: String,
    val interviewNumber: String,
    val positiveCount: Int,
    val negativeCount: Int
)
