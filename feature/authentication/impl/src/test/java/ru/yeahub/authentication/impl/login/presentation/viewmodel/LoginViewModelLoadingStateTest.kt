package ru.yeahub.authentication.impl.login.presentation.viewmodel

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
import ru.yeahub.authentication.impl.login.util.MainDispatcherExtension
import ru.yeahub.core_utils.validation.EmailValidator
import ru.yeahub.test.TestArgumentsProvider

//загрузка включается во время входа и выключается после завершения.
@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelLoadingStateTest {

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

    @JvmField
    @RegisterExtension
    val mainDispatcherExtension = MainDispatcherExtension()

    data class LoginViewModelLoadingStateTestCase(
        val dataToTest: LoginModel,
        val authResult: AuthResult,
        val expectedResultDuringLogin: Boolean,
        val expectedResultAfterLogin: Boolean,
    )

    class LoginViewModelLoadingStateArgumentsProvider :
        TestArgumentsProvider<LoginViewModelLoadingStateTestCase>() {

        override fun testCases(): List<LoginViewModelLoadingStateTestCase> = listOf(
            LoginViewModelLoadingStateTestCase(
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
                expectedResultDuringLogin = true,
                expectedResultAfterLogin = false,
            ),
        )
    }
}
