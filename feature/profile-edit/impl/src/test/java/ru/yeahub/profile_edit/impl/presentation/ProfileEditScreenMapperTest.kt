package ru.yeahub.profile_edit.impl.presentation

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import retrofit2.HttpException
import retrofit2.Response
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSocialPlatform
import ru.yeahub.test.TestArgumentsProvider
import ru.yeahub.ui.R
import java.io.IOException
import ru.yeahub.profile_edit.impl.R as ProfileEditR

private object TestFixtures {

    val skillKotlin = DomainProfileEditSkill(imageRes = 1, name = "Kotlin")
    val skillJava = DomainProfileEditSkill(imageRes = 2, name = "Java")
    val skillAndroid = DomainProfileEditSkill(imageRes = 3, name = "Android")

    fun createUserInput(
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

    fun createStaticData(
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

    fun createViewModelStaticData(
        initialUserInput: UserInput = createUserInput(),
        staticData: StaticDomainData = createStaticData(),
    ): ViewModelStaticData = ViewModelStaticData(
        initialUserInput = initialUserInput,
        staticData = staticData,
    )

    fun createMutableState(
        userInput: UserInput = createUserInput(),
        throwable: Throwable? = null,
        showUnsavedChangesDialog: Boolean = false,
    ): ProfileEditMutableState = ProfileEditMutableState(
        userInput = userInput,
        throwable = throwable,
        showUnsavedChangesDialog = showUnsavedChangesDialog,
    )

    fun createLoadedInput(
        mutableState: ProfileEditMutableState = createMutableState(),
        staticData: ViewModelStaticData = createViewModelStaticData(),
    ): ProfileEditMapperInput.Loaded = ProfileEditMapperInput.Loaded(
        mutableState = mutableState,
        staticData = staticData,
    )

    fun createHttpException(code: Int): HttpException {
        val response = Response.error<Any>(code, "".toResponseBody(null))
        return HttpException(response)
    }

    fun loadedInputWithNickname(nickname: String) = createLoadedInput(
        mutableState = createMutableState(userInput = createUserInput(nickname = nickname)),
    )

    fun loadedInputWithLocation(location: String) = createLoadedInput(
        mutableState = createMutableState(userInput = createUserInput(location = location)),
    )

    fun loadedInputWithSocialLinks(
        socialLinks: Map<DomainProfileEditSocialPlatform, String>,
    ) = createLoadedInput(
        mutableState = createMutableState(userInput = createUserInput(socialLinks = socialLinks)),
    )

    fun loadedInputWithThrowable(
        throwable: Throwable?,
        showUnsavedChangesDialog: Boolean,
    ) = createLoadedInput(
        mutableState = createMutableState(
            throwable = throwable,
            showUnsavedChangesDialog = showUnsavedChangesDialog,
        ),
    )

    fun loadedInputWithSkills(
        allSkills: PersistentList<DomainProfileEditSkill>,
        chosenSkills: PersistentList<DomainProfileEditSkill>,
    ) = createLoadedInput(
        mutableState = createMutableState(userInput = createUserInput(selectedSkills = chosenSkills)),
        staticData = createViewModelStaticData(staticData = createStaticData(allSkills = allSkills)),
    )

    fun expectedLoadedState() = ProfileEditState.Loaded(
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
            listOfSkills = persistentListOf(skillJava, skillAndroid),
            listOfChosenSkills = persistentListOf(skillKotlin),
        ),
        showUnsavedChangesDialog = false,
        hasValidationErrors = false,
        snackbarState = null,
    )
}

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
                input = TestFixtures.createLoadedInput(),
                expectedResult = TestFixtures.expectedLoadedState(),
            ),
        )
    }
}

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
        val input = TestFixtures.loadedInputWithNickname(testCase.nickname)
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
        val input = TestFixtures.loadedInputWithLocation(testCase.location)
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
        val input = TestFixtures.loadedInputWithSocialLinks(testCase.socialLinks)
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

/**
 * Проверяет маппинг всех типов Throwable на TextOrResource в mapThrowableToMessage():
 * IOException, все именованные HTTP-коды (401/403/404/413/400/422),
 * диапазон 500..599 (граничные значения 500/599 и середина 503),
 * else-ветка HttpException (402, 418) и else-ветка общая (RuntimeException, IllegalStateException).
 */
class ProfileEditScreenMapperErrorThrowableMappingTest {

    private val mapper = ProfileEditScreenMapper()

    @ParameterizedTest
    @ArgumentsSource(ProfileEditScreenMapperErrorThrowableMappingArgumentsProvider::class)
    fun `should map throwable to correct error message`(
        testCase: ProfileEditScreenMapperErrorThrowableMappingTestCase,
    ) {
        val input = ProfileEditMapperInput.Error(testCase.throwable)
        val result = mapper.getScreenState(input)
        val error = result as ProfileEditState.Error
        assertEquals(testCase.expectedMessage, error.message)
    }

