package ru.yeahub.authentication.impl.login.presentation.mapper

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.authentication.impl.login.presentation.model.LoginUserInput
import ru.yeahub.core_utils.validation.EmailValidator
import ru.yeahub.test.TestArgumentsProvider

class LoginStateMapperSubmitEnabledTest {

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

    data class LoginStateMapperSubmitEnabledTestCase(
        val dataToTest: LoginUserInput,
        val isEmailValid: Boolean,
        val expectedResult: Boolean,
    )

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