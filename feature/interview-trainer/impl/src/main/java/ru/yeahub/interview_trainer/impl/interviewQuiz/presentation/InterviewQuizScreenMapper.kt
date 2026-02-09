package ru.yeahub.interview_trainer.impl.interviewQuiz.presentation

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap

class InterviewQuizScreenMapper {

    fun getScreenState(
        questions: PersistentList<InterviewQuizState.Loaded.VoQuestion>,
        questionIndex: Int,
        isAnswerVisible: Boolean,
        answers: PersistentMap<Long, InterviewQuizState.Loaded.QuizAnswer>,
        selectedAnswer: InterviewQuizState.Loaded.QuizAnswer
    ): InterviewQuizState {
        val canGoNext = answers.containsKey(questions[questionIndex].id) &&
            questionIndex != questions.lastIndex

        val canGoPrev = questionIndex > 0

        val question = questions[questionIndex]

        val questionsCount = questions.size

        val isLastQuestion = questionIndex != questions.lastIndex

        return InterviewQuizState.Loaded(
            questions = questions,
            questionsCount = questionsCount,
            questionIndex = questionIndex,
            question = question,
            isAnswerVisible = isAnswerVisible,
            answers = answers,
            canGoNext = canGoNext,
            canGoPrev = canGoPrev,
            selectedAnswer = selectedAnswer,
            isLastQuestion = isLastQuestion
        )
    }
}