    data class ProfileEditScreenMapperErrorThrowableMappingTestCase(
        val throwable: Throwable,
        val expectedMessage: TextOrResource,
    )

    class ProfileEditScreenMapperErrorThrowableMappingArgumentsProvider :
        TestArgumentsProvider<ProfileEditScreenMapperErrorThrowableMappingTestCase>() {
        override fun testCases() = listOf(
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = IOException("no network"),
                expectedMessage = TextOrResource.Resource(ProfileEditR.string.error_no_internet),
            ),
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = TestFixtures.createHttpException(401),
                expectedMessage = TextOrResource.Resource(
                    ProfileEditR.string.error_session_expired,
                ),
            ),
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = TestFixtures.createHttpException(403),
                expectedMessage = TextOrResource.Resource(ProfileEditR.string.error_forbidden),
            ),
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = TestFixtures.createHttpException(404),
                expectedMessage = TextOrResource.Resource(
                    ProfileEditR.string.error_profile_not_found,
                ),
            ),
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = TestFixtures.createHttpException(413),
                expectedMessage = TextOrResource.Resource(
                    ProfileEditR.string.error_file_too_large,
                ),
            ),
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = TestFixtures.createHttpException(400),
                expectedMessage = TextOrResource.Resource(ProfileEditR.string.error_invalid_data),
            ),
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = TestFixtures.createHttpException(422),
                expectedMessage = TextOrResource.Resource(ProfileEditR.string.error_invalid_data),
            ),
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = TestFixtures.createHttpException(500),
                expectedMessage = TextOrResource.Resource(ProfileEditR.string.error_server),
            ),
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = TestFixtures.createHttpException(503),
                expectedMessage = TextOrResource.Resource(ProfileEditR.string.error_server),
            ),
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = TestFixtures.createHttpException(599),
                expectedMessage = TextOrResource.Resource(ProfileEditR.string.error_server),
            ),
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = TestFixtures.createHttpException(402),
                expectedMessage = TextOrResource.Resource(R.string.error_screen_text),
            ),
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = TestFixtures.createHttpException(418),
                expectedMessage = TextOrResource.Resource(R.string.error_screen_text),
            ),
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = RuntimeException("unknown"),
                expectedMessage = TextOrResource.Resource(R.string.error_screen_text),
            ),
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = IllegalStateException("bug"),
                expectedMessage = TextOrResource.Resource(R.string.error_screen_text),
            ),
        )
    }
}

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
        val input = TestFixtures.loadedInputWithThrowable(
            throwable = testCase.throwable,
            showUnsavedChangesDialog = testCase.showUnsavedChangesDialog,
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
        val input = TestFixtures.loadedInputWithSkills(
            allSkills = testCase.allSkills,
            chosenSkills = testCase.chosenSkills,
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
                allSkills = persistentListOf(
                    TestFixtures.skillKotlin,
                    TestFixtures.skillJava,
                    TestFixtures.skillAndroid,
                ),
                chosenSkills = persistentListOf(),
                expectedAvailableSkills = persistentListOf(
                    TestFixtures.skillKotlin,
                    TestFixtures.skillJava,
                    TestFixtures.skillAndroid,
                ),
                expectedChosenSkills = persistentListOf(),
            ),
            ProfileEditScreenMapperSkillsMappingTestCase(
                allSkills = persistentListOf(
                    TestFixtures.skillKotlin,
                    TestFixtures.skillJava,
                    TestFixtures.skillAndroid,
                ),
                chosenSkills = persistentListOf(TestFixtures.skillKotlin),
                expectedAvailableSkills = persistentListOf(
                    TestFixtures.skillJava,
                    TestFixtures.skillAndroid,
                ),
                expectedChosenSkills = persistentListOf(TestFixtures.skillKotlin),
            ),
            ProfileEditScreenMapperSkillsMappingTestCase(
                allSkills = persistentListOf(
                    TestFixtures.skillKotlin,
                    TestFixtures.skillJava,
                    TestFixtures.skillAndroid,
                ),
                chosenSkills = persistentListOf(
                    TestFixtures.skillKotlin,
                    TestFixtures.skillJava,
                    TestFixtures.skillAndroid,
                ),
                expectedAvailableSkills = persistentListOf(),
                expectedChosenSkills = persistentListOf(
                    TestFixtures.skillKotlin,
                    TestFixtures.skillJava,
                    TestFixtures.skillAndroid,
                ),
            ),
            ProfileEditScreenMapperSkillsMappingTestCase(
                allSkills = persistentListOf(
                    TestFixtures.skillKotlin,
                    TestFixtures.skillJava,
                    TestFixtures.skillAndroid,
                ),
                chosenSkills = persistentListOf(TestFixtures.skillJava),
                expectedAvailableSkills = persistentListOf(
                    TestFixtures.skillKotlin,
                    TestFixtures.skillAndroid,
                ),
                expectedChosenSkills = persistentListOf(TestFixtures.skillJava),
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
