package test

import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.interview_trainer.impl.interviewQuiz.presentation.QuizAnswerResult
import ru.yeahub.interview_trainer.impl.interviewQuiz.presentation.VoQuestionWithAnswer
import ru.yeahub.interview_trainer.impl.R
import ru.yeahub.interview_trainer.impl.interviewQuizResult.presentation.InterviewQuizResultScreenMapper
import ru.yeahub.test.TestArgumentsProvider

class InterviewQuizResultScreenMapperTest {

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProvider::class)
    fun getScreenStateTest(
        testCase: InterviewQuizResultScreenMapperTestCase
    ) {
        val result = InterviewQuizResultScreenMapper().getScreenState(
            questions = testCase.questions
        )

        assertEquals(testCase.expectedResult, result)
    }

    data class InterviewQuizResultScreenMapperTestCase(
        val questions: List<VoQuestionWithAnswer>,
        val expectedResult: InterviewQuizResultState.Loaded
    )

    class ArgumentsProvider :
        TestArgumentsProvider<InterviewQuizResultScreenMapperTestCase>() {

        override fun testCases(): List<InterviewQuizResultScreenMapperTestCase> = listOf(
            InterviewQuizResultScreenMapperTestCase(
                questions = listOf(
                    VoQuestionWithAnswer(
                        id = 1L,
                        title = "Q1",
                        shortAnswer = "A1",
                        userAnswer = QuizAnswerResult.KNOWN,

                    ),
                    VoQuestionWithAnswer(
                        id = 2L,
                        title = "Q2",
                        shortAnswer = "A2",
                        userAnswer = QuizAnswerResult.UNKNOWN
                    )
                ),
                expectedResult = InterviewQuizResultState.Loaded(
                    overallPercentage = 0.5f,
                    totalQuestions = 2,
                    titleTopAppBar = TextOrResource.Resource(R.string.create_quiz_top_bar_header_text),
                    newQuestions = 0,
                    inProgress = 0,
                    studied = 1,
                    skills = persistentListOf(),
                    questions = persistentListOf(
                        InterviewQuizResultState.Loaded.VoQuestionResult(
                            questionText = "Q1",
                            isCorrect = true
                        ),
                        InterviewQuizResultState.Loaded.VoQuestionResult(
                            questionText = "Q2",
                            isCorrect = false
                        )
                    ),

                )
            ),


            InterviewQuizResultScreenMapperTestCase(
                questions = listOf(
                    VoQuestionWithAnswer(
                        id = 1L,
                        title = "Q1",
                        shortAnswer = "A1",
                        userAnswer = QuizAnswerResult.KNOWN
                    ),
                    VoQuestionWithAnswer(
                        id = 2L,
                        title = "Q2",
                        shortAnswer = "A2",
                        userAnswer = QuizAnswerResult.KNOWN
                    )
                ),
                expectedResult = InterviewQuizResultState.Loaded(
                    overallPercentage = 1f,
                    totalQuestions = 2,
                    newQuestions = 0,
                    titleTopAppBar = TextOrResource.Resource(R.string.create_quiz_top_bar_header_text),
                    inProgress = 0,
                    studied = 2,
                    skills = persistentListOf(),
                    questions = persistentListOf(
                        InterviewQuizResultState.Loaded.VoQuestionResult(
                            questionText = "Q1",
                            isCorrect = true
                        ),
                        InterviewQuizResultState.Loaded.VoQuestionResult(
                            questionText = "Q2",
                            isCorrect = true
                        )
                    ),

                )
            ),


            InterviewQuizResultScreenMapperTestCase(
                questions = emptyList(),
                expectedResult = InterviewQuizResultState.Loaded(
                    overallPercentage = 0f,
                    totalQuestions = 0,
                    newQuestions = 0,
                    inProgress = 0,
                    studied = 0,
                    skills = persistentListOf(),
                    questions = persistentListOf(),
                    titleTopAppBar = TextOrResource.Resource(R.string.create_quiz_top_bar_header_text)
                )
            )
        )
    }
}