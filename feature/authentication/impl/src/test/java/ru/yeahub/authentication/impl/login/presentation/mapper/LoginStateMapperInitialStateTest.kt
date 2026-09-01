package ru.yeahub.authentication.impl.login.presentation.mapper

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.authentication.impl.login.presentation.model.LoginState
import ru.yeahub.test.TestArgumentsProvider

class LoginStateMapperInitialStateTest {
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

    data class LoginStateMapperInitialStateTestCase(
        val expectedResult: LoginState,
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
}