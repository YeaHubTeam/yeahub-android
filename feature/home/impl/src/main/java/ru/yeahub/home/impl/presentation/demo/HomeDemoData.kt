package ru.yeahub.home.impl.presentation.demo

import ru.yeahub.home.impl.presentation.model.CollectionUiModel
import ru.yeahub.home.impl.presentation.model.InterviewHistoryUiModel
import ru.yeahub.home.impl.presentation.model.InterviewStatsUiModel
import ru.yeahub.home.impl.presentation.model.InterviewTrainingUiModel
import ru.yeahub.home.impl.presentation.model.QuestionUiModel
import ru.yeahub.home.impl.presentation.state.HomeScreenState
import ru.yeahub.ui.R as CoreUiR

object HomeDemoData {
    private const val SAMPLE_QUESTION_COUNT = 3
    private const val SAMPLE_INTERVIEW_COUNT = 4

    val collectionData = CollectionUiModel(
        imageRes = CoreUiR.drawable.product_image,
        tags = listOf("Frontend", "Mobile", "Mobile"),
        title = "Что такое Virtual DOM, и как он работает? Митап True Tech Go",
        isForParticipants = true,
        questionsCount = 30,
        additionalTags = listOf("Software tester", "Fullstack")
    )

    val interviewHistory = List(SAMPLE_INTERVIEW_COUNT) { index ->
        InterviewHistoryUiModel(
            interviewDate = "15/04/2024",
            interviewNumber = (index + 1).toString(),
            positiveCount = 45,
            negativeCount = 15
        )
    }

    val interviewStats = InterviewStatsUiModel(
        chartPercent = 0.75f,
        total = 20,
        new = 50,
        inProcess = 120,
        studied = 12
    )

    val questionData = QuestionUiModel(
        question = "Что такое Virtual DOM, и как он работает?",
        rating = 4,
        complexity = 10
    )

    val content = HomeScreenState.Content(
        training = InterviewTrainingUiModel(
            questionText = questionData.question,
            currentQuestionNumber = 10,
            totalQuestions = 45
        ),
        interviewStats = interviewStats,
        questions = List(SAMPLE_QUESTION_COUNT) { questionData },
        interviewHistory = interviewHistory,
        collections = List(2) { collectionData }
    )
}
