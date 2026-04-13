package ru.yeahub.profile_edit.impl.presentation.profile_edit_mapper_tests

import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.profile_edit.impl.presentation.ProfileEditMapperInput
import ru.yeahub.profile_edit.impl.presentation.ProfileEditMutableState
import ru.yeahub.profile_edit.impl.presentation.ProfileEditScreenMapper
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState
import ru.yeahub.profile_edit.impl.presentation.StaticDomainData
import ru.yeahub.profile_edit.impl.presentation.UserInput
import ru.yeahub.profile_edit.impl.presentation.ViewModelStaticData
import ru.yeahub.test.TestArgumentsProvider
import ru.yeahub.ui.R

/**
 * Проверяет граничные значения валидации никнейма:
 * MIN_NICKNAME_LENGTH=2 (0, 1 → ошибка; 2, 3 → ok)
 * MAX_NICKNAME_LENGTH=30 (29, 30 → ok; 31, 100 → ошибка).
 * Для каждого кейса сверяет значение поля, конкретный TextOrResource в error и hasValidationErrors.
 */
class ProfileEditScreenMapperNicknameValidationTest {

    private val mapper = ProfileEditScreenMapper()

    @ParameterizedTest
    @ArgumentsSource(ProfileEditScreenMapperNicknameValidationArgumentsProvider::class)
    fun `should validate nickname length boundaries`(
        testCase: ProfileEditScreenMapperNicknameValidationTestCase,
    ) {
        val input = ProfileEditMapperInput.Loaded(
            mutableState = ProfileEditMutableState(
                userInput = UserInput(
                    avatarUrl = null,
                    nickname = testCase.nickname,
                    specialization = "",
                    location = "",
                    socialLinks = emptyMap(),
                    aboutMe = "",
                    selectedSkills = persistentListOf(),
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
                    allSkills = persistentListOf(),
                ),
            ),
        )
        val result = mapper.getScreenState(input)
        val loaded = result as ProfileEditState.Loaded
        assertEquals(testCase.nickname, loaded.personalInfoState.nickname.value)
        assertEquals(testCase.expectedError, loaded.personalInfoState.nickname.error)
        assertEquals(testCase.expectedHasValidationErrors, loaded.hasValidationErrors)
    }

    data class ProfileEditScreenMapperNicknameValidationTestCase(
        val nickname: String,
        val expectedError: TextOrResource?,
        val expectedHasValidationErrors: Boolean,
    )

    class ProfileEditScreenMapperNicknameValidationArgumentsProvider :
        TestArgumentsProvider<ProfileEditScreenMapperNicknameValidationTestCase>() {
        override fun testCases() = listOf(
            ProfileEditScreenMapperNicknameValidationTestCase(
                nickname = "",
                expectedError = TextOrResource.Resource(R.string.error_minimal_length_2),
                expectedHasValidationErrors = true,
            ),
            ProfileEditScreenMapperNicknameValidationTestCase(
                nickname = "a",
                expectedError = TextOrResource.Resource(R.string.error_minimal_length_2),
                expectedHasValidationErrors = true,
            ),
            ProfileEditScreenMapperNicknameValidationTestCase(
                nickname = "ab",
                expectedError = null,
                expectedHasValidationErrors = false,
            ),
            ProfileEditScreenMapperNicknameValidationTestCase(
                nickname = "abc",
                expectedError = null,
                expectedHasValidationErrors = false,
            ),
            ProfileEditScreenMapperNicknameValidationTestCase(
                nickname = "a".repeat(29),
                expectedError = null,
                expectedHasValidationErrors = false,
            ),
            ProfileEditScreenMapperNicknameValidationTestCase(
                nickname = "a".repeat(30),
                expectedError = null,
                expectedHasValidationErrors = false,
            ),
            ProfileEditScreenMapperNicknameValidationTestCase(
                nickname = "a".repeat(31),
                expectedError = TextOrResource.Resource(R.string.error_max_length_30),
                expectedHasValidationErrors = true,
            ),
            ProfileEditScreenMapperNicknameValidationTestCase(
                nickname = "a".repeat(100),
                expectedError = TextOrResource.Resource(R.string.error_max_length_30),
                expectedHasValidationErrors = true,
            ),
        )
    }
}
