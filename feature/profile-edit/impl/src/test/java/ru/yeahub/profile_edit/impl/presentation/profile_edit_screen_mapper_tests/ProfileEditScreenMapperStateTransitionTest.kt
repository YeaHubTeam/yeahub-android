package ru.yeahub.profile_edit.impl.presentation.profile_edit_screen_mapper_tests

import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.profile_edit.impl.presentation.ProfileEditMapperInput
import ru.yeahub.profile_edit.impl.presentation.ProfileEditMutableState
import ru.yeahub.profile_edit.impl.presentation.ProfileEditScreenMapper
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState
import ru.yeahub.profile_edit.impl.presentation.StaticDomainData
import ru.yeahub.profile_edit.impl.presentation.UserInput
import ru.yeahub.profile_edit.impl.presentation.ViewModelStaticData
import ru.yeahub.test.TestArgumentsProvider
import java.io.IOException
import ru.yeahub.profile_edit.impl.R as ProfileEditR

private val skillKotlin = DomainProfileEditSkill(imageRes = 1, name = "Kotlin")
private val skillJava = DomainProfileEditSkill(imageRes = 2, name = "Java")
private val skillAndroid = DomainProfileEditSkill(imageRes = 3, name = "Android")

/**
 * Проверяет, что getScreenState() возвращает корректный тип состояния и его содержимое
 * для каждого из трёх возможных входных типов: Loading, Error, Loaded.
 * Для Loaded сверяет весь объект целиком — все поля PersonalInfoTabState, AboutMeTabState,
 * SkillsTabState, showUnsavedChangesDialog, hasValidationErrors, snackbarState.
 */
internal class ProfileEditScreenMapperStateTransitionTest {

    private val mapper = ProfileEditScreenMapper()

    @ParameterizedTest
    @ArgumentsSource(ProfileEditScreenMapperStateTransitionArgumentsProvider::class)
    fun `should return correct state type and content for each input type`(
        testCase: ProfileEditScreenMapperStateTransitionTestCase,
    ) {
        val input = testCase.input
        val result = mapper.getScreenState(input)
        assertEquals(testCase.expectedResult, result)
    }

    internal data class ProfileEditScreenMapperStateTransitionTestCase(
        val input: ProfileEditMapperInput,
        val expectedResult: ProfileEditState,
    )

    internal class ProfileEditScreenMapperStateTransitionArgumentsProvider :
        TestArgumentsProvider<ProfileEditScreenMapperStateTransitionTestCase>() {
        override fun testCases() = listOf(
            ProfileEditScreenMapperStateTransitionTestCase(
                input = ProfileEditMapperInput.Loading,
                expectedResult = ProfileEditState.Loading,
            ),
            ProfileEditScreenMapperStateTransitionTestCase(
                input = ProfileEditMapperInput.Error(IOException("no network")),
                expectedResult = ProfileEditState.Error(
                    message = TextOrResource.Resource(ProfileEditR.string.error_no_internet),
                ),
            ),
            ProfileEditScreenMapperStateTransitionTestCase(
                input = ProfileEditMapperInput.Loaded(
                    mutableState = ProfileEditMutableState(
                        userInput = UserInput(
                            avatarUrl = "https://example.com/avatar.jpg",
                            nickname = "validNick",
                            specialization = "Android",
                            location = "Moscow",
                            socialLinks = emptyMap(),
                            aboutMe = "About me text",
                            selectedSkills = persistentListOf(skillKotlin),
                        ),
                        throwable = null,
                        showUnsavedChangesDialog = false,
                    ),
                    staticData = ViewModelStaticData(
                        initialUserInput = UserInput(
                            avatarUrl = null,
                            nickname = "validNick",
                            specialization = "Android",
                            location = "",
                            socialLinks = emptyMap(),
                            aboutMe = "",
                            selectedSkills = persistentListOf(),
                        ),
                        staticData = StaticDomainData(
                            email = "user@example.com",
                            specializationList = persistentListOf("Android", "iOS", "Backend"),
                            isSpecializationEditable = true,
                            allSkills = persistentListOf(skillKotlin, skillJava, skillAndroid),
                        ),
                    ),
                ),
                expectedResult = ProfileEditState.Loaded(
                    personalInfoState = ProfileEditState.PersonalInfoTabState(
                        avatarUrl = "https://example.com/avatar.jpg",
                        nickname = ProfileEditState.ValidatedField("validNick", null),
                        specializationList = persistentListOf("Android", "iOS", "Backend"),
                        specialization = "Android",
                        isSpecializationEditable = true,
                        email = "user@example.com",
                        location = ProfileEditState.ValidatedField("Moscow", null),
                        socialLinks = persistentMapOf(),
                    ),
                    aboutMeTabState = ProfileEditState.AboutMeTabState("About me text"),
                    skillsTabState = ProfileEditState.SkillsTabState(
                        listOfSkills = persistentListOf(
                            skillJava,
                            skillAndroid,
                        ),
                        listOfChosenSkills = persistentListOf(
                            skillKotlin,
                        ),
                    ),
                    showUnsavedChangesDialog = false,
                    hasValidationErrors = false,
                    snackbarState = null,
                ),
            ),
        )
    }
}
