package ru.yeahub.authentication.impl.login.presentation.mapper

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.authentication.impl.R
import ru.yeahub.authentication.impl.login.presentation.model.LoginUserInput
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.core_utils.validation.EmailValidator
import ru.yeahub.test.TestArgumentsProvider

//при некорректном email появляется ошибка, но до взаимодействия с полем она не показывается
class LoginStateMapperInvalidEmailErrorTest {

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

    data class LoginStateMapperInvalidEmailErrorTestCase(
        val dataToTest: LoginUserInput,
        val expectedResult: TextOrResource?,
    )

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
}