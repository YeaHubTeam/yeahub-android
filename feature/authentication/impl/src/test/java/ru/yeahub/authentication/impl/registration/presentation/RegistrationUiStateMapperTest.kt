package ru.yeahub.authentication.impl.registration.presentation

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.authentication.impl.R
import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationError
import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationException
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.core_utils.validation.EmailValidator
import java.util.stream.Stream

class RegistrationUiStateMapperTest {

    private val mapper = RegistrationUiStateMapper()

    @BeforeEach
    fun setUp() {
        mockkObject(EmailValidator)
        every { EmailValidator.isValid(any()) } returns true
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @ParameterizedTest
    @ArgumentsSource(UpdatedStateArgumentsProvider::class)
    fun `should update form state by action correctly`(testCase: UpdatedStateTestCase) {
        val initialForm = testCase.setupForm(mapper.getInitialFormState())
        val currentState = RegistrationUiState.Content(initialForm)

        val result = mapper.mapToUpdatedState(currentState, testCase.action)

        assertEquals(testCase.expectedForm, result.formState)
    }

    @ParameterizedTest
    @ArgumentsSource(ValidationArgumentsProvider::class)
    fun `should validate form and return correct isSubmitEnabled`(testCase: ValidationTestCase) {
        every { EmailValidator.isValid(any()) } returns testCase.isEmailValid

        val form = mapper.getInitialFormState().copy(
            nickname = testCase.nickname,
            email = testCase.email,
            password = testCase.password,
            confirmPassword = testCase.confirmPassword,
            isPdAccepted = testCase.isPdAccepted,
            isOfferAccepted = testCase.isOfferAccepted,
        )
        val currentState = RegistrationUiState.Content(form)

        val result = mapper.mapToUpdatedState(
            currentState,
            testCase.action,
        )

        assertEquals(testCase.expectedSubmitEnabled, result.formState.isSubmitEnabled)
    }

    @ParameterizedTest
    @ArgumentsSource(ErrorStateArgumentsProvider::class)
    fun `should map exception to correct error resource`(testCase: ErrorStateTestCase) {
        val currentState = RegistrationUiState.Content(mapper.getInitialFormState())

        val result = mapper.mapToErrorState(currentState, testCase.exception)

        assertTrue(result is RegistrationUiState.Error)
        val errorMessage = (result as RegistrationUiState.Error).message as TextOrResource.Resource
        assertEquals(testCase.expectedErrorRes, errorMessage.resource)
    }

    data class UpdatedStateTestCase(
        val name: String,
        val action: RegistrationAction,
        val setupForm: (RegistrationFormState) -> RegistrationFormState = { it },
        val expectedForm: RegistrationFormState,
    ) {
        override fun toString(): String = name
    }

    data class ValidationTestCase(
        val name: String,
        val nickname: String,
        val email: String,
        val isEmailValid: Boolean,
        val password: String,
        val confirmPassword: String,
        val isPdAccepted: Boolean,
        val isOfferAccepted: Boolean,
        val action: RegistrationAction,
        val expectedSubmitEnabled: Boolean,
    ) {
        override fun toString(): String = name
    }

    data class ErrorStateTestCase(
        val name: String,
        val exception: RegistrationException,
        val expectedErrorRes: Int,
    ) {
        override fun toString(): String = name
    }

    class UpdatedStateArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(
                    UpdatedStateTestCase(
                        name = "NicknameChanged обновляет nickname",
                        action = RegistrationAction.NicknameChanged("John"),
                        expectedForm = initialFormWith(nickname = "John"),
                    )
                ),
                Arguments.of(
                    UpdatedStateTestCase(
                        name = "EmailChanged обновляет email",
                        action = RegistrationAction.EmailChanged("test@mail.ru"),
                        expectedForm = initialFormWith(email = "test@mail.ru"),
                    )
                ),
                Arguments.of(
                    UpdatedStateTestCase(
                        name = "PasswordChanged обновляет password",
                        action = RegistrationAction.PasswordChanged("Pass123!"),
                        expectedForm = initialFormWith(password = "Pass123!"),
                    )
                ),
                Arguments.of(
                    UpdatedStateTestCase(
                        name = "ConfirmPasswordChanged обновляет confirmPassword",
                        action = RegistrationAction.ConfirmPasswordChanged("Pass123!"),
                        expectedForm = initialFormWith(confirmPassword = "Pass123!"),
                    )
                ),
                Arguments.of(
                    UpdatedStateTestCase(
                        name = "EmailFocusChanged(false) при непустом email ставит isEmailTouched = true",
                        action = RegistrationAction.EmailFocusChanged(false),
                        setupForm = { it.copy(email = "test@mail.ru") },
                        expectedForm = initialFormWith(
                            email = "test@mail.ru",
                            isEmailTouched = true,
                        ),
                    )
                ),
                Arguments.of(
                    UpdatedStateTestCase(
                        name = "EmailFocusChanged(true) не ставит isEmailTouched",
                        action = RegistrationAction.EmailFocusChanged(true),
                        setupForm = { it.copy(email = "test@mail.ru", isEmailTouched = true) },
                        expectedForm = initialFormWith(email = "test@mail.ru"),
                    )
                ),
                Arguments.of(
                    UpdatedStateTestCase(
                        name = "PasswordFocusChanged(false) при непустом пароле ставит isPasswordTouched = true",
                        action = RegistrationAction.PasswordFocusChanged(false),
                        setupForm = { it.copy(password = "Pass123!") },
                        expectedForm = initialFormWith(
                            password = "Pass123!",
                            isPasswordTouched = true,
                        ),
                    )
                ),
                Arguments.of(
                    UpdatedStateTestCase(
                        name = "ConfirmPasswordFocusChanged(false) при непустом " +
                            "пароле ставит isConfirmPasswordTouched = true",
                        action = RegistrationAction.ConfirmPasswordFocusChanged(false),
                        setupForm = { it.copy(password = "Pass123!", confirmPassword = "Pass123!") },
                        expectedForm = initialFormWith(
                            password = "Pass123!",
                            confirmPassword = "Pass123!",
                            isConfirmPasswordTouched = true,
                        ),
                    )
                ),
                Arguments.of(
                    UpdatedStateTestCase(
                        name = "PdAcceptedChanged(true) обновляет isPdAccepted",
                        action = RegistrationAction.PdAcceptedChanged(true),
                        expectedForm = initialFormWith(isPdAccepted = true),
                    )
                ),
                Arguments.of(
                    UpdatedStateTestCase(
                        name = "OfferAcceptedChanged(true) обновляет isOfferAccepted",
                        action = RegistrationAction.OfferAcceptedChanged(true),
                        expectedForm = initialFormWith(isOfferAccepted = true),
                    )
                ),
                Arguments.of(
                    UpdatedStateTestCase(
                        name = "MailingAcceptedChanged(true) обновляет isMailingAccepted",
                        action = RegistrationAction.MailingAcceptedChanged(true),
                        expectedForm = initialFormWith(isMailingAccepted = true),
                    )
                ),
                Arguments.of(
                    UpdatedStateTestCase(
                        name = "TogglePasswordVisible переключает isPasswordVisible на true",
                        action = RegistrationAction.TogglePasswordVisible,
                        expectedForm = initialFormWith(isPasswordVisible = true),
                    )
                ),
                Arguments.of(
                    UpdatedStateTestCase(
                        name = "ToggleConfirmPasswordVisible переключает isConfirmPasswordVisible на true",
                        action = RegistrationAction.ToggleConfirmPasswordVisible,
                        expectedForm = initialFormWith(isConfirmPasswordVisible = true),
                    )
                ),
                Arguments.of(
                    UpdatedStateTestCase(
                        name = "SubmitClicked не меняет форму",
                        action = RegistrationAction.SubmitClicked,
                        expectedForm = initialFormWith(),
                    )
                ),
            )
        }
    }

    class ValidationArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(
                    ValidationTestCase(
                        name = "Все поля валидны — submit разрешён",
                        nickname = "user",
                        email = "test@test.com",
                        isEmailValid = true,
                        password = "Pass123!",
                        confirmPassword = "Pass123!",
                        isPdAccepted = true,
                        isOfferAccepted = true,
                        expectedSubmitEnabled = true,
                        action = RegistrationAction.NicknameChanged("user")
                    )
                ),
                Arguments.of(
                    ValidationTestCase(
                        name = "Пустой nickname — submit запрещён",
                        nickname = "",
                        email = "test@test.com",
                        isEmailValid = true,
                        password = "Pass123!",
                        confirmPassword = "Pass123!",
                        isPdAccepted = true,
                        isOfferAccepted = true,
                        expectedSubmitEnabled = false,
                        action = RegistrationAction.NicknameChanged("")
                    )
                ),
                Arguments.of(
                    ValidationTestCase(
                        name = "Невалидный email — submit запрещён",
                        nickname = "user",
                        email = "invalid",
                        isEmailValid = false,
                        password = "Pass123!",
                        confirmPassword = "Pass123!",
                        isPdAccepted = true,
                        isOfferAccepted = true,
                        expectedSubmitEnabled = false,
                        action = RegistrationAction.EmailChanged("user")
                    )
                ),
                Arguments.of(
                    ValidationTestCase(
                        name = "Слабый пароль — submit запрещён",
                        nickname = "user",
                        email = "test@test.com",
                        isEmailValid = true,
                        password = "short",
                        confirmPassword = "short",
                        isPdAccepted = true,
                        isOfferAccepted = true,
                        expectedSubmitEnabled = false,
                        action = RegistrationAction.PasswordChanged("1234")
                    )
                ),
                Arguments.of(
                    ValidationTestCase(
                        name = "Пароли не совпадают — submit запрещён",
                        nickname = "user",
                        email = "test@test.com",
                        isEmailValid = true,
                        password = "Pass123!",
                        confirmPassword = "Mismatch!",
                        isPdAccepted = true,
                        isOfferAccepted = true,
                        expectedSubmitEnabled = false,
                        action = RegistrationAction.ConfirmPasswordChanged("1234")
                    )
                ),
                Arguments.of(
                    ValidationTestCase(
                        name = "ПД не принят — submit запрещён",
                        nickname = "user",
                        email = "test@test.com",
                        isEmailValid = true,
                        password = "Pass123!",
                        confirmPassword = "Pass123!",
                        isPdAccepted = false,
                        isOfferAccepted = true,
                        expectedSubmitEnabled = false,
                        action = RegistrationAction.PdAcceptedChanged(false)
                    )
                ),
                Arguments.of(
                    ValidationTestCase(
                        name = "Оферта не принята — submit запрещён",
                        nickname = "user",
                        email = "test@test.com",
                        isEmailValid = true,
                        password = "Pass123!",
                        confirmPassword = "Pass123!",
                        isPdAccepted = true,
                        isOfferAccepted = false,
                        expectedSubmitEnabled = false,
                        action = RegistrationAction.OfferAcceptedChanged(false)
                    )
                ),
            )
        }
    }

    class ErrorStateArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(
                    ErrorStateTestCase(
                        name = "Conflict → error_user_already_exists",
                        exception = RegistrationException(RegistrationError.Conflict),
                        expectedErrorRes = R.string.error_user_already_exists,
                    )
                ),
                Arguments.of(
                    ErrorStateTestCase(
                        name = "NotFound → error_resource_not_found",
                        exception = RegistrationException(RegistrationError.NotFound),
                        expectedErrorRes = R.string.error_resource_not_found,
                    )
                ),
                Arguments.of(
                    ErrorStateTestCase(
                        name = "UnknownError → login_unknown_error",
                        exception = RegistrationException(RegistrationError.UnknownError),
                        expectedErrorRes = R.string.login_unknown_error,
                    )
                ),
            )
        }
    }
}

private fun initialFormWith(
    nickname: String = "",
    nicknameError: TextOrResource? = null,
    email: String = "",
    emailError: TextOrResource? = null,
    password: String = "",
    passwordError: TextOrResource? = null,
    confirmPassword: String = "",
    confirmPasswordError: TextOrResource? = null,
    isPdAccepted: Boolean = false,
    isOfferAccepted: Boolean = false,
    isMailingAccepted: Boolean = false,
    isPasswordVisible: Boolean = false,
    isConfirmPasswordVisible: Boolean = false,
    isSubmitEnabled: Boolean = false,
    isEmailTouched: Boolean = false,
    isPasswordTouched: Boolean = false,
    isConfirmPasswordTouched: Boolean = false,
): RegistrationFormState = RegistrationFormState(
    nickname = nickname,
    nicknameError = nicknameError,
    email = email,
    emailError = emailError,
    password = password,
    passwordError = passwordError,
    confirmPassword = confirmPassword,
    confirmPasswordError = confirmPasswordError,
    isPdAccepted = isPdAccepted,
    isOfferAccepted = isOfferAccepted,
    isMailingAccepted = isMailingAccepted,
    isPasswordVisible = isPasswordVisible,
    isConfirmPasswordVisible = isConfirmPasswordVisible,
    isSubmitEnabled = isSubmitEnabled,
    isEmailTouched = isEmailTouched,
    isPasswordTouched = isPasswordTouched,
    isConfirmPasswordTouched = isConfirmPasswordTouched,
)
