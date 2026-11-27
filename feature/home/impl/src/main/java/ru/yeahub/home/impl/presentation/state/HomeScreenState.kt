package ru.yeahub.home.impl.presentation.state

import ru.yeahub.home.impl.presentation.model.CollectionUiModel
import ru.yeahub.home.impl.presentation.model.InterviewHistoryUiModel
import ru.yeahub.home.impl.presentation.model.InterviewStatsUiModel
import ru.yeahub.home.impl.presentation.model.InterviewTrainingUiModel
import ru.yeahub.home.impl.presentation.model.QuestionUiModel

sealed class HomeScreenState {
    data class Content(
        val training: InterviewTrainingUiModel,
        val interviewStats: InterviewStatsUiModel,
        val questions: List<QuestionUiModel>,
        val interviewHistory: List<InterviewHistoryUiModel>,
        val collections: List<CollectionUiModel>
    ) : HomeScreenState()
}
