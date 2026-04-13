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
import java.io.IOException
import ru.yeahub.profile_edit.impl.R as ProfileEditR

/**
 * Проверяет два независимых поведения при маппинге Loaded:
 * 1. showUnsavedChangesDialog передаётся в состояние без изменений (true/false).
 * 2. snackbarState — null при throwable=null, содержит корректный message и throwableMessage иначе.
 * Кейсы с throwable!=null дополнительно проверяют, что диалог и snackbar независимы друг от друга.
 */
class ProfileEditScreenMapperSnackbarAndDialogTest {

    private val mapper = ProfileEditScreenMapper()

    @ParameterizedTest
    @ArgumentsSource(ProfileEditScreenMapperSnackbarAndDialogArgumentsProvider::class)
    fun `should pass through dialog flag and map snackbar state`(
        testCase: ProfileEditScreenMapperSnackbarAndDialogTestCase,
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
                    selectedSkills = persistentListOf(),
                ),
                throwable = testCase.throwable,
                showUnsavedChangesDialog = testCase.showUnsavedChangesDialog,
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
        assertEquals(testCase.showUnsavedChangesDialog, loaded.showUnsavedChangesDialog)
        if (testCase.expectedSnackbarMessage == null) {
            assertEquals(null, loaded.snackbarState)
        } else {
            val snackbar = loaded.snackbarState!!
            assertEquals(testCase.expectedSnackbarMessage, snackbar.message)
            assertEquals(testCase.expectedThrowableMessage, snackbar.throwableMessage)
        }
    }

    data class ProfileEditScreenMapperSnackbarAndDialogTestCase(
        val throwable: Throwable?,
        val showUnsavedChangesDialog: Boolean,
        val expectedSnackbarMessage: TextOrResource?,
        val expectedThrowableMessage: String?,
    )

    class ProfileEditScreenMapperSnackbarAndDialogArgumentsProvider :
        TestArgumentsProvider<ProfileEditScreenMapperSnackbarAndDialogTestCase>() {
        override fun testCases() = listOf(
            ProfileEditScreenMapperSnackbarAndDialogTestCase(
                throwable = null,
                showUnsavedChangesDialog = false,
                expectedSnackbarMessage = null,
                expectedThrowableMessage = null,
            ),
            ProfileEditScreenMapperSnackbarAndDialogTestCase(
                throwable = null,
                showUnsavedChangesDialog = true,
                expectedSnackbarMessage = null,
                expectedThrowableMessage = null,
            ),
            ProfileEditScreenMapperSnackbarAndDialogTestCase(
                throwable = IOException("net err"),
                showUnsavedChangesDialog = false,
                expectedSnackbarMessage = TextOrResource.Resource(
                    ProfileEditR.string.error_no_internet,
                ),
                expectedThrowableMessage = "net err",
            ),
            ProfileEditScreenMapperSnackbarAndDialogTestCase(
                throwable = IOException("net err"),
                showUnsavedChangesDialog = true,
                expectedSnackbarMessage = TextOrResource.Resource(
                    ProfileEditR.string.error_no_internet,
                ),
                expectedThrowableMessage = "net err",
            ),
            ProfileEditScreenMapperSnackbarAndDialogTestCase(
                throwable = RuntimeException("oops"),
                showUnsavedChangesDialog = false,
                expectedSnackbarMessage = TextOrResource.Resource(R.string.error_screen_text),
                expectedThrowableMessage = "oops",
            ),
            ProfileEditScreenMapperSnackbarAndDialogTestCase(
                throwable = Exception(),
                showUnsavedChangesDialog = false,
                expectedSnackbarMessage = TextOrResource.Resource(R.string.error_screen_text),
                expectedThrowableMessage = Exception().toString(),
            ),
        )
    }
}
