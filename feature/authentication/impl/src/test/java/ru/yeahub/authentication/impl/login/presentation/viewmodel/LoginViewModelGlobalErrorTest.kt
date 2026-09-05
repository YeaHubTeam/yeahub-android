package ru.yeahub.authentication.impl.login.presentation.viewmodel

import io.mockk.coEvery
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
import ru.yeahub.authentication.impl.R
import ru.yeahub.authentication.impl.login.domain.entity.Failure
import ru.yeahub.authentication.impl.login.domain.entity.LoginError
import ru.yeahub.authentication.impl.login.domain.entity.LoginException
import ru.yeahub.authentication.impl.login.domain.entity.LoginModel
import ru.yeahub.authentication.impl.login.domain.usecase.LoginUseCase
import ru.yeahub.authentication.impl.login.presentation.mapper.LoginStateMapper
import ru.yeahub.authentication.impl.login.presentation.model.LoginAction
import ru.yeahub.authentication.impl.login.presentation.model.LoginCommand
import ru.yeahub.authentication.impl.login.util.MainDispatcherExtension
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.core_utils.validation.EmailValidator
import ru.yeahub.test.TestArgumentsProvider

//показ Snackbar для ошибок сети, сервера, отсутствующего пользователя, сохранения токена и неизвестной ошибки.
@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelGlobalErrorTest {

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

    data class LoginViewModelGlobalErrorTestCase(
        val dataToTest: LoginError,
        val expectedResult: LoginCommand,
    )

    class LoginViewModelGlobalErrorArgumentsProvider :
        TestArgumentsProvider<LoginViewModelGlobalErrorTestCase>() {

        override fun testCases(): List<LoginViewModelGlobalErrorTestCase> = listOf(
            LoginViewModelGlobalErrorTestCase(
                dataToTest = LoginError.UserNotFound,
                expectedResult = LoginCommand.ShowSnackbar(
                    message = TextOrResource.Resource(
                        R.string.login_user_not_found,
                    ),
                ),
            ),
            LoginViewModelGlobalErrorTestCase(
                dataToTest = LoginError.Network,
                expectedResult = LoginCommand.ShowSnackbar(
                    message = TextOrResource.Resource(
                        R.string.login_network_error,
                    ),
                ),
            ),
            LoginViewModelGlobalErrorTestCase(
                dataToTest = LoginError.Server,
                expectedResult = LoginCommand.ShowSnackbar(
                    message = TextOrResource.Resource(
                        R.string.login_server_error,
                    ),
                ),
            ),
            LoginViewModelGlobalErrorTestCase(
                dataToTest = LoginError.TokenSaveFailed,
                expectedResult = LoginCommand.ShowSnackbar(
                    message = TextOrResource.Resource(
                        R.string.login_token_save_error,
                    ),
                ),
            ),

            LoginViewModelGlobalErrorTestCase(
                dataToTest = LoginError.AccountBlocked,
                expectedResult = LoginCommand.ShowSnackbar(
                    message = TextOrResource.Resource(
                        R.string.login_account_blocked,
                    ),
                ),
            ),

            LoginViewModelGlobalErrorTestCase(
                dataToTest = LoginError.TooManyAttempts,
                expectedResult = LoginCommand.ShowSnackbar(
                    message = TextOrResource.Resource(
                        R.string.login_too_many_attempts,
                    ),
                ),
            ),

            LoginViewModelGlobalErrorTestCase(
                dataToTest = LoginError.EmailNotConfirmed,
                expectedResult = LoginCommand.ShowSnackbar(
                    message = TextOrResource.Resource(
                        R.string.login_email_not_confirmed,
                    ),
                ),
            ),
            LoginViewModelGlobalErrorTestCase(
                dataToTest = LoginError.Unknown,
                expectedResult = LoginCommand.ShowSnackbar(
                    message = TextOrResource.Resource(
                        R.string.login_unknown_error,
                    ),
                ),
            ),
        )
    }
}
