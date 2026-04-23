package ru.yeahub.interview_trainer.impl.interviewQuizResult.presentation

import InterviewQuizResultState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.interview_trainer.impl.R
import ru.yeahub.interview_trainer.impl.interviewQuiz.presentation.QuizAnswerResult
import ru.yeahub.interview_trainer.impl.interviewQuiz.presentation.VoQuestionWithAnswer

class InterviewQuizResultScreenMapper {

    fun getScreenState(
        questions: List<VoQuestionWithAnswer>
    ): InterviewQuizResultState.Loaded {
        val total = questions.size
        val correct = questions.count { it.userAnswer == QuizAnswerResult.KNOWN }

        return InterviewQuizResultState.Loaded(
            titleTopAppBar = TextOrResource.Resource(R.string.create_quiz_top_bar_header_text),
            overallPercentage = if (total == 0) 0f else correct.toFloat() / total,
            totalQuestions = total,
            newQuestions = 0,
            inProgress = 0,
            studied = correct,
            skills = persistentListOf(),
            questions = questions.map { question: VoQuestionWithAnswer ->
                InterviewQuizResultState.Loaded.VoQuestionResult(
                    questionText = question.title,
                    isCorrect = question.userAnswer == QuizAnswerResult.KNOWN
                )
            }.toPersistentList(),
        )
    }
}