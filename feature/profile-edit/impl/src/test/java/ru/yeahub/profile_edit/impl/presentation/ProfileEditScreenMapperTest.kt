package ru.yeahub.profile_edit.impl.presentation

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import retrofit2.HttpException
import retrofit2.Response
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSocialPlatform
import ru.yeahub.ui.R
import java.io.IOException
import java.util.stream.Stream
import ru.yeahub.profile_edit.impl.R as ProfileEditR

abstract class TestProfileEditArgumentsProvider<T> : ArgumentsProvider {
    abstract fun testCases(): List<T>
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
        testCases().map { Arguments.of(it) }.stream()
}

class ProfileEditScreenMapperTest {

    private val mapper = ProfileEditScreenMapper()

    companion object {
        private val skillKotlin = DomainProfileEditSkill(imageRes = 1, name = "Kotlin")
        private val skillJava = DomainProfileEditSkill(imageRes = 2, name = "Java")
        private val skillAndroid = DomainProfileEditSkill(imageRes = 3, name = "Android")

        private fun createHttpException(code: Int): HttpException {
            val response = Response.error<Any>(code, "".toResponseBody(null))
            return HttpException(response)
        }
    }

    private fun createUserInput(
        avatarUrl: String? = "https://example.com/avatar.jpg",
        nickname: String = "validNick",
        specialization: String = "Android",
        location: String = "Moscow",
        socialLinks: Map<DomainProfileEditSocialPlatform, String> = emptyMap(),
        aboutMe: String = "About me text",
        selectedSkills: PersistentList<DomainProfileEditSkill> = persistentListOf(skillKotlin),
    ): UserInput = UserInput(
        avatarUrl = avatarUrl,
        nickname = nickname,
        specialization = specialization,
        location = location,
        socialLinks = socialLinks,
        aboutMe = aboutMe,
        selectedSkills = selectedSkills,
    )

    private fun createStaticData(
        email: String = "user@example.com",
        specializationList: PersistentList<String> = persistentListOf("Android", "iOS", "Backend"),
        isSpecializationEditable: Boolean = true,
        allSkills: PersistentList<DomainProfileEditSkill> = persistentListOf(
            skillKotlin,
            skillJava,
            skillAndroid,
        ),
    ): StaticDomainData = StaticDomainData(
        email = email,
        specializationList = specializationList,
        isSpecializationEditable = isSpecializationEditable,
        allSkills = allSkills,
    )

    private fun createViewModelStaticData(
        initialUserInput: UserInput = createUserInput(),
        staticData: StaticDomainData = createStaticData(),
    ): ViewModelStaticData = ViewModelStaticData(
        initialUserInput = initialUserInput,
        staticData = staticData,
    )

    private fun createMutableState(
        userInput: UserInput = createUserInput(),
        throwable: Throwable? = null,
        showUnsavedChangesDialog: Boolean = false,
    ): ProfileEditMutableState = ProfileEditMutableState(
        userInput = userInput,
        throwable = throwable,
        showUnsavedChangesDialog = showUnsavedChangesDialog,
    )

    private fun createLoadedInput(
        mutableState: ProfileEditMutableState = createMutableState(),
        staticData: ViewModelStaticData = createViewModelStaticData(),
    ): ProfileEditMapperInput.Loaded = ProfileEditMapperInput.Loaded(
        mutableState = mutableState,
        staticData = staticData,
    )


    @Test
    fun `getScreenState should return Loading when input is Loading`() {
        val input = ProfileEditMapperInput.Loading
        val result = mapper.getScreenState(input)
        assertSame(ProfileEditState.Loading, result)
    }

