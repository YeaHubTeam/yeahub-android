package test

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.interview_trainer.impl.createQuiz.data.CreateQuizDataToDomainMapper
import ru.yeahub.interview_trainer.impl.createQuiz.domain.DomainSpecialization
import ru.yeahub.interview_trainer.impl.createQuiz.domain.DomainSpecializationListResponse
import ru.yeahub.network_api.models.GetSpecializationResponse
import ru.yeahub.network_api.models.GetSpecializationsResponse
import ru.yeahub.test.TestArgumentsProvider
import test.SpecializationExampleDataClasses.defaultDomainSpecial
import test.SpecializationExampleDataClasses.defaultDomainSpecialListResponse
import test.SpecializationExampleDataClasses.defaultDomainSpecialWithImage
import test.SpecializationExampleDataClasses.defaultSpecialListResponse
import test.SpecializationExampleDataClasses.defaultSpecialResponse
import test.SpecializationExampleDataClasses.defaultSpecialResponseWithImage

class CreateQuizMapperTest {
    private val toDomainMapper = CreateQuizDataToDomainMapper()

    //CreateQuizDataToDomainMapper тесты

    @Test
    fun dataListToDomainList_shouldHandleEmptyList() {
        val emptyData = GetSpecializationsResponse(
            page = 1L,
            limit = 10L,
            data = emptyList(),
            total = 0L
        )
        val result = toDomainMapper.dataListToDomainList(emptyData)
        val expected = DomainSpecializationListResponse(
            page = 1L,
            limit = 10L,
            data = emptyList(),
            total = 0L
        )
        Assertions.assertEquals(expected, result)
    }

    @Test
    fun dataToDomain_shouldMapCorrectly() {
        val result = toDomainMapper.dataToDomain(defaultSpecialResponse)
        Assertions.assertEquals(defaultDomainSpecial, result)
    }

    //Параметризированные тесты

    class ArgumentsProvider1 :
        TestArgumentsProvider<SpecializationSelectionDataToDomainMapperTestCase1>() {
        override fun testCases(): List<SpecializationSelectionDataToDomainMapperTestCase1> = listOf(
            SpecializationSelectionDataToDomainMapperTestCase1(
                dataToTest = defaultSpecialResponse,
                expectedResult = defaultDomainSpecial
            ),
            SpecializationSelectionDataToDomainMapperTestCase1(
                dataToTest = defaultSpecialResponseWithImage,
                expectedResult = defaultDomainSpecialWithImage
            )
        )
    }

    class ArgumentsProvider2 :
        TestArgumentsProvider<SpecializationSelectionDataToDomainMapperTestCase2>() {
        override fun testCases(): List<SpecializationSelectionDataToDomainMapperTestCase2> = listOf(
            SpecializationSelectionDataToDomainMapperTestCase2(
                dataToTest = defaultSpecialListResponse,
                expectedResult = defaultDomainSpecialListResponse
            )
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProvider1::class)
    fun specializationSelectionDataToDomainMapperTestCase1(
        testCase1: SpecializationSelectionDataToDomainMapperTestCase1,
    ) {
        val result = toDomainMapper.dataToDomain(testCase1.dataToTest)
        Assertions.assertEquals(testCase1.expectedResult, result)
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProvider2::class)
    fun specializationSelectionDataToDomainMapperTestCase2(
        testCase2: SpecializationSelectionDataToDomainMapperTestCase2,
    ) {
        val result = toDomainMapper.dataListToDomainList(testCase2.dataToTest)
        Assertions.assertEquals(testCase2.expectedResult, result)
    }
}

data class SpecializationSelectionDataToDomainMapperTestCase1(
    val dataToTest: GetSpecializationResponse,
    val expectedResult: DomainSpecialization,
)

data class SpecializationSelectionDataToDomainMapperTestCase2(
    val dataToTest: GetSpecializationsResponse,
    val expectedResult: DomainSpecializationListResponse,
)
