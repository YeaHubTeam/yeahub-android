package ru.yeahub.profile_edit.impl.presentation.profile_edit_screen_mapper_tests

import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSocialPlatform
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
 * Проверяет, что validateMaxLength() применяется к каждой записи в socialLinks независимо:
 * одна ссылка в норме, одна превышает, обе превышают, пустой map.
 * Сверяет error для каждой платформы отдельно и итоговый hasValidationErrors.
 */
class ProfileEditScreenMapperSocialLinksValidationTest {

    private val mapper = ProfileEditScreenMapper()

    @ParameterizedTest
    @ArgumentsSource(ProfileEditScreenMapperSocialLinksValidationArgumentsProvider::class)
    fun `should validate social links max length`(
        testCase: ProfileEditScreenMapperSocialLinksValidationTestCase,
    ) {
        val input = ProfileEditMapperInput.Loaded(
            mutableState = ProfileEditMutableState(
                userInput = UserInput(
                    avatarUrl = null,
                    nickname = "validNick",
                    specialization = "",
                    location = "",
                    socialLinks = testCase.socialLinks,
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
        assertEquals(testCase.socialLinks.size, loaded.personalInfoState.socialLinks.size)
        testCase.socialLinks.forEach { (platform, url) ->
            val validatedField = loaded.personalInfoState.socialLinks[platform]
            assertEquals(url, validatedField?.value)
            assertEquals(testCase.expectedErrors[platform], validatedField?.error)
        }
        assertEquals(testCase.expectedHasValidationErrors, loaded.hasValidationErrors)
    }

    data class ProfileEditScreenMapperSocialLinksValidationTestCase(
        val socialLinks: Map<DomainProfileEditSocialPlatform, String>,
        val expectedErrors: Map<DomainProfileEditSocialPlatform, TextOrResource?>,
        val expectedHasValidationErrors: Boolean,
    )

    class ProfileEditScreenMapperSocialLinksValidationArgumentsProvider :
        TestArgumentsProvider<ProfileEditScreenMapperSocialLinksValidationTestCase>() {
        override fun testCases() = listOf(
            ProfileEditScreenMapperSocialLinksValidationTestCase(
                socialLinks = mapOf(
                    DomainProfileEditSocialPlatform.GitHub to "https://github.com/test",
                    DomainProfileEditSocialPlatform.Telegram to "https://t.me/test",
                ),
                expectedErrors = mapOf(
                    DomainProfileEditSocialPlatform.GitHub to null,
                    DomainProfileEditSocialPlatform.Telegram to null,
                ),
                expectedHasValidationErrors = false,
            ),
            ProfileEditScreenMapperSocialLinksValidationTestCase(
                socialLinks = mapOf(
                    DomainProfileEditSocialPlatform.GitHub to "a".repeat(255),
                ),
                expectedErrors = mapOf(
                    DomainProfileEditSocialPlatform.GitHub to null,
                ),
                expectedHasValidationErrors = false,
            ),
            ProfileEditScreenMapperSocialLinksValidationTestCase(
                socialLinks = mapOf(
                    DomainProfileEditSocialPlatform.GitHub to "a".repeat(256),
                ),
                expectedErrors = mapOf(
                    DomainProfileEditSocialPlatform.GitHub to TextOrResource.Resource(
                        R.string.error_max_length_255,
                    ),
                ),
                expectedHasValidationErrors = true,
            ),
            ProfileEditScreenMapperSocialLinksValidationTestCase(
                socialLinks = mapOf(
                    DomainProfileEditSocialPlatform.GitHub to "a".repeat(256),
                    DomainProfileEditSocialPlatform.Telegram to "a".repeat(300),
                ),
                expectedErrors = mapOf(
                    DomainProfileEditSocialPlatform.GitHub to TextOrResource.Resource(
                        R.string.error_max_length_255,
                    ),
                    DomainProfileEditSocialPlatform.Telegram to TextOrResource.Resource(
                        R.string.error_max_length_255,
                    ),
                ),
                expectedHasValidationErrors = true,
            ),
            ProfileEditScreenMapperSocialLinksValidationTestCase(
                socialLinks = emptyMap(),
                expectedErrors = emptyMap(),
                expectedHasValidationErrors = false,
            ),
        )
    }
}
