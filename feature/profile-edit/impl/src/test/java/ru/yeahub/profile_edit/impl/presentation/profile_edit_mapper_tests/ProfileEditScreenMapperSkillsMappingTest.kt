package ru.yeahub.profile_edit.impl.presentation.profile_edit_mapper_tests

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.profile_edit.impl.presentation.ProfileEditMapperInput
import ru.yeahub.profile_edit.impl.presentation.ProfileEditMutableState
import ru.yeahub.profile_edit.impl.presentation.ProfileEditScreenMapper
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState
import ru.yeahub.profile_edit.impl.presentation.StaticDomainData
import ru.yeahub.profile_edit.impl.presentation.UserInput
import ru.yeahub.profile_edit.impl.presentation.ViewModelStaticData
import ru.yeahub.test.TestArgumentsProvider

private val skillKotlin = DomainProfileEditSkill(imageRes = 1, name = "Kotlin")
private val skillJava = DomainProfileEditSkill(imageRes = 2, name = "Java")
private val skillAndroid = DomainProfileEditSkill(imageRes = 3, name = "Android")

/**
 * Проверяет, что mapSkillsState() корректно разделяет навыки:
 * listOfSkills = allSkills минус chosenSkills, listOfChosenSkills = chosenSkills.
 * Кейсы: все доступны, один выбран (первый/средний), все выбраны, оба списка пустые.
 */
class ProfileEditScreenMapperSkillsMappingTest {

    private val mapper = ProfileEditScreenMapper()

    @ParameterizedTest
    @ArgumentsSource(ProfileEditScreenMapperSkillsMappingArgumentsProvider::class)
    fun `should correctly partition skills between available and chosen`(
        testCase: ProfileEditScreenMapperSkillsMappingTestCase,
    ) {
        val input = ProfileEditMapperInput.Loaded(
            mutableState = ProfileEditMutableState(
                userInput = UserInput(
                    avatarUrl = null,
                    nickname = "validNick",
                    specialization = "",
                    location = "",
                    socialLinks = emptyMap(),
                    aboutMe = "",
                    selectedSkills = testCase.chosenSkills,
                ),
                throwable = null,
                showUnsavedChangesDialog = false,
            ),
            staticData = ViewModelStaticData(
                initialUserInput = UserInput(
                    avatarUrl = null,
                    nickname = "validNick",
                    specialization = "",
                    location = "",
                    socialLinks = emptyMap(),
                    aboutMe = "",
                    selectedSkills = persistentListOf(),
                ),
                staticData = StaticDomainData(
                    email = "",
                    specializationList = persistentListOf(),
                    isSpecializationEditable = false,
                    allSkills = testCase.allSkills,
                ),
            ),
        )
        val result = mapper.getScreenState(input)
        val loaded = result as ProfileEditState.Loaded
        assertEquals(testCase.expectedAvailableSkills, loaded.skillsTabState.listOfSkills)
        assertEquals(testCase.expectedChosenSkills, loaded.skillsTabState.listOfChosenSkills)
    }

    data class ProfileEditScreenMapperSkillsMappingTestCase(
        val allSkills: PersistentList<DomainProfileEditSkill>,
        val chosenSkills: PersistentList<DomainProfileEditSkill>,
        val expectedAvailableSkills: PersistentList<DomainProfileEditSkill>,
        val expectedChosenSkills: PersistentList<DomainProfileEditSkill>,
    )

    class ProfileEditScreenMapperSkillsMappingArgumentsProvider :
        TestArgumentsProvider<ProfileEditScreenMapperSkillsMappingTestCase>() {
        override fun testCases() = listOf(
            ProfileEditScreenMapperSkillsMappingTestCase(
                allSkills = persistentListOf(skillKotlin, skillJava, skillAndroid),
                chosenSkills = persistentListOf(),
                expectedAvailableSkills = persistentListOf(skillKotlin, skillJava, skillAndroid),
                expectedChosenSkills = persistentListOf(),
            ),
            ProfileEditScreenMapperSkillsMappingTestCase(
                allSkills = persistentListOf(skillKotlin, skillJava, skillAndroid),
                chosenSkills = persistentListOf(skillKotlin),
                expectedAvailableSkills = persistentListOf(skillJava, skillAndroid),
                expectedChosenSkills = persistentListOf(skillKotlin),
            ),
            ProfileEditScreenMapperSkillsMappingTestCase(
                allSkills = persistentListOf(skillKotlin, skillJava, skillAndroid),
                chosenSkills = persistentListOf(skillKotlin, skillJava, skillAndroid),
                expectedAvailableSkills = persistentListOf(),
                expectedChosenSkills = persistentListOf(skillKotlin, skillJava, skillAndroid),
            ),
            ProfileEditScreenMapperSkillsMappingTestCase(
                allSkills = persistentListOf(skillKotlin, skillJava, skillAndroid),
                chosenSkills = persistentListOf(skillJava),
                expectedAvailableSkills = persistentListOf(skillKotlin, skillAndroid),
                expectedChosenSkills = persistentListOf(skillJava),
            ),
            ProfileEditScreenMapperSkillsMappingTestCase(
                allSkills = persistentListOf(),
                chosenSkills = persistentListOf(),
                expectedAvailableSkills = persistentListOf(),
                expectedChosenSkills = persistentListOf(),
            ),
        )
    }
}
