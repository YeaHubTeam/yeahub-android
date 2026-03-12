package test

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.network_api.models.GetSpecializationResponse
import ru.yeahub.network_api.models.GetSpecializationsResponse
import ru.yeahub.selection_specializations.impl.data.SpecializationSelectionDataToDomainMapper
import ru.yeahub.selection_specializations.impl.domain.DomainSpecilialization
import ru.yeahub.selection_specializations.impl.domain.DomainSpecilializationListResponse
import ru.yeahub.selection_specializations.impl.presentation.SpecializationSelectionScreenMapper.getScreenState
import ru.yeahub.selection_specializations.impl.presentation.VoSpecilialization
import ru.yeahub.test.TestArgumentsProvider
import test.SpecializationExampleDataClasses.defaultDomainSpecial
import test.SpecializationExampleDataClasses.defaultDomainSpecialListResponse
import test.SpecializationExampleDataClasses.defaultDomainSpecialWithImage
import test.SpecializationExampleDataClasses.defaultSpecialListResponse
import test.SpecializationExampleDataClasses.defaultSpecialResponse
import test.SpecializationExampleDataClasses.defaultSpecialResponseWithImage
import test.SpecializationExampleDataClasses.defaultVoSpecialization
import test.SpecializationExampleDataClasses.defaultVoSpecializationWithImage

class SpecializationSelectionMapperTest(
    private val toDomainMapper: SpecializationSelectionDataToDomainMapper
) {

    class ArgumentsProvider1 : TestArgumentsProvider<SpecializationSelectionDataToDomainMapperTestCase1>() {
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

    class ArgumentsProvider2 : TestArgumentsProvider<SpecializationSelectionDataToDomainMapperTestCase2>() {
        override fun testCases(): List<SpecializationSelectionDataToDomainMapperTestCase2> = listOf(
            SpecializationSelectionDataToDomainMapperTestCase2(
                dataToTest = defaultSpecialListResponse,
                expectedResult = defaultDomainSpecialListResponse
            )
        )
    }

    class ArgumentsProvider3 : TestArgumentsProvider<SpecializationSelectionDomainToVoMapperTestCase>() {
        override fun testCases(): List<SpecializationSelectionDomainToVoMapperTestCase> = listOf(
            SpecializationSelectionDomainToVoMapperTestCase(
                dataToTest = listOf(defaultDomainSpecial, defaultDomainSpecialWithImage),
                expectedResult = listOf(defaultVoSpecialization, defaultVoSpecializationWithImage)
            )
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProvider1::class)
    fun specializationSelectionDataToDomainMapperTestCase1(
        testCase1: SpecializationSelectionDataToDomainMapperTestCase1
    ) {
        val result = toDomainMapper.dataToDomain(testCase1.dataToTest)
        assertEquals(testCase1.expectedResult, result)
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProvider2::class)
    fun specializationSelectionDataToDomainMapperTestCase2(
        testCase2: SpecializationSelectionDataToDomainMapperTestCase2
    ) {
        val result = toDomainMapper.dataListToDomainList(testCase2.dataToTest)
        assertEquals(testCase2.expectedResult, result)
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProvider3::class)
    fun specializationSelectionDomainToVoMapperTestCase(
        testCase: SpecializationSelectionDomainToVoMapperTestCase
    ) {
        val result = getScreenState(testCase.dataToTest)
        assertEquals(testCase.expectedResult, result)
    }
}

data class SpecializationSelectionDataToDomainMapperTestCase1(
    val dataToTest: GetSpecializationResponse,
    val expectedResult: DomainSpecilialization
)

data class SpecializationSelectionDataToDomainMapperTestCase2(
    val dataToTest: GetSpecializationsResponse,
    val expectedResult: DomainSpecilializationListResponse
)

data class SpecializationSelectionDomainToVoMapperTestCase(
    val dataToTest: List<DomainSpecilialization>,
    val expectedResult: List<VoSpecilialization>
)
