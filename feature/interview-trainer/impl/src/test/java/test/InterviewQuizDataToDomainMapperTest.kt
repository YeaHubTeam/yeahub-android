package test

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.interview_trainer.impl.interviewQuiz.data.InterviewQuizDataToDomainMapper
import ru.yeahub.interview_trainer.impl.interviewQuiz.domain.DomainQuestion
import ru.yeahub.interview_trainer.impl.interviewQuiz.domain.DomainQuestionsListResponse
import ru.yeahub.network_api.models.GetNewMockQuizResponse
import ru.yeahub.network_api.models.GetQuestionResponse
import ru.yeahub.network_api.models.NestedUserReferenceDto
import ru.yeahub.network_api.models.QuizAnswersWrapperDto
import ru.yeahub.test.TestArgumentsProvider

class InterviewQuizDataToDomainMapperTest {

    private val dataToDomainMapper = InterviewQuizDataToDomainMapper()

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProvider::class)
    fun dataToDomainMappingTestCase(
        testCase: DataToDomainMappingTestCase
    ) {
        val result = dataToDomainMapper.mapDataListToDomainList(testCase.dataToTest)
        assertEquals(testCase.expectedResult, result)
    }

    object QuestionExamplesDataClasses {

        private val defaultQuestionResponse = GetQuestionResponse(
            id = 1,
            title = "default question 0",
            description = "default description",
            code = "0101",
            imageSrc = null,
            keywords = null,
            longAnswer = null,
            shortAnswer = "default short answer",
            status = null,
            rate = null,
            complexity = null,
            createdById = "07.03.2026",
            updatedById = null,
            questionSpecializations = emptyList(),
            questionSkills = emptyList(),
            createdAt = "07.03.2026",
            updatedAt = "07.03.2026",
            createdBy = NestedUserReferenceDto("userId", "username"),
            updatedBy = null
        )

        private val defaultDomainQuestion = DomainQuestion(
            id = 1,
            title = "default question 0",
            shortAnswer = "default short answer"
        )

        private val defaultQuestionResponseWithLongAnswer = GetQuestionResponse(
            id = 2,
            title = "default question with long answer 1",
            description = "default description",
            code = "0102",
            imageSrc = null,
            keywords = null,
            longAnswer = "default long answer",
            shortAnswer = "default short answer",
            status = null,
            rate = null,
            complexity = null,
            createdById = "07.03.2026",
            updatedById = null,
            questionSpecializations = emptyList(),
            questionSkills = emptyList(),
            createdAt = "07.03.2026",
            updatedAt = "07.03.2026",
            createdBy = NestedUserReferenceDto("userId", "username"),
            updatedBy = null
        )

        private val defaultDomainQuestionWithLongAnswer = DomainQuestion(
            id = 2,
            title = "default question with long answer 1",
            shortAnswer = "default short answer"
        )

        val defaultNewMockQuizResponse = GetNewMockQuizResponse(
            id = "0101010",
            startDate = "01.01.2026",
            fullCount = 2,
            skills = emptyList(),
            response = QuizAnswersWrapperDto(emptyList()),
            questions = listOf(defaultQuestionResponse, defaultQuestionResponseWithLongAnswer),
        )

        val defaultDomainListResponse = DomainQuestionsListResponse(
            fullCount = 2,
            questions = listOf(defaultDomainQuestion, defaultDomainQuestionWithLongAnswer)
        )
    }

    data class DataToDomainMappingTestCase(
        val dataToTest: GetNewMockQuizResponse,
        val expectedResult:  DomainQuestionsListResponse
    )

    class ArgumentsProvider:
        TestArgumentsProvider<DataToDomainMappingTestCase>() {

        override fun testCases(): List<DataToDomainMappingTestCase> {
            return listOf(
                DataToDomainMappingTestCase(
                    dataToTest = QuestionExamplesDataClasses.defaultNewMockQuizResponse,
                    expectedResult = QuestionExamplesDataClasses.defaultDomainListResponse
                )
            )
        }
    }
}