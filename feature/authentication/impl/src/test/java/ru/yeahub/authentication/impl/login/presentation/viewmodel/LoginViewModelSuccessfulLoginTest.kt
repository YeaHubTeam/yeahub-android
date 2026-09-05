package ru.yeahub.authentication.impl.login.presentation.viewmodel

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.authentication.impl.login.domain.entity.AuthResult
import ru.yeahub.authentication.impl.login.domain.entity.AuthTokens
import ru.yeahub.authentication.impl.login.domain.entity.LoginModel
import ru.yeahub.authentication.impl.login.domain.entity.UserProfile
import ru.yeahub.authentication.impl.login.domain.usecase.LoginUseCase
import ru.yeahub.authentication.impl.login.presentation.mapper.LoginStateMapper
import ru.yeahub.authentication.impl.login.presentation.model.LoginAction
import ru.yeahub.authentication.impl.login.presentation.model.LoginCommand
import ru.yeahub.authentication.impl.login.util.MainDispatcherExtension
import ru.yeahub.core_utils.validation.EmailValidator
import ru.yeahub.test.TestArgumentsProvider

//вызов LoginUseCase и переход на главный экран после успешного входа
@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelSuccessfulLoginTest {

    @ParameterizedTest
    @ArgumentsSource(LoginViewModelSuccessfulLoginArgumentsProvider::class)
    fun `should login and navigate to main screen`(
        testCase: LoginViewModelSuccessfulLoginTestCase,
    ) = runTest(mainDispatcherExtension.dispatcher) {
        mockkObject(EmailValidator)

        try {
            every {
                EmailValidator.isValid(testCase.dataToTest.email)
            } returns true

            val loginUseCase = mockk<LoginUseCase>()

            coEvery {
                loginUseCase.invoke(
                    loginModel = testCase.dataToTest,
                )
            } returns testCase.authResult

            val viewModel = LoginViewModel(
                loginUseCase = loginUseCase,
                mapper = LoginStateMapper(),
            )

            val commandResult = async {
                viewModel.commands.first()
            }

            runCurrent()

            viewModel.onAction(
                action = LoginAction.OnEmailChanged(
                    value = testCase.dataToTest.email,
                ),
            )
            viewModel.onAction(
                action = LoginAction.OnPasswordChanged(
                    value = testCase.dataToTest.password,
                ),
            )
            viewModel.onAction(
                action = LoginAction.OnLoginClick,
            )

            advanceUntilIdle()

            coVerify(exactly = 1) {
                loginUseCase.invoke(
                    loginModel = testCase.dataToTest,
                )
            }

            assertEquals(
                testCase.expectedResult,
                commandResult.await(),
            )
        } finally {
            unmockkObject(EmailValidator)
        }
    }

    @JvmField
    @RegisterExtension
    val mainDispatcherExtension = MainDispatcherExtension()

    data class LoginViewModelSuccessfulLoginTestCase(
        val dataToTest: LoginModel,
        val authResult: AuthResult,
        val expectedResult: LoginCommand,
    )

    class LoginViewModelSuccessfulLoginArgumentsProvider :
        TestArgumentsProvider<LoginViewModelSuccessfulLoginTestCase>() {

        override fun testCases(): List<LoginViewModelSuccessfulLoginTestCase> = listOf(
            LoginViewModelSuccessfulLoginTestCase(
                dataToTest = LoginModel(
                    email = "user@example.com",
                    password = "Password1!",
                ),
                authResult = AuthResult(
                    tokens = AuthTokens(
                        accessToken = "test-access-token",
                    ),
                    userProfile = UserProfile(
                        id = "test-user-id",
                        email = "user@example.com",
                        username = "test-user",
                        avatarUrl = null,
                    ),
                ),
                expectedResult = LoginCommand.NavigateToMain,
            ),
        )
    }
}