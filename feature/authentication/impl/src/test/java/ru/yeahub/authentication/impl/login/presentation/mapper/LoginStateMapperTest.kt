package ru.yeahub.authentication.impl.login.presentation.mapper

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.authentication.impl.R
import ru.yeahub.authentication.impl.login.presentation.model.LoginState
import ru.yeahub.authentication.impl.login.presentation.model.LoginUserInput
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.core_utils.validation.EmailValidator
import ru.yeahub.test.TestArgumentsProvider

class LoginStateMapperTest {

    @ParameterizedTest
    @ArgumentsSource(LoginStateMapperInitialStateArgumentsProvider::class)
    fun `should return correct initial state`(
        testCase: LoginStateMapperInitialStateTestCase,
    ) {
        val result = LoginStateMapper().getInitialState()

        assertEquals(
            testCase.expectedResult,
            result,
        )
    }

    @ParameterizedTest
    @ArgumentsSource(LoginStateMapperEmptyPasswordErrorArgumentsProvider::class)
    fun `should show empty password error when password field is touched`(
        testCase: LoginStateMapperEmptyPasswordErrorTestCase,
    ) {
        val result = LoginStateMapper().mapToScreenState(
            userInput = testCase.dataToTest,
        )

        assertEquals(
            testCase.expectedResult,
            result.passwordError,
        )
    }

    @ParameterizedTest
    @ArgumentsSource(LoginStateMapperInvalidEmailErrorArgumentsProvider::class)
    fun `should show invalid email error when email field is touched`(
        testCase: LoginStateMapperInvalidEmailErrorTestCase,
    ) {
        mockkObject(EmailValidator)

        try {
            every {
                EmailValidator.isValid(testCase.dataToTest.email)
            } returns false

            val result = LoginStateMapper().mapToScreenState(
                userInput = testCase.dataToTest,
            )

            assertEquals(
                testCase.expectedResult,
                result.emailError,
            )
        } finally {
            unmockkObject(EmailValidator)
        }
    }

    @ParameterizedTest
    @ArgumentsSource(LoginStateMapperSubmitEnabledArgumentsProvider::class)
    fun `should determine submit availability correctly`(
        testCase: LoginStateMapperSubmitEnabledTestCase,
    ) {
        mockkObject(EmailValidator)

        try {
            every {
                EmailValidator.isValid(testCase.dataToTest.email)
            } returns testCase.isEmailValid

            val result = LoginStateMapper().mapToScreenState(
                userInput = testCase.dataToTest,
            )

            assertEquals(
                testCase.expectedResult,
                result.isSubmitEnabled,
            )
        } finally {
            unmockkObject(EmailValidator)
        }
    }

    data class LoginStateMapperInitialStateTestCase(
        val expectedResult: LoginState,
    )

    data class LoginStateMapperEmptyPasswordErrorTestCase(
        val dataToTest: LoginUserInput,
        val expectedResult: TextOrResource?,
    )

    data class LoginStateMapperInvalidEmailErrorTestCase(
        val dataToTest: LoginUserInput,
        val expectedResult: TextOrResource?,
    )

    data class LoginStateMapperSubmitEnabledTestCase(
        val dataToTest: LoginUserInput,
        val isEmailValid: Boolean,
        val expectedResult: Boolean,
    )

    class LoginStateMapperInitialStateArgumentsProvider :
        TestArgumentsProvider<LoginStateMapperInitialStateTestCase>() {

        override fun testCases(): List<LoginStateMapperInitialStateTestCase> = listOf(
            LoginStateMapperInitialStateTestCase(
                expectedResult = LoginState(
                    email = "",
                    password = "",
                    isPasswordVisible = false,
                    emailError = null,
                    passwordError = null,
                    isSubmitEnabled = false,
                    isSubmitting = false,
                ),
            ),
        )
    }

    class LoginStateMapperEmptyPasswordErrorArgumentsProvider :
        TestArgumentsProvider<LoginStateMapperEmptyPasswordErrorTestCase>() {

        override fun testCases(): List<LoginStateMapperEmptyPasswordErrorTestCase> = listOf(
            LoginStateMapperEmptyPasswordErrorTestCase(
                dataToTest = LoginUserInput(
                    email = "",
                    password = "",
                    isPasswordVisible = false,
                    isEmailTouched = false,
                    isPasswordTouched = true,
                    isValidationRequested = false,
                    isSubmitting = false,
                    emailServerError = null,
                    passwordServerError = null,
                ),
                expectedResult = TextOrResource.Resource(
                    R.string.login_password_empty,
                ),
            ),
            LoginStateMapperEmptyPasswordErrorTestCase(
                dataToTest = LoginUserInput(
                    email = "",
                    password = "",
                    isPasswordVisible = false,
                    isEmailTouched = false,
                    isPasswordTouched = false,
                    isValidationRequested = false,
                    isSubmitting = false,
                    emailServerError = null,
                    passwordServerError = null,
                ),
                expectedResult = null,
            ),
        )
    }

    class LoginStateMapperInvalidEmailErrorArgumentsProvider :
        TestArgumentsProvider<LoginStateMapperInvalidEmailErrorTestCase>() {

        override fun testCases(): List<LoginStateMapperInvalidEmailErrorTestCase> = listOf(
            LoginStateMapperInvalidEmailErrorTestCase(
                dataToTest = LoginUserInput(
                    email = "invalid-email",
                    password = "",
                    isPasswordVisible = false,
                    isEmailTouched = true,
                    isPasswordTouched = false,
                    isValidationRequested = false,
                    isSubmitting = false,
                    emailServerError = null,
                    passwordServerError = null,
                ),
                expectedResult = TextOrResource.Resource(
                    R.string.login_email_invalid,
                ),
            ),
            LoginStateMapperInvalidEmailErrorTestCase(
                dataToTest = LoginUserInput(
                    email = "invalid-email",
                    password = "",
                    isPasswordVisible = false,
                    isEmailTouched = false,
                    isPasswordTouched = false,
                    isValidationRequested = false,
                    isSubmitting = false,
                    emailServerError = null,
                    passwordServerError = null,
                ),
                expectedResult = null,
            ),
        )
    }

    class LoginStateMapperSubmitEnabledArgumentsProvider :
        TestArgumentsProvider<LoginStateMapperSubmitEnabledTestCase>() {

        override fun testCases(): List<LoginStateMapperSubmitEnabledTestCase> = listOf(
            LoginStateMapperSubmitEnabledTestCase(
                dataToTest = createUserInput(
                    email = "user@example.com",
                    password = "password",
                ),
                isEmailValid = true,
                expectedResult = true,
            ),
            LoginStateMapperSubmitEnabledTestCase(
                dataToTest = createUserInput(
                    email = "invalid-email",
                    password = "password",
                ),
                isEmailValid = false,
                expectedResult = false,
            ),
            LoginStateMapperSubmitEnabledTestCase(
                dataToTest = createUserInput(
                    email = "user@example.com",
                    password = "",
                ),
                isEmailValid = true,
                expectedResult = false,
            ),
            LoginStateMapperSubmitEnabledTestCase(
                dataToTest = createUserInput(
                    email = "",
                    password = "password",
                ),
                isEmailValid = false,
                expectedResult = false,
            ),
        )

        private companion object {

            fun createUserInput(
                email: String,
                password: String,
            ): LoginUserInput = LoginUserInput(
                email = email,
                password = password,
                isPasswordVisible = false,
                isEmailTouched = false,
                isPasswordTouched = false,
                isValidationRequested = false,
                isSubmitting = false,
                emailServerError = null,
                passwordServerError = null,
            )
        }
    }
}