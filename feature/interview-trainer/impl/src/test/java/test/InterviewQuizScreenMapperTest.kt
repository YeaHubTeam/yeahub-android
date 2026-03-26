package test

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.interview_trainer.impl.interviewQuiz.domain.DomainQuestion
import ru.yeahub.interview_trainer.impl.interviewQuiz.presentation.InterviewQuizScreenMapper
import ru.yeahub.interview_trainer.impl.interviewQuiz.presentation.InterviewQuizState
import ru.yeahub.test.TestArgumentsProvider

class InterviewQuizScreenMapperTest {

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProvider::class)
    fun getScreenStateTest(
        testCase: InterviewQuizScreenMapperTestCase
    ) {
        val result = InterviewQuizScreenMapper().getScreenState(
            domainQuestions = testCase.domainQuestions,
            questionIndex = testCase.questionIndex,
            isAnswerVisible = testCase.isAnswerVisible,
            answers = testCase.answers,
            selectedAnswer = testCase.selectedAnswer
        )
        assertEquals(testCase.expectedResult, result)
    }

    object ScreenMapperExampleDataClasses {

        val defaultDomainQuestions = listOf(
            DomainQuestion(
                id = 1L,
                title = "Question 1",
                shortAnswer = "Answer 1"
            ),
            DomainQuestion(
                id = 2L,
                title = "Question 2",
                shortAnswer = "Answer 2"
            ),
            DomainQuestion(
                id = 3L,
                title = "Question 3",
                shortAnswer = "Answer 3"
            )
        )

        val defaultVoQuestions = persistentListOf(
            InterviewQuizState.Loaded.VoQuestion(
                id = 1L,
                title = "Question 1",
                shortAnswer = "Answer 1"
            ),
            InterviewQuizState.Loaded.VoQuestion(
                id = 2L,
                title = "Question 2",
                shortAnswer = "Answer 2"
            ),
            InterviewQuizState.Loaded.VoQuestion(
                id = 3L,
                title = "Question 3",
                shortAnswer = "Answer 3"
            )
        )
    }

    data class InterviewQuizScreenMapperTestCase(
        val domainQuestions: List<DomainQuestion>,
        val questionIndex: Int,
        val isAnswerVisible: Boolean,
        val answers: PersistentMap<Long, InterviewQuizState.Loaded.QuizAnswer>,
        val selectedAnswer: InterviewQuizState.Loaded.QuizAnswer,
        val expectedResult: InterviewQuizState.Loaded
    )

    class ArgumentsProvider :
        TestArgumentsProvider<InterviewQuizScreenMapperTestCase>() {

        override fun testCases(): List<InterviewQuizScreenMapperTestCase> = listOf(
            InterviewQuizScreenMapperTestCase(
                domainQuestions = ScreenMapperExampleDataClasses.defaultDomainQuestions,
                questionIndex = 0,
                isAnswerVisible = false,
                answers = persistentMapOf(),
                selectedAnswer = InterviewQuizState.Loaded.QuizAnswer.NONE,
                expectedResult = InterviewQuizState.Loaded(
                    questions = ScreenMapperExampleDataClasses.defaultVoQuestions,
                    questionsCount = 3,
                    questionIndex = 0,
                    question = ScreenMapperExampleDataClasses.defaultVoQuestions[0],
                    isAnswerVisible = false,
                    answers = persistentMapOf(),
                    canGoPrev = false,
                    canGoNext = false,
                    selectedAnswer = InterviewQuizState.Loaded.QuizAnswer.NONE,
                    isLastQuestion = false
                )
            ),
            InterviewQuizScreenMapperTestCase(
                domainQuestions = ScreenMapperExampleDataClasses.defaultDomainQuestions,
                questionIndex = 0,
                isAnswerVisible = true,
                answers = persistentMapOf(
                    1L to InterviewQuizState.Loaded.QuizAnswer.KNOWN
                ),
                selectedAnswer = InterviewQuizState.Loaded.QuizAnswer.KNOWN,
                expectedResult = InterviewQuizState.Loaded(
                    questions = ScreenMapperExampleDataClasses.defaultVoQuestions,
                    questionsCount = 3,
                    questionIndex = 0,
                    question = ScreenMapperExampleDataClasses.defaultVoQuestions[0],
                    isAnswerVisible = true,
                    answers = persistentMapOf(
                        1L to InterviewQuizState.Loaded.QuizAnswer.KNOWN
                    ),
                    canGoPrev = false,
                    canGoNext = true,
                    selectedAnswer = InterviewQuizState.Loaded.QuizAnswer.KNOWN,
                    isLastQuestion = false
                )
            ),
            InterviewQuizScreenMapperTestCase(
                domainQuestions = ScreenMapperExampleDataClasses.defaultDomainQuestions,
                questionIndex = 1,
                isAnswerVisible = false,
                answers = persistentMapOf(
                    1L to InterviewQuizState.Loaded.QuizAnswer.KNOWN,
                    2L to InterviewQuizState.Loaded.QuizAnswer.UNKNOWN
                ),
                selectedAnswer = InterviewQuizState.Loaded.QuizAnswer.UNKNOWN,
                expectedResult = InterviewQuizState.Loaded(
                    questions = ScreenMapperExampleDataClasses.defaultVoQuestions,
                    questionsCount = 3,
                    questionIndex = 1,
                    question = ScreenMapperExampleDataClasses.defaultVoQuestions[1],
                    isAnswerVisible = false,
                    answers = persistentMapOf(
                        1L to InterviewQuizState.Loaded.QuizAnswer.KNOWN,
                        2L to InterviewQuizState.Loaded.QuizAnswer.UNKNOWN
                    ),
                    canGoPrev = true,
                    canGoNext = true,
                    selectedAnswer = InterviewQuizState.Loaded.QuizAnswer.UNKNOWN,
                    isLastQuestion = false
                )
            ),
            InterviewQuizScreenMapperTestCase(
                domainQuestions = ScreenMapperExampleDataClasses.defaultDomainQuestions,
                questionIndex = 2,
                isAnswerVisible = true,
                answers = persistentMapOf(
                    1L to InterviewQuizState.Loaded.QuizAnswer.KNOWN,
                    2L to InterviewQuizState.Loaded.QuizAnswer.UNKNOWN,
                    3L to InterviewQuizState.Loaded.QuizAnswer.KNOWN
                ),
                selectedAnswer = InterviewQuizState.Loaded.QuizAnswer.KNOWN,
                expectedResult = InterviewQuizState.Loaded(
                    questions = ScreenMapperExampleDataClasses.defaultVoQuestions,
                    questionsCount = 3,
                    questionIndex = 2,
                    question = ScreenMapperExampleDataClasses.defaultVoQuestions[2],
                    isAnswerVisible = true,
                    answers = persistentMapOf(
                        1L to InterviewQuizState.Loaded.QuizAnswer.KNOWN,
                        2L to InterviewQuizState.Loaded.QuizAnswer.UNKNOWN,
                        3L to InterviewQuizState.Loaded.QuizAnswer.KNOWN
                    ),
                    canGoPrev = true,
                    canGoNext = false,
                    selectedAnswer = InterviewQuizState.Loaded.QuizAnswer.KNOWN,
                    isLastQuestion = true
                )
            )
        )
    }
}