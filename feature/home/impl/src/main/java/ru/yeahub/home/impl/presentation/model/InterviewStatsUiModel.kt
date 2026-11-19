package ru.yeahub.home.impl.presentation.model

data class InterviewStatsUiModel(
    val chartPercent: Float,
    val total: Int,
    val new: Int,
    val inProcess: Int,
    val studied: Int
)
