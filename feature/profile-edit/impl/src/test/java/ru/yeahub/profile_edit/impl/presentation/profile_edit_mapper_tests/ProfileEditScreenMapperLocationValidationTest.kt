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
 * Проверяет граничные значения валидации location через validateMaxLength():
 * MAX_FIELD_LENGTH=255 (0, 254, 255 → ok; 256, 500 → ошибка).
 * Location не имеет нижней границы — пустая строка валидна.
 */
class ProfileEditScreenMapperLocationValidationTest {

    private val mapper = ProfileEditScreenMapper()

    @ParameterizedTest
    @ArgumentsSource(ProfileEditScreenMapperLocationValidationArgumentsProvider::class)
    fun `should validate location max length`(
        testCase: ProfileEditScreenMapperLocationValidationTestCase,
    ) {
        val input = ProfileEditMapperInput.Loaded(
            mutableState = ProfileEditMutableState(
                userInput = UserInput(
                    avatarUrl = null,
                    nickname = "validNick",
                    specialization = "",
                    location = testCase.location,
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
        assertEquals(testCase.location, loaded.personalInfoState.location.value)
        assertEquals(testCase.expectedError, loaded.personalInfoState.location.error)
        assertEquals(testCase.expectedHasValidationErrors, loaded.hasValidationErrors)
    }

    data class ProfileEditScreenMapperLocationValidationTestCase(
        val location: String,
        val expectedError: TextOrResource?,
        val expectedHasValidationErrors: Boolean,
    )

    class ProfileEditScreenMapperLocationValidationArgumentsProvider :
        TestArgumentsProvider<ProfileEditScreenMapperLocationValidationTestCase>() {
        override fun testCases() = listOf(
            ProfileEditScreenMapperLocationValidationTestCase(
                location = "",
                expectedError = null,
                expectedHasValidationErrors = false,
            ),
            ProfileEditScreenMapperLocationValidationTestCase(
                location = "a".repeat(254),
                expectedError = null,
                expectedHasValidationErrors = false,
            ),
            ProfileEditScreenMapperLocationValidationTestCase(
                location = "a".repeat(255),
                expectedError = null,
                expectedHasValidationErrors = false,
            ),
            ProfileEditScreenMapperLocationValidationTestCase(
                location = "a".repeat(256),
                expectedError = TextOrResource.Resource(R.string.error_max_length_255),
                expectedHasValidationErrors = true,
            ),
            ProfileEditScreenMapperLocationValidationTestCase(
                location = "a".repeat(500),
                expectedError = TextOrResource.Resource(R.string.error_max_length_255),
                expectedHasValidationErrors = true,
            ),
        )
    }
}
