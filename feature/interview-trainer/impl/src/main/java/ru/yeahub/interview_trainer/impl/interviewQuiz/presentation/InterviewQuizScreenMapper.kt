package ru.yeahub.interview_trainer.impl.interviewQuiz.presentation

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.toPersistentList
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.interview_trainer.impl.R
import ru.yeahub.interview_trainer.impl.interviewQuiz.domain.DomainQuestion

class InterviewQuizScreenMapper {

    fun getScreenState(
        domainQuestions: List<DomainQuestion>,
        questionIndex: Int,
        isAnswerVisible: Boolean,
        answers: PersistentMap<Long, InterviewQuizState.Loaded.QuizAnswer>,
        selectedAnswer: InterviewQuizState.Loaded.QuizAnswer
    ): InterviewQuizState {
        val questions = domainQuestions.map {
            InterviewQuizState.Loaded.VoQuestion(it.id, it.title, it.shortAnswer)
        }.toPersistentList()

        val canGoNext = answers.containsKey(questions[questionIndex].id) &&
            questionIndex != questions.lastIndex

        val canGoPrev = questionIndex > 0

        val question = questions[questionIndex]

        val questionsCount = questions.size

        val isLastQuestion = questionIndex == questions.lastIndex

        return InterviewQuizState.Loaded(
            titleTopAppBar = TextOrResource.Resource(R.string.create_quiz_top_bar_header_text),
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

    fun getScreenState(e: Throwable): InterviewQuizState =
        InterviewQuizState.Error(e)
}