    @Test
    fun `getScreenState should return Loaded with correct content when input is Loaded`() {
        val input = createLoadedInput()
        val result = mapper.getScreenState(input)
        val loaded = result as ProfileEditState.Loaded
        assertEquals("https://example.com/avatar.jpg", loaded.personalInfoState.avatarUrl)
        assertEquals(
            ProfileEditState.ValidatedField("validNick", null),
            loaded.personalInfoState.nickname,
        )
        assertEquals(
            persistentListOf("Android", "iOS", "Backend"),
            loaded.personalInfoState.specializationList,
        )
        assertEquals("Android", loaded.personalInfoState.specialization)
        assertEquals(true, loaded.personalInfoState.isSpecializationEditable)
        assertEquals("user@example.com", loaded.personalInfoState.email)
        assertEquals(
            ProfileEditState.ValidatedField("Moscow", null),
            loaded.personalInfoState.location,
        )
        assertEquals(
            persistentMapOf<DomainProfileEditSocialPlatform, ProfileEditState.ValidatedField>(),
            loaded.personalInfoState.socialLinks,
        )
        assertEquals(ProfileEditState.AboutMeTabState("About me text"), loaded.aboutMeTabState)
        assertEquals(persistentListOf(skillJava, skillAndroid), loaded.skillsTabState.listOfSkills)
        assertEquals(persistentListOf(skillKotlin), loaded.skillsTabState.listOfChosenSkills)
        assertEquals(false, loaded.showUnsavedChangesDialog)
        assertEquals(false, loaded.hasValidationErrors)
        assertEquals(null, loaded.snackbarState)
    }

    @Test
    fun `getScreenState should return Error with mapped message when input is Error`() {
        val input = ProfileEditMapperInput.Error(IOException("no network"))
        val result = mapper.getScreenState(input)
        val error = result as ProfileEditState.Error
        assertEquals(TextOrResource.Resource(ProfileEditR.string.error_no_internet), error.message)
    }

    data class NicknameValidationTestCase(
        val nickname: String,
        val expectedError: TextOrResource?,
        val expectedHasValidationErrors: Boolean,
    )

