package ru.yeahub.interview_trainer.impl.interviewQuiz.presentation

object InterviewQuizScreenMapper {

    fun getScreenState(
        questions: List<InterviewQuizState.Loaded.VoQuestion>,
        questionsCount: Int,
        currentQuestion: Int,
        isAnswerVisible: Boolean,
        answers: Map<Long, InterviewQuizState.Loaded.QuizAnswer>
    ): InterviewQuizState = InterviewQuizState.Loaded(
        questions = questions,
        questionsCount = questionsCount,
        currentQuestionIndex = currentQuestion,
        isAnswerVisible = isAnswerVisible,
        answers = answers
    )
}