package ru.yeahub.profile_edit.impl.presentation.profile_edit_screen_mapper_tests

import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.profile_edit.impl.presentation.OperationError
import ru.yeahub.profile_edit.impl.presentation.ProfileEditMapperInput
import ru.yeahub.profile_edit.impl.presentation.ProfileEditMutableState
import ru.yeahub.profile_edit.impl.presentation.ProfileEditScreenMapper
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState
import ru.yeahub.profile_edit.impl.presentation.StaticDomainData
import ru.yeahub.profile_edit.impl.presentation.UserInput
import ru.yeahub.profile_edit.impl.presentation.ViewModelStaticData
import ru.yeahub.profile_edit.impl.ui.cropper.ImageValidationError
import ru.yeahub.profile_edit.impl.ui.cropper.ImageValidationException
import ru.yeahub.test.TestArgumentsProvider
import ru.yeahub.ui.R
import java.io.IOException
import ru.yeahub.profile_edit.impl.R as ProfileEditR

/**
 * Проверяет два независимых поведения при маппинге Loaded:
 * 1. showUnsavedChangesDialog передаётся в состояние без изменений (true/false).
 * 2. snackbarState — null при operationError=null, содержит actionMessage и errorMessage иначе.
 * Кейсы с operationError!=null дополнительно проверяют, что диалог и snackbar независимы.
 */
class ProfileEditScreenMapperSnackbarAndDialogTest {

    private val mapper = ProfileEditScreenMapper()

    @ParameterizedTest
    @ArgumentsSource(ProfileEditScreenMapperSnackbarAndDialogArgumentsProvider::class)
    internal fun `should pass through dialog flag and map snackbar state`(
        testCase: ProfileEditScreenMapperSnackbarAndDialogTestCase,
    ) {
        val input = ProfileEditMapperInput.Loaded(
            mutableState = ProfileEditMutableState(
                userInput = UserInput(
                    avatarUrl = "",
                    nickname = "validNick",
                    specialization = "",
                    location = "",
                    socialLinks = emptyMap(),
                    aboutMe = "",
                    selectedSkills = persistentListOf(),
                ),
                operationError = testCase.operationError,
                showUnsavedChangesDialog = testCase.showUnsavedChangesDialog,
            ),
            staticData = ViewModelStaticData(
                initialUserInput = UserInput(
                    avatarUrl = "",
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
        if (testCase.expectedActionMessage == null) {
            assertEquals(null, loaded.snackbarState)
        } else {
            val snackbar = loaded.snackbarState!!
            assertEquals(testCase.expectedActionMessage, snackbar.actionMessage)
            assertEquals(testCase.expectedErrorMessage, snackbar.errorMessage)
        }
    }

    internal data class ProfileEditScreenMapperSnackbarAndDialogTestCase(
        val operationError: OperationError?,
        val showUnsavedChangesDialog: Boolean,
        val expectedActionMessage: TextOrResource?,
        val expectedErrorMessage: TextOrResource?,
    )

    private class ProfileEditScreenMapperSnackbarAndDialogArgumentsProvider :
        TestArgumentsProvider<ProfileEditScreenMapperSnackbarAndDialogTestCase>() {
        override fun testCases() = listOf(
            ProfileEditScreenMapperSnackbarAndDialogTestCase(
                operationError = null,
                showUnsavedChangesDialog = false,
                expectedActionMessage = null,
                expectedErrorMessage = null,
            ),
            ProfileEditScreenMapperSnackbarAndDialogTestCase(
                operationError = null,
                showUnsavedChangesDialog = true,
                expectedActionMessage = null,
                expectedErrorMessage = null,
            ),
            ProfileEditScreenMapperSnackbarAndDialogTestCase(
                operationError = OperationError(
                    IOException("net err"),
                    TextOrResource.Resource(
                        ProfileEditR.string.error_action_save_profile,
                    ),
                ),
                showUnsavedChangesDialog = false,
                expectedActionMessage = TextOrResource.Resource(
                    ProfileEditR.string.error_action_save_profile,
                ),
                expectedErrorMessage = TextOrResource.Resource(ProfileEditR.string.error_no_internet),
            ),
            ProfileEditScreenMapperSnackbarAndDialogTestCase(
                operationError = OperationError(
                    IOException("net err"),
                    TextOrResource.Resource(
                        ProfileEditR.string.error_action_save_profile,
                    ),
                ),
                showUnsavedChangesDialog = true,
                expectedActionMessage = TextOrResource.Resource(
                    ProfileEditR.string.error_action_save_profile,
                ),
                expectedErrorMessage = TextOrResource.Resource(ProfileEditR.string.error_no_internet),
            ),
            ProfileEditScreenMapperSnackbarAndDialogTestCase(
                operationError = OperationError(
                    IOException(),
                    TextOrResource.Resource(
                        ProfileEditR.string.error_action_upload_avatar,
                    ),
                ),
                showUnsavedChangesDialog = false,
                expectedActionMessage = TextOrResource.Resource(
                    ProfileEditR.string.error_action_upload_avatar,
                ),
                expectedErrorMessage = TextOrResource.Resource(ProfileEditR.string.error_no_internet),
            ),
            ProfileEditScreenMapperSnackbarAndDialogTestCase(
                operationError = OperationError(
                    IOException(),
                    TextOrResource.Resource(
                        ProfileEditR.string.error_action_delete_avatar,
                    ),
                ),
                showUnsavedChangesDialog = false,
                expectedActionMessage = TextOrResource.Resource(
                    ProfileEditR.string.error_action_delete_avatar,
                ),
                expectedErrorMessage = TextOrResource.Resource(ProfileEditR.string.error_no_internet),
            ),
            ProfileEditScreenMapperSnackbarAndDialogTestCase(
                operationError = OperationError(
                    ImageValidationException(ImageValidationError.CannotRead),
                    TextOrResource.Resource(
                        ProfileEditR.string.error_action_image_validation,
                    ),
                ),
                showUnsavedChangesDialog = false,
                expectedActionMessage = TextOrResource.Resource(
                    ProfileEditR.string.error_action_image_validation,
                ),
                expectedErrorMessage = TextOrResource.Resource(
                    ProfileEditR.string.error_cannot_read_file,
                ),
            ),
            ProfileEditScreenMapperSnackbarAndDialogTestCase(
                operationError = OperationError(
                    ImageValidationException(ImageValidationError.FileTooLarge),
                    TextOrResource.Resource(
                        ProfileEditR.string.error_action_image_validation,
                    ),
                ),
                showUnsavedChangesDialog = false,
                expectedActionMessage = TextOrResource.Resource(
                    ProfileEditR.string.error_action_image_validation,
                ),
                expectedErrorMessage = TextOrResource.Resource(
                    ProfileEditR.string.error_file_too_large,
                ),
            ),
            ProfileEditScreenMapperSnackbarAndDialogTestCase(
                operationError = OperationError(
                    ImageValidationException(ImageValidationError.CropFailed),
                    TextOrResource.Resource(
                        ProfileEditR.string.error_action_image_validation,
                    ),
                ),
                showUnsavedChangesDialog = false,
                expectedActionMessage = TextOrResource.Resource(
                    ProfileEditR.string.error_action_image_validation,
                ),
                expectedErrorMessage = TextOrResource.Resource(
                    ProfileEditR.string.error_crop_failed,
                ),
            ),
            ProfileEditScreenMapperSnackbarAndDialogTestCase(
                operationError = OperationError(
                    RuntimeException("oops"),
                    TextOrResource.Resource(
                        ProfileEditR.string.error_action_save_profile,
                    ),
                ),
                showUnsavedChangesDialog = false,
                expectedActionMessage = TextOrResource.Resource(
                    ProfileEditR.string.error_action_save_profile,
                ),
                expectedErrorMessage = TextOrResource.Resource(R.string.error_screen_text),
            ),
        )
    }
}
