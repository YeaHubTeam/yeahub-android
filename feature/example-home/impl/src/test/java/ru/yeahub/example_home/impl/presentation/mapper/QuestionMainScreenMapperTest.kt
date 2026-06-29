package ru.yeahub.example_home.impl.presentation.mapper

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.example_home.impl.presentation.model.QuestionMainItemType
import java.util.stream.Stream

class QuestionMainScreenMapperTest {

    private val mapper = QuestionMainScreenMapper()

    @ParameterizedTest
    @ArgumentsSource(InterviewTrainerVisibilityArgumentsProvider::class)
    fun `interview trainer card visibility follows feature toggle`(
        testCase: InterviewTrainerVisibilityTestCase
    ) {
        val itemTypes = mapper.getInitialUiModels(
            isInterviewTrainerEnabled = testCase.isInterviewTrainerEnabled
        ).map { it.type }

        Assertions.assertEquals(
            testCase.expectedContainsInterviewTrainer,
            itemTypes.contains(QuestionMainItemType.InterviewTrainer)
        )
        Assertions.assertTrue(itemTypes.contains(QuestionMainItemType.BaseQuestions))
        Assertions.assertTrue(itemTypes.contains(QuestionMainItemType.Collections))
    }

    data class InterviewTrainerVisibilityTestCase(
        val isInterviewTrainerEnabled: Boolean,
        val expectedContainsInterviewTrainer: Boolean
    )

    class InterviewTrainerVisibilityArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(
                    InterviewTrainerVisibilityTestCase(
                        isInterviewTrainerEnabled = true,
                        expectedContainsInterviewTrainer = true
                    )
                ),
                Arguments.of(
                    InterviewTrainerVisibilityTestCase(
                        isInterviewTrainerEnabled = false,
                        expectedContainsInterviewTrainer = false
                    )
                )
            )
        }
    }
}
