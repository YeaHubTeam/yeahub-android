package test

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.interview_trainer.impl.createQuiz.data.CreateQuizDataToDomainMapper
import ru.yeahub.interview_trainer.impl.createQuiz.domain.DomainSpecialization
import ru.yeahub.interview_trainer.impl.createQuiz.domain.DomainSpecializationListResponse
import ru.yeahub.network_api.models.GetSpecializationResponse
import ru.yeahub.network_api.models.GetSpecializationsResponse
import ru.yeahub.test.TestArgumentsProvider

class CreateQuizMapperTest {
    private val toDomainMapper = CreateQuizDataToDomainMapper()

    //CreateQuizDataToDomainMapper параметризированный тест
    class ArgumentsProvider :
        TestArgumentsProvider<SpecializationSelectionDataToDomainMapperTestCase>() {
        override fun testCases(): List<SpecializationSelectionDataToDomainMapperTestCase> = listOf(
            SpecializationSelectionDataToDomainMapperTestCase(
                dataToTest = SpecializationExampleDataClasses.defaultSpecialListResponse,
                expectedResult = SpecializationExampleDataClasses.defaultDomainSpecialListResponse
            )
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProvider::class)
    fun specializationSelectionDataToDomainMapperTestCase(
        testCase: SpecializationSelectionDataToDomainMapperTestCase,
    ) {
        val result = toDomainMapper.dataListToDomainList(testCase.dataToTest)
        Assertions.assertEquals(testCase.expectedResult, result)
    }

    object SpecializationExampleDataClasses {
        val defaultSpecialResponse = GetSpecializationResponse(
            id = 0L,
            title = "default title num 0",
            description = "default description",
            imageSrc = null,
            createdAt = "01.01.1970",
            updatedAt = "01.01.1970"
        )

        val defaultSpecialResponseWithImage = GetSpecializationResponse(
            id = 1L,
            title = "default title num 1",
            description = "default description",
            imageSrc = "some image url",
            createdAt = "01.01.1970",
            updatedAt = "01.01.1970"
        )

        val defaultDomainSpecial = DomainSpecialization(
            id = 0L,
            title = "default title num 0"
        )

        val defaultDomainSpecialWithImage = DomainSpecialization(
            id = 1L,
            title = "default title num 1"
        )

        val defaultSpecialListResponse = GetSpecializationsResponse(
            page = 1L,
            limit = 10L,
            data = listOf(defaultSpecialResponse, defaultSpecialResponseWithImage),
            total = 2L
        )

        val defaultDomainSpecialListResponse = DomainSpecializationListResponse(
            data = listOf(defaultDomainSpecial, defaultDomainSpecialWithImage),
            total = 2L
        )
    }

    data class SpecializationSelectionDataToDomainMapperTestCase(
        val dataToTest: GetSpecializationsResponse,
        val expectedResult: DomainSpecializationListResponse,
    )
}