    class NicknameValidationArgumentsProvider :
        TestProfileEditArgumentsProvider<NicknameValidationTestCase>() {
        override fun testCases() = listOf(
            NicknameValidationTestCase(
                nickname = "",
                expectedError = TextOrResource.Resource(R.string.error_minimal_length_2),
                expectedHasValidationErrors = true,
            ),
            NicknameValidationTestCase(
                nickname = "a",
                expectedError = TextOrResource.Resource(R.string.error_minimal_length_2),
                expectedHasValidationErrors = true,
            ),
            NicknameValidationTestCase(
                nickname = "ab",
                expectedError = null,
                expectedHasValidationErrors = false,
            ),
            NicknameValidationTestCase(
                nickname = "abc",
                expectedError = null,
                expectedHasValidationErrors = false,
            ),
            NicknameValidationTestCase(
                nickname = "a".repeat(29),
                expectedError = null,
                expectedHasValidationErrors = false,
            ),
            NicknameValidationTestCase(
                nickname = "a".repeat(30),
                expectedError = null,
                expectedHasValidationErrors = false,
            ),
            NicknameValidationTestCase(
                nickname = "a".repeat(31),
                expectedError = TextOrResource.Resource(R.string.error_max_length_30),
                expectedHasValidationErrors = true,
            ),
            NicknameValidationTestCase(
                nickname = "a".repeat(100),
                expectedError = TextOrResource.Resource(R.string.error_max_length_30),
                expectedHasValidationErrors = true,
            ),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(NicknameValidationArgumentsProvider::class)
    fun `getScreenState should validate nickname length boundaries`(
        testCase: NicknameValidationTestCase,
    ) {
        val input = createLoadedInput(
            mutableState = createMutableState(
                userInput = createUserInput(nickname = testCase.nickname),
            ),
        )
        val result = mapper.getScreenState(input)
        val loaded = result as ProfileEditState.Loaded
        assertEquals(testCase.nickname, loaded.personalInfoState.nickname.value)
        assertEquals(testCase.expectedError, loaded.personalInfoState.nickname.error)
        assertEquals(testCase.expectedHasValidationErrors, loaded.hasValidationErrors)
    }

    data class LocationValidationTestCase(
        val location: String,
        val expectedError: TextOrResource?,
        val expectedHasValidationErrors: Boolean,
    )

    class LocationValidationArgumentsProvider :
        TestProfileEditArgumentsProvider<LocationValidationTestCase>() {
        override fun testCases() = listOf(
            LocationValidationTestCase(
                location = "",
                expectedError = null,
                expectedHasValidationErrors = false,
            ),
            LocationValidationTestCase(
                location = "a".repeat(254),
                expectedError = null,
                expectedHasValidationErrors = false,
            ),
            LocationValidationTestCase(
                location = "a".repeat(255),
                expectedError = null,
                expectedHasValidationErrors = false,
            ),
            LocationValidationTestCase(
                location = "a".repeat(256),
                expectedError = TextOrResource.Resource(R.string.error_max_length_255),
                expectedHasValidationErrors = true,
            ),
            LocationValidationTestCase(
                location = "a".repeat(500),
                expectedError = TextOrResource.Resource(R.string.error_max_length_255),
                expectedHasValidationErrors = true,
            ),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(LocationValidationArgumentsProvider::class)
    fun `getScreenState should validate location max length`(
        testCase: LocationValidationTestCase,
    ) {
        val input = createLoadedInput(
            mutableState = createMutableState(
                userInput = createUserInput(location = testCase.location),
            ),
        )
        val result = mapper.getScreenState(input)
        val loaded = result as ProfileEditState.Loaded
        assertEquals(testCase.location, loaded.personalInfoState.location.value)
        assertEquals(testCase.expectedError, loaded.personalInfoState.location.error)
        assertEquals(testCase.expectedHasValidationErrors, loaded.hasValidationErrors)
    }


    data class SocialLinksValidationTestCase(
        val socialLinks: Map<DomainProfileEditSocialPlatform, String>,
        val expectedErrors: Map<DomainProfileEditSocialPlatform, TextOrResource?>,
        val expectedHasValidationErrors: Boolean,
    )

    class SocialLinksValidationArgumentsProvider :
        TestProfileEditArgumentsProvider<SocialLinksValidationTestCase>() {
        override fun testCases() = listOf(
            SocialLinksValidationTestCase(
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
            SocialLinksValidationTestCase(
                socialLinks = mapOf(
                    DomainProfileEditSocialPlatform.GitHub to "a".repeat(255),
                ),
                expectedErrors = mapOf(
                    DomainProfileEditSocialPlatform.GitHub to null,
                ),
                expectedHasValidationErrors = false,
            ),
            SocialLinksValidationTestCase(
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
            SocialLinksValidationTestCase(
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
            SocialLinksValidationTestCase(
                socialLinks = emptyMap(),
                expectedErrors = emptyMap(),
                expectedHasValidationErrors = false,
            ),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(SocialLinksValidationArgumentsProvider::class)
    fun `getScreenState should validate social links max length`(
        testCase: SocialLinksValidationTestCase,
    ) {
        val input = createLoadedInput(
            mutableState = createMutableState(
                userInput = createUserInput(socialLinks = testCase.socialLinks),
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

    data class ErrorThrowableTestCase(
        val throwable: Throwable,
        val expectedMessage: TextOrResource,
    )

    class ErrorThrowableArgumentsProvider :
        TestProfileEditArgumentsProvider<ErrorThrowableTestCase>() {
        override fun testCases() = listOf(
            ErrorThrowableTestCase(
                throwable = IOException("no network"),
                expectedMessage = TextOrResource.Resource(ProfileEditR.string.error_no_internet),
            ),
            ErrorThrowableTestCase(
                throwable = createHttpException(401),
                expectedMessage = TextOrResource.Resource(
                    ProfileEditR.string.error_session_expired,
                ),
            ),
            ErrorThrowableTestCase(
                throwable = createHttpException(403),
                expectedMessage = TextOrResource.Resource(ProfileEditR.string.error_forbidden),
            ),
            ErrorThrowableTestCase(
                throwable = createHttpException(404),
                expectedMessage = TextOrResource.Resource(
                    ProfileEditR.string.error_profile_not_found,
                ),
            ),
            ErrorThrowableTestCase(
                throwable = createHttpException(413),
                expectedMessage = TextOrResource.Resource(
                    ProfileEditR.string.error_file_too_large,
                ),
            ),
            ErrorThrowableTestCase(
                throwable = createHttpException(400),
                expectedMessage = TextOrResource.Resource(ProfileEditR.string.error_invalid_data),
            ),
            ErrorThrowableTestCase(
                throwable = createHttpException(422),
                expectedMessage = TextOrResource.Resource(ProfileEditR.string.error_invalid_data),
            ),
            ErrorThrowableTestCase(
                throwable = createHttpException(500),
                expectedMessage = TextOrResource.Resource(ProfileEditR.string.error_server),
            ),
            ErrorThrowableTestCase(
                throwable = createHttpException(503),
                expectedMessage = TextOrResource.Resource(ProfileEditR.string.error_server),
            ),
            ErrorThrowableTestCase(
                throwable = createHttpException(599),
                expectedMessage = TextOrResource.Resource(ProfileEditR.string.error_server),
            ),
            ErrorThrowableTestCase(
                throwable = createHttpException(402),
                expectedMessage = TextOrResource.Resource(R.string.error_screen_text),
            ),
            ErrorThrowableTestCase(
                throwable = createHttpException(418),
                expectedMessage = TextOrResource.Resource(R.string.error_screen_text),
            ),
            ErrorThrowableTestCase(
                throwable = RuntimeException("unknown"),
                expectedMessage = TextOrResource.Resource(R.string.error_screen_text),
            ),
            ErrorThrowableTestCase(
                throwable = IllegalStateException("bug"),
                expectedMessage = TextOrResource.Resource(R.string.error_screen_text),
            ),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ErrorThrowableArgumentsProvider::class)
    fun `getScreenState should map throwable to correct error message`(
        testCase: ErrorThrowableTestCase,
    ) {
        val input = ProfileEditMapperInput.Error(testCase.throwable)
        val result = mapper.getScreenState(input)
        val error = result as ProfileEditState.Error
        assertEquals(testCase.expectedMessage, error.message)
    }

    data class SnackbarAndDialogTestCase(
        val throwable: Throwable?,
        val showUnsavedChangesDialog: Boolean,
        val expectedSnackbarMessage: TextOrResource?,
        val expectedThrowableMessage: String?,
    )

    class SnackbarAndDialogArgumentsProvider :
        TestProfileEditArgumentsProvider<SnackbarAndDialogTestCase>() {
        override fun testCases() = listOf(
            SnackbarAndDialogTestCase(
                throwable = null,
                showUnsavedChangesDialog = false,
                expectedSnackbarMessage = null,
                expectedThrowableMessage = null,
            ),
            SnackbarAndDialogTestCase(
                throwable = null,
                showUnsavedChangesDialog = true,
                expectedSnackbarMessage = null,
                expectedThrowableMessage = null,
            ),
            SnackbarAndDialogTestCase(
                throwable = IOException("net err"),
                showUnsavedChangesDialog = false,
                expectedSnackbarMessage = TextOrResource.Resource(
                    ProfileEditR.string.error_no_internet,
                ),
                expectedThrowableMessage = "net err",
            ),
            SnackbarAndDialogTestCase(
                throwable = IOException("net err"),
                showUnsavedChangesDialog = true,
                expectedSnackbarMessage = TextOrResource.Resource(
                    ProfileEditR.string.error_no_internet,
                ),
                expectedThrowableMessage = "net err",
            ),
            SnackbarAndDialogTestCase(
                throwable = RuntimeException("oops"),
                showUnsavedChangesDialog = false,
                expectedSnackbarMessage = TextOrResource.Resource(R.string.error_screen_text),
                expectedThrowableMessage = "oops",
            ),
            SnackbarAndDialogTestCase(
                throwable = Exception(),
                showUnsavedChangesDialog = false,
                expectedSnackbarMessage = TextOrResource.Resource(R.string.error_screen_text),
                expectedThrowableMessage = Exception().toString(),
            ),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(SnackbarAndDialogArgumentsProvider::class)
    fun `getScreenState should pass through dialog flag and map snackbar state`(
        testCase: SnackbarAndDialogTestCase,
    ) {
        val input = createLoadedInput(
            mutableState = createMutableState(
                throwable = testCase.throwable,
                showUnsavedChangesDialog = testCase.showUnsavedChangesDialog,
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

    data class SkillsMappingTestCase(
        val allSkills: PersistentList<DomainProfileEditSkill>,
        val chosenSkills: PersistentList<DomainProfileEditSkill>,
        val expectedAvailableSkills: PersistentList<DomainProfileEditSkill>,
        val expectedChosenSkills: PersistentList<DomainProfileEditSkill>,
    )

    class SkillsMappingArgumentsProvider :
        TestProfileEditArgumentsProvider<SkillsMappingTestCase>() {
        override fun testCases() = listOf(
            SkillsMappingTestCase(
                allSkills = persistentListOf(skillKotlin, skillJava, skillAndroid),
                chosenSkills = persistentListOf(),
                expectedAvailableSkills = persistentListOf(skillKotlin, skillJava, skillAndroid),
                expectedChosenSkills = persistentListOf(),
            ),
            SkillsMappingTestCase(
                allSkills = persistentListOf(skillKotlin, skillJava, skillAndroid),
                chosenSkills = persistentListOf(skillKotlin),
                expectedAvailableSkills = persistentListOf(skillJava, skillAndroid),
                expectedChosenSkills = persistentListOf(skillKotlin),
            ),
            SkillsMappingTestCase(
                allSkills = persistentListOf(skillKotlin, skillJava, skillAndroid),
                chosenSkills = persistentListOf(skillKotlin, skillJava, skillAndroid),
                expectedAvailableSkills = persistentListOf(),
                expectedChosenSkills = persistentListOf(skillKotlin, skillJava, skillAndroid),
            ),
            SkillsMappingTestCase(
                allSkills = persistentListOf(skillKotlin, skillJava, skillAndroid),
                chosenSkills = persistentListOf(skillJava),
                expectedAvailableSkills = persistentListOf(skillKotlin, skillAndroid),
                expectedChosenSkills = persistentListOf(skillJava),
            ),
            SkillsMappingTestCase(
                allSkills = persistentListOf(),
                chosenSkills = persistentListOf(),
                expectedAvailableSkills = persistentListOf(),
                expectedChosenSkills = persistentListOf(),
            ),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(SkillsMappingArgumentsProvider::class)
    fun `getScreenState should correctly partition skills between available and chosen`(
        testCase: SkillsMappingTestCase,
    ) {
        val input = createLoadedInput(
            mutableState = createMutableState(
                userInput = createUserInput(selectedSkills = testCase.chosenSkills),
            ),
            staticData = createViewModelStaticData(
                staticData = createStaticData(allSkills = testCase.allSkills),
            ),
        )
        val result = mapper.getScreenState(input)
        val loaded = result as ProfileEditState.Loaded
        assertEquals(testCase.expectedAvailableSkills, loaded.skillsTabState.listOfSkills)
        assertEquals(testCase.expectedChosenSkills, loaded.skillsTabState.listOfChosenSkills)
    }
}
