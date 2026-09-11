package ru.yeahub.authentication.impl.login.presentation.viewmodel

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.authentication.impl.R
import ru.yeahub.authentication.impl.login.domain.entity.AuthResult
import ru.yeahub.authentication.impl.login.domain.entity.AuthTokens
import ru.yeahub.authentication.impl.login.domain.entity.Failure
import ru.yeahub.authentication.impl.login.domain.entity.LoginError
import ru.yeahub.authentication.impl.login.domain.entity.LoginException
import ru.yeahub.authentication.impl.login.domain.entity.LoginModel
import ru.yeahub.authentication.impl.login.domain.entity.UserProfile
import ru.yeahub.authentication.impl.login.domain.usecase.LoginUseCase
import ru.yeahub.authentication.impl.login.presentation.mapper.LoginStateMapper
import ru.yeahub.authentication.impl.login.presentation.model.LoginAction
import ru.yeahub.authentication.impl.login.presentation.model.LoginCommand
import ru.yeahub.authentication.impl.login.util.MainDispatcherExtension
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.core_utils.validation.EmailValidator
import ru.yeahub.test.TestArgumentsProvider

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

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

    @ParameterizedTest
    @ArgumentsSource(LoginViewModelLoadingStateArgumentsProvider::class)
    fun `should update loading state during login`(
        testCase: LoginViewModelLoadingStateTestCase,
    ) = runTest(mainDispatcherExtension.dispatcher) {
        mockkObject(EmailValidator)

        try {
            every {
                EmailValidator.isValid(testCase.dataToTest.email)
            } returns true

            val loginResult = CompletableDeferred<AuthResult>()
            val loginUseCase = mockk<LoginUseCase>()

            coEvery {
                loginUseCase.invoke(
                    loginModel = testCase.dataToTest,
                )
            } coAnswers {
                loginResult.await()
            }

            val viewModel = LoginViewModel(
                loginUseCase = loginUseCase,
                mapper = LoginStateMapper(),
            )

            backgroundScope.launch(
                UnconfinedTestDispatcher(testScheduler),
            ) {
                viewModel.state.collect()
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

            runCurrent()

            assertEquals(
                testCase.expectedResultDuringLogin,
                viewModel.state.value.isSubmitting,
            )

            loginResult.complete(
                value = testCase.authResult,
            )

            advanceUntilIdle()

            assertEquals(
                testCase.expectedResultAfterLogin,
                viewModel.state.value.isSubmitting,
            )
        } finally {
            unmockkObject(EmailValidator)
        }
    }

    @ParameterizedTest
    @ArgumentsSource(LoginViewModelGlobalErrorArgumentsProvider::class)
    fun `should show snackbar for global login error`(
        testCase: LoginViewModelGlobalErrorTestCase,
    ) = runTest(mainDispatcherExtension.dispatcher) {
        val loginModel = LoginModel(
            email = "user@example.com",
            password = "Password1!",
        )

        mockkObject(EmailValidator)

        try {
            every {
                EmailValidator.isValid(loginModel.email)
            } returns true

            val loginUseCase = mockk<LoginUseCase>()

            coEvery {
                loginUseCase.invoke(
                    loginModel = loginModel,
                )
            } throws LoginException(
                error = testCase.dataToTest,
                failure = Failure(
                    cause = null,
                    httpCode = null,
                ),
            )

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
                    value = loginModel.email,
                ),
            )
            viewModel.onAction(
                action = LoginAction.OnPasswordChanged(
                    value = loginModel.password,
                ),
            )
            viewModel.onAction(
                action = LoginAction.OnLoginClick,
            )

            advanceUntilIdle()

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

    data class LoginViewModelLoadingStateTestCase(
        val dataToTest: LoginModel,
        val authResult: AuthResult,
        val expectedResultDuringLogin: Boolean,
        val expectedResultAfterLogin: Boolean,
    )

    data class LoginViewModelGlobalErrorTestCase(
        val dataToTest: LoginError,
        val expectedResult: LoginCommand,
    )

    class LoginViewModelSuccessfulLoginArgumentsProvider :
        TestArgumentsProvider<LoginViewModelSuccessfulLoginTestCase>() {

        override fun testCases(): List<LoginViewModelSuccessfulLoginTestCase> = listOf(
            LoginViewModelSuccessfulLoginTestCase(
                dataToTest = createLoginModel(),
                authResult = createAuthResult(),
                expectedResult = LoginCommand.NavigateToMain,
            ),
        )
    }

    class LoginViewModelLoadingStateArgumentsProvider :
        TestArgumentsProvider<LoginViewModelLoadingStateTestCase>() {

        override fun testCases(): List<LoginViewModelLoadingStateTestCase> = listOf(
            LoginViewModelLoadingStateTestCase(
                dataToTest = createLoginModel(),
                authResult = createAuthResult(),
                expectedResultDuringLogin = true,
                expectedResultAfterLogin = false,
            ),
        )
    }

    class LoginViewModelGlobalErrorArgumentsProvider :
        TestArgumentsProvider<LoginViewModelGlobalErrorTestCase>() {

        override fun testCases(): List<LoginViewModelGlobalErrorTestCase> = listOf(
            createGlobalErrorTestCase(
                error = LoginError.UserNotFound,
                stringResource = R.string.login_user_not_found,
            ),
            createGlobalErrorTestCase(
                error = LoginError.Network,
                stringResource = R.string.login_network_error,
            ),
            createGlobalErrorTestCase(
                error = LoginError.Server,
                stringResource = R.string.login_server_error,
            ),
            createGlobalErrorTestCase(
                error = LoginError.TokenSaveFailed,
                stringResource = R.string.login_token_save_error,
            ),
            createGlobalErrorTestCase(
                error = LoginError.AccountBlocked,
                stringResource = R.string.login_account_blocked,
            ),
            createGlobalErrorTestCase(
                error = LoginError.TooManyAttempts,
                stringResource = R.string.login_too_many_attempts,
            ),
            createGlobalErrorTestCase(
                error = LoginError.EmailNotConfirmed,
                stringResource = R.string.login_email_not_confirmed,
            ),
            createGlobalErrorTestCase(
                error = LoginError.Unknown,
                stringResource = R.string.login_unknown_error,
            ),
        )

        private companion object {

            fun createGlobalErrorTestCase(
                error: LoginError,
                stringResource: Int,
            ): LoginViewModelGlobalErrorTestCase {
                return LoginViewModelGlobalErrorTestCase(
                    dataToTest = error,
                    expectedResult = LoginCommand.ShowSnackbar(
                        message = TextOrResource.Resource(
                            stringResource,
                        ),
                    ),
                )
            }
        }
    }

    private companion object {

        fun createLoginModel(): LoginModel {
            return LoginModel(
                email = "user@example.com",
                password = "Password1!",
            )
        }

        fun createAuthResult(): AuthResult {
            return AuthResult(
                tokens = AuthTokens(
                    accessToken = "test-access-token",
                ),
                userProfile = UserProfile(
                    id = "test-user-id",
                    email = "user@example.com",
                    username = "test-user",
                    avatarUrl = null,
                ),
            )
        }
    }
}