package test

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.interview_trainer.impl.createQuiz.domain.DomainSpecialization
import ru.yeahub.interview_trainer.impl.createQuiz.presentation.CreateQuizScreenMapper
import ru.yeahub.interview_trainer.impl.createQuiz.presentation.CreateQuizState
import ru.yeahub.test.TestArgumentsProvider

class CreateQuizScreenMapperTest {

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProvider::class)
    fun getScreenStateTest(
        testCase: CreateQuizScreenMapperTestCase,
    ) {
        val result = CreateQuizScreenMapper.getScreenState(
            specializations = testCase.specializations,
            selectedSpecializationId = testCase.selectedSpecializationId,
            questionsCount = testCase.questionsCount
        )
        Assertions.assertEquals(testCase.expectedResult, result)
    }

    object ScreenMapperExampleDataClasses {
        val defaultDomainSpecial = DomainSpecialization(
            id = 0L,
            title = "default title num 0"
        )

        val defaultDomainSpecialWithImage = DomainSpecialization(
            id = 1L,
            title = "default title num 1"
        )

        val defaultDomainSpecialList = listOf(
            defaultDomainSpecial,
            defaultDomainSpecialWithImage
        )

        val defaultVoSpecial = CreateQuizState.Loaded.VoSpecialization(
            id = 0L,
            title = "default title num 0"
        )

        val defaultVoSpecialWithImage = CreateQuizState.Loaded.VoSpecialization(
            id = 1L,
            title = "default title num 1"
        )

        val defaultLoadedState = CreateQuizState.Loaded(
            specializations = listOf(defaultVoSpecial, defaultVoSpecialWithImage),
            selectedSpecializationId = 0L,
            questionsCount = 10
        )

        val loadedStateWithDifferentSelection = CreateQuizState.Loaded(
            specializations = listOf(defaultVoSpecial, defaultVoSpecialWithImage),
            selectedSpecializationId = 1L,
            questionsCount = 5
        )
    }

    data class CreateQuizScreenMapperTestCase(
        val specializations: List<DomainSpecialization>,
        val selectedSpecializationId: Long,
        val questionsCount: Int,
        val expectedResult: CreateQuizState.Loaded,
    )

    class ArgumentsProvider :
        TestArgumentsProvider<CreateQuizScreenMapperTestCase>() {
        override fun testCases(): List<CreateQuizScreenMapperTestCase> = listOf(
            CreateQuizScreenMapperTestCase(
                specializations = ScreenMapperExampleDataClasses.defaultDomainSpecialList,
                selectedSpecializationId = 0L,
                questionsCount = 10,
                expectedResult = ScreenMapperExampleDataClasses.defaultLoadedState
            ),
            CreateQuizScreenMapperTestCase(
                specializations = ScreenMapperExampleDataClasses.defaultDomainSpecialList,
                selectedSpecializationId = 1L,
                questionsCount = 5,
                expectedResult = ScreenMapperExampleDataClasses.loadedStateWithDifferentSelection
            )
        )
    }
}