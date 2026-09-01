package ru.yeahub.authentication.impl.login.presentation.mapper

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.authentication.impl.R
import ru.yeahub.authentication.impl.login.presentation.model.LoginUserInput
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.test.TestArgumentsProvider

class LoginStateMapperEmptyPasswordErrorTest {

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

    data class LoginStateMapperEmptyPasswordErrorTestCase(
        val dataToTest: LoginUserInput,
        val expectedResult: TextOrResource?,
    )

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
}