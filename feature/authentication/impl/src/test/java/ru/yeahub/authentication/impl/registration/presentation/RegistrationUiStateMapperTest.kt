package ru.yeahub.authentication.impl.registration.presentation

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import ru.yeahub.authentication.impl.R
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

    @Test
    fun `mapToInitialState returns correct initial state`() {
        val state = mapper.mapToInitialState()

        assertTrue(state is RegistrationUiState.Content)
        val form = state.formState
        assertEquals("", form.nickname)
        assertEquals("", form.email)
        assertEquals("", form.password)
        assertEquals("", form.confirmPassword)
        assertFalse(form.isPdAccepted)
        assertFalse(form.isOfferAccepted)
        assertFalse(form.isSubmitEnabled)
        assertNull(form.emailError)
        assertNull(form.nicknameError)
        assertNull(form.passwordError)
        assertNull(form.confirmPasswordError)
    }

    @ParameterizedTest
    @MethodSource("provideValidationCases")
    fun `validateForm logic works correctly`(
        nickname: String,
        email: String,
        isEmailValid: Boolean,
        password: String,
        confirmPassword: String,
        isPdAccepted: Boolean,
        isOfferAccepted: Boolean,
        expectedSubmitEnabled: Boolean
    ) {
        every { EmailValidator.isValid(any()) } returns isEmailValid
        
        val form = mapper.getInitialFormState().copy(
            nickname = nickname,
            email = email,
            password = password,
            confirmPassword = confirmPassword,
            isPdAccepted = isPdAccepted,
            isOfferAccepted = isOfferAccepted
        )
        val currentState = RegistrationUiState.Content(form)

        val resultState = mapper.mapToUpdatedState(currentState, RegistrationAction.NicknameChanged(nickname))

        assertEquals(expectedSubmitEnabled, resultState.formState.isSubmitEnabled)
    }

    companion object {
        @JvmStatic
        fun provideValidationCases(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "user",
                "test@test.com",
                true,
                "Pass123!",
                "Pass123!",
                true,
                true,
                true
            ),
            Arguments.of(
                "",
                "test@test.com",
                true,
                "Pass123!",
                "Pass123!",
                true,
                true,
                false
            ),
            Arguments.of(
                "user",
                "invalid",
                false,
                "Pass123!",
                "Pass123!",
                true,
                true,
                false
            ),
            Arguments.of(
                "user",
                "test@test.com",
                true,
                "short",
                "short",
                true,
                true,
                false
            ),
            Arguments.of(
                "user",
                "test@test.com",
                true,
                "Pass123!",
                "Mismatch!",
                true,
                true,
                false
            ),
            Arguments.of(
                "user",
                "test@test.com",
                true,
                "short",
                "short",
                true,
                true,
                false
            ),
            Arguments.of(
                "user",
                "test@test.com",
                true,
                "nospecial123",
                "nospecial123",
                true,
                true,
                false
            ),
            Arguments.of(
                "user",
                "test@test.com",
                true,
                "NoDigit!",
                "NoDigit!",
                true,
                true,
                false
            ),
            Arguments.of(
                "user",
                "test@test.com",
                true,
                "nouppercase1!",
                "nouppercase1!",
                true,
                true,
                false
            ),
            Arguments.of(
                "user",
                "test@test.com",
                true,
                "Pass123!",
                "Pass123!",
                false,
                true,
                false
            ),
            Arguments.of(
                "user",
                "test@test.com",
                true,
                "Pass123!",
                "Pass123!",
                true,
                false,
                false
            )
        )

        @JvmStatic
        fun providePasswordErrorCases(): Stream<Arguments> = Stream.of(
            Arguments.of("short", R.string.error_password_too_short),
            Arguments.of("NoSpecial123", R.string.error_password_no_special_char),
            Arguments.of("NoDigit!", R.string.error_password_no_digit),
            Arguments.of("nouppercase1!", R.string.error_password_no_uppercase)
        )
    }

    @ParameterizedTest
    @MethodSource("providePasswordErrorCases")
    fun `validateForm returns correct error resource for specific password failures`(
        password: String,
        expectedErrorRes: Int
    ) {
        val form = mapper.getInitialFormState().copy(
            password = password,
            isPasswordTouched = true 
        )
        val currentState = RegistrationUiState.Content(form)

        val resultState = mapper.mapToUpdatedState(currentState, RegistrationAction.PasswordChanged(password))

        val error = resultState.formState.passwordError as? TextOrResource.Resource
        assertEquals(expectedErrorRes, error?.resource)
    }

    @Test
    fun `mapToLoadingState preserves form state`() {
        val form = mapper.getInitialFormState().copy(nickname = "some user")
        val currentState = RegistrationUiState.Content(form)

        val resultState = mapper.mapToLoadingState(currentState)

        assertTrue(resultState is RegistrationUiState.Loading)
        assertEquals(form, resultState.formState)
    }

    @Test
    fun `handleFocusAction updates touched state correctly`() {
        val form = mapper.getInitialFormState().copy(email = "test")
        val currentState = RegistrationUiState.Content(form)

        val stateAfterFocusLost = mapper.mapToUpdatedState(
            currentState,
            RegistrationAction.EmailFocusChanged(false)
        )
        assertTrue(stateAfterFocusLost.formState.isEmailTouched)

        val stateAfterFocusGained = mapper.mapToUpdatedState(
            stateAfterFocusLost,
            RegistrationAction.EmailFocusChanged(true)
        )
        assertFalse(stateAfterFocusGained.formState.isEmailTouched)
    }
}
