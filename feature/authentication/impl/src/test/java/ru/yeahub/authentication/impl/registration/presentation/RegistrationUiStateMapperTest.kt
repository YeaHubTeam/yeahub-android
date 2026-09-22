package ru.yeahub.authentication.impl.registration.presentation

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.authentication.impl.R
import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationError
import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationException
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.core_utils.validation.EmailValidator
import ru.yeahub.test.TestArgumentsProvider

class RegistrationUiStateMapperTest {

    private val mapper = RegistrationUiStateMapper()

    /**
     * Один параметризованный тест покрывает все основные сценарии,
     * которые умеет обрабатывать RegistrationUiStateMapper:
     *
     * - создание начального состояния;
     * - изменение полей формы;
     * - изменение focus/touched;
     * - изменение согласий;
     * - нажатие SubmitClicked;
     * - валидацию формы;
     * - состояние Loading;
     * - преобразование ошибок регистрации.
     *
     * EmailValidator вызывается внутри mapper и использует android.util.Patterns,
     * поэтому в JVM unit-тесте его необходимо замокировать.
     */
    @ParameterizedTest
    @ArgumentsSource(RegistrationUiStateMapperArgumentsProvider::class)
    fun `should correctly map registration state`(
        testCase: RegistrationUiStateMapperTestCase
    ) {
        mockkObject(EmailValidator)

        // isEmailValid — это результат зависимости EmailValidator,
        // а expectedState — ожидаемый результат работы самого mapper.
        every { EmailValidator.isValid(any()) } returns testCase.isEmailValid

        val result = when (testCase.scenario) {
            Scenario.INITIAL -> {
                mapper.mapToInitialState()
            }

            Scenario.UPDATE -> {
                mapper.mapToUpdatedState(
                    currentState = testCase.currentState!!,
                    action = testCase.action!!
                )
            }

            Scenario.LOADING -> {
                mapper.mapToLoadingState(
                    currentState = testCase.currentState!!
                )
            }

            Scenario.ERROR -> {
                mapper.mapToErrorState(
                    currentState = testCase.currentState!!,
                    exception = testCase.exception!!
                )
            }
        }

        Assertions.assertEquals(
            testCase.expectedState,
            result
        )
    }

    /**
     * Тип сценария определяет, какой публичный метод mapper необходимо вызвать.
     */
    enum class Scenario {
        INITIAL,
        UPDATE,
        LOADING,
        ERROR,
    }

    /**
     * Все данные одного тестового сценария.
     *
     * Не все поля нужны каждому сценарию:
     *
     * INITIAL  -> только expectedState
     * UPDATE   -> currentState + action
     * LOADING  -> currentState
     * ERROR    -> currentState + exception
     *
     * isEmailValid нужен только потому, что EmailValidator является
     * внешней зависимостью mapper и в JVM-тесте мы задаём его результат вручную.
     */
    data class RegistrationUiStateMapperTestCase(
        val scenario: Scenario,
        val currentState: RegistrationUiState? = null,
        val action: RegistrationAction? = null,
        val exception: RegistrationException? = null,
        val isEmailValid: Boolean = true,
        val expectedState: RegistrationUiState,
    )

    /**
     * Все 37 сценариев RegistrationUiStateMapper.
     */
    class RegistrationUiStateMapperArgumentsProvider :
        TestArgumentsProvider<RegistrationUiStateMapperTestCase>() {

        override fun testCases(): List<RegistrationUiStateMapperTestCase> =
            listOf(

                // -----------------------------------------------------------------
                // 1. INITIAL STATE
                // -----------------------------------------------------------------

                /**
                 * Mapper должен создать Content с полностью пустой формой.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.INITIAL,
                    expectedState = RegistrationUiState.Content(
                        RegistrationFormState(
                            nickname = "",
                            nicknameError = null,
                            email = "",
                            emailError = null,
                            password = "",
                            passwordError = null,
                            confirmPassword = "",
                            confirmPasswordError = null,
                            isPdAccepted = false,
                            isOfferAccepted = false,
                            isMailingAccepted = false,
                            isPasswordVisible = false,
                            isConfirmPasswordVisible = false,
                            isSubmitEnabled = false,
                            isEmailTouched = false,
                            isPasswordTouched = false,
                            isConfirmPasswordTouched = false,
                        )
                    )
                ),

                // -----------------------------------------------------------------
                // 2-5. FIELD CHANGES
                // -----------------------------------------------------------------

                /**
                 * Изменение nickname должно менять только nickname.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(validForm()),
                    action = RegistrationAction.NicknameChanged("Alex"),
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            nickname = "Alex"
                        )
                    )
                ),

                /**
                 * Изменение email должно менять только email.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(validForm()),
                    action = RegistrationAction.EmailChanged("alex@example.com"),
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            email = "alex@example.com"
                        )
                    )
                ),

                /**
                 * Изменение password должно менять только password.
                 *
                 * После изменения password пароль подтверждения остаётся
                 * прежним, поэтому форма становится невалидной.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(validForm()),
                    action = RegistrationAction.PasswordChanged("NewPassword1!"),
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            password = "NewPassword1!",
                            isSubmitEnabled = false
                        )
                    )
                ),

                /**
                 * Изменение confirmPassword должно менять только confirmPassword.
                 *
                 * Здесь значение отличается от password, поэтому submit выключен.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(validForm()),
                    action = RegistrationAction.ConfirmPasswordChanged(
                        "NewPassword1!"
                    ),
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            confirmPassword = "NewPassword1!",
                            isSubmitEnabled = false
                        )
                    )
                ),

                // -----------------------------------------------------------------
                // 6-14. FOCUS / TOUCHED
                // -----------------------------------------------------------------

                /**
                 * Email был заполнен и потерял focus -> email становится touched.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(
                        validForm().copy(
                            isEmailTouched = false
                        )
                    ),
                    action = RegistrationAction.EmailFocusChanged(
                        hasFocus = false
                    ),
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            isEmailTouched = true
                        )
                    )
                ),

                /**
                 * Email получил focus -> touched становится false.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(
                        validForm().copy(
                            isEmailTouched = true
                        )
                    ),
                    action = RegistrationAction.EmailFocusChanged(
                        hasFocus = true
                    ),
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            isEmailTouched = false
                        )
                    )
                ),

                /**
                 * Пустой email потерял focus.
                 *
                 * touched не устанавливается, потому что mapper проверяет
                 * form.email.isNotEmpty().
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(
                        validForm().copy(
                            email = "",
                            isEmailTouched = false
                        )
                    ),
                    action = RegistrationAction.EmailFocusChanged(
                        hasFocus = false
                    ),
                    isEmailValid = false,
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            email = "",
                            isEmailTouched = false,
                            isSubmitEnabled = false
                        )
                    )
                ),

                /**
                 * Заполненный password потерял focus -> password touched.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(
                        validForm().copy(
                            isPasswordTouched = false
                        )
                    ),
                    action = RegistrationAction.PasswordFocusChanged(
                        hasFocus = false
                    ),
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            isPasswordTouched = true
                        )
                    )
                ),

                /**
                 * Password получил focus -> touched false.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(
                        validForm().copy(
                            isPasswordTouched = true
                        )
                    ),
                    action = RegistrationAction.PasswordFocusChanged(
                        hasFocus = true
                    ),
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            isPasswordTouched = false
                        )
                    )
                ),

                /**
                 * Пустой password потерял focus -> touched остаётся false.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(
                        validForm().copy(
                            password = "",
                            isPasswordTouched = false
                        )
                    ),
                    action = RegistrationAction.PasswordFocusChanged(
                        hasFocus = false
                    ),
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            password = "",
                            isPasswordTouched = false,
                            isSubmitEnabled = false
                        )
                    )
                ),

                /**
                 * Заполненный confirmPassword потерял focus -> touched.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(
                        validForm().copy(
                            isConfirmPasswordTouched = false
                        )
                    ),
                    action = RegistrationAction.ConfirmPasswordFocusChanged(
                        hasFocus = false
                    ),
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            isConfirmPasswordTouched = true
                        )
                    )
                ),

                /**
                 * ConfirmPassword получил focus -> touched false.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(
                        validForm().copy(
                            isConfirmPasswordTouched = true
                        )
                    ),
                    action = RegistrationAction.ConfirmPasswordFocusChanged(
                        hasFocus = true
                    ),
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            isConfirmPasswordTouched = false
                        )
                    )
                ),

                /**
                 * Пустой confirmPassword потерял focus -> touched false.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(
                        validForm().copy(
                            confirmPassword = "",
                            isConfirmPasswordTouched = false
                        )
                    ),
                    action = RegistrationAction.ConfirmPasswordFocusChanged(
                        hasFocus = false
                    ),
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            confirmPassword = "",
                            isConfirmPasswordTouched = false,
                            isSubmitEnabled = false
                        )
                    )
                ),

                // -----------------------------------------------------------------
                // 15-17. CONSENTS
                // -----------------------------------------------------------------

                /**
                 * Согласие на обработку персональных данных включено.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(
                        validForm()
                    ),
                    action = RegistrationAction.PdAcceptedChanged(false),
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            isPdAccepted = false,
                            isSubmitEnabled = false
                        )
                    )
                ),

                /**
                 * Согласие с офертой выключено.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(
                        validForm()
                    ),
                    action = RegistrationAction.OfferAcceptedChanged(false),
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            isOfferAccepted = false,
                            isSubmitEnabled = false
                        )
                    )
                ),

                /**
                 * Отказ от рассылки НЕ должен запрещать регистрацию.
                 *
                 * Важно: isMailingAccepted отсутствует в условии
                 * isSubmitEnabled внутри mapper.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(
                        validForm()
                    ),
                    action = RegistrationAction.MailingAcceptedChanged(false),
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            isMailingAccepted = false,
                            isSubmitEnabled = true
                        )
                    )
                ),

                // -----------------------------------------------------------------
                // 18-19. PASSWORD VISIBILITY
                // -----------------------------------------------------------------

                /**
                 * Переключение видимости пароля.
                 *
                 * Это техническая ветка mapper, поэтому её можно покрыть,
                 * даже если с точки зрения бизнес-логики она не является
                 * отдельным пользовательским сценарием.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(
                        validForm().copy(
                            isPasswordVisible = false
                        )
                    ),
                    action = RegistrationAction.TogglePasswordVisible,
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            isPasswordVisible = true
                        )
                    )
                ),

                /**
                 * Переключение видимости подтверждения пароля.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(
                        validForm().copy(
                            isConfirmPasswordVisible = false
                        )
                    ),
                    action = RegistrationAction.ToggleConfirmPasswordVisible,
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            isConfirmPasswordVisible = true
                        )
                    )
                ),

                // -----------------------------------------------------------------
                // 20. SUBMIT
                // -----------------------------------------------------------------

                /**
                 * Mapper не должен менять форму при SubmitClicked.
                 *
                 * Само выполнение регистрации происходит уже во ViewModel,
                 * а не в mapper.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(
                        validForm()
                    ),
                    action = RegistrationAction.SubmitClicked,
                    expectedState = RegistrationUiState.Content(
                        validForm()
                    )
                ),

                // -----------------------------------------------------------------
                // 21-28. FORM VALIDATION
                // -----------------------------------------------------------------

                /**
                 * Полностью валидная форма -> submit разрешён.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(
                        validForm()
                    ),
                    action = RegistrationAction.SubmitClicked,
                    isEmailValid = true,
                    expectedState = RegistrationUiState.Content(
                        validForm()
                    )
                ),

                /**
                 * Пустой nickname -> submit запрещён.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(
                        validForm().copy(
                            nickname = ""
                        )
                    ),
                    action = RegistrationAction.SubmitClicked,
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            nickname = "",
                            isSubmitEnabled = false
                        )
                    )
                ),

                /**
                 * Некорректный email -> submit запрещён.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(
                        validForm()
                    ),
                    action = RegistrationAction.SubmitClicked,
                    isEmailValid = false,
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            isSubmitEnabled = false
                        )
                    )
                ),

                /**
                 * Слабый password -> submit запрещён.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(
                        validForm().copy(
                            password = "weak"
                        )
                    ),
                    action = RegistrationAction.SubmitClicked,
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            password = "weak",
                            isSubmitEnabled = false
                        )
                    )
                ),

                /**
                 * Пароли не совпадают -> submit запрещён.
                 *
                 * Здесь touched = true, чтобы mapper дополнительно показал
                 * ошибку несовпадения паролей.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(
                        validForm().copy(
                            confirmPassword = "AnotherPassword1!",
                            isConfirmPasswordTouched = true
                        )
                    ),
                    action = RegistrationAction.SubmitClicked,
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            confirmPassword = "AnotherPassword1!",
                            isConfirmPasswordTouched = true,
                            passwordError = TextOrResource.Resource(
                                R.string.error_passwords_not_match
                            ),
                            confirmPasswordError = TextOrResource.Resource(
                                R.string.error_passwords_not_match
                            ),
                            isSubmitEnabled = false
                        )
                    )
                ),

                /**
                 * Пользователь не принял PD -> submit запрещён.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(
                        validForm().copy(
                            isPdAccepted = false
                        )
                    ),
                    action = RegistrationAction.SubmitClicked,
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            isPdAccepted = false,
                            isSubmitEnabled = false
                        )
                    )
                ),

                /**
                 * Пользователь не принял оферту -> submit запрещён.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(
                        validForm().copy(
                            isOfferAccepted = false
                        )
                    ),
                    action = RegistrationAction.SubmitClicked,
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            isOfferAccepted = false,
                            isSubmitEnabled = false
                        )
                    )
                ),

                /**
                 * Отказ от рассылки не влияет на возможность отправки формы.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(
                        validForm().copy(
                            isMailingAccepted = false
                        )
                    ),
                    action = RegistrationAction.SubmitClicked,
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            isMailingAccepted = false,
                            isSubmitEnabled = true
                        )
                    )
                ),

                // -----------------------------------------------------------------
                // 29-32. PASSWORD ERROR TYPES
                // -----------------------------------------------------------------

                /**
                 * Слишком короткий пароль.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(
                        validForm().copy(
                            password = "Aa1!",
                            isPasswordTouched = true
                        )
                    ),
                    action = RegistrationAction.SubmitClicked,
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            password = "Aa1!",
                            isPasswordTouched = true,
                            passwordError = TextOrResource.Resource(
                                R.string.error_password_too_short
                            ),
                            isSubmitEnabled = false
                        )
                    )
                ),

                /**
                 * В пароле нет заглавной буквы.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(
                        validForm().copy(
                            password = "password1!",
                            isPasswordTouched = true
                        )
                    ),
                    action = RegistrationAction.SubmitClicked,
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            password = "password1!",
                            isPasswordTouched = true,
                            passwordError = TextOrResource.Resource(
                                R.string.error_password_no_uppercase
                            ),
                            isSubmitEnabled = false
                        )
                    )
                ),

                /**
                 * В пароле нет цифры.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(
                        validForm().copy(
                            password = "Password!",
                            isPasswordTouched = true
                        )
                    ),
                    action = RegistrationAction.SubmitClicked,
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            password = "Password!",
                            isPasswordTouched = true,
                            passwordError = TextOrResource.Resource(
                                R.string.error_password_no_digit
                            ),
                            isSubmitEnabled = false
                        )
                    )
                ),

                /**
                 * В пароле нет специального символа.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.UPDATE,
                    currentState = RegistrationUiState.Content(
                        validForm().copy(
                            password = "Password1",
                            isPasswordTouched = true
                        )
                    ),
                    action = RegistrationAction.SubmitClicked,
                    expectedState = RegistrationUiState.Content(
                        validForm().copy(
                            password = "Password1",
                            isPasswordTouched = true,
                            passwordError = TextOrResource.Resource(
                                R.string.error_password_no_special_char
                            ),
                            isSubmitEnabled = false
                        )
                    )
                ),

                // -----------------------------------------------------------------
                // 33. LOADING
                // -----------------------------------------------------------------

                /**
                 * Loading должен сохранить текущее состояние формы,
                 * но изменить UiState на Loading.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.LOADING,
                    currentState = RegistrationUiState.Content(
                        validForm()
                    ),
                    expectedState = RegistrationUiState.Loading(
                        validForm()
                    )
                ),

                // -----------------------------------------------------------------
                // 34-37. REGISTRATION ERRORS
                // -----------------------------------------------------------------

                /**
                 * Conflict -> пользователь уже существует.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.ERROR,
                    currentState = RegistrationUiState.Content(
                        validForm()
                    ),
                    exception = RegistrationException(
                        error = RegistrationError.Conflict
                    ),
                    expectedState = RegistrationUiState.Error(
                        message = TextOrResource.Resource(
                            R.string.error_user_already_exists
                        ),
                        formState = validForm()
                    )
                ),

                /**
                 * NotFound -> ресурс не найден.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.ERROR,
                    currentState = RegistrationUiState.Content(
                        validForm()
                    ),
                    exception = RegistrationException(
                        error = RegistrationError.NotFound
                    ),
                    expectedState = RegistrationUiState.Error(
                        message = TextOrResource.Resource(
                            R.string.error_resource_not_found
                        ),
                        formState = validForm()
                    )
                ),

                /**
                 * Success в данном mapper не является отдельной веткой:
                 * всё, что не Conflict и не NotFound, попадает в else
                 * и получает login_unknown_error.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.ERROR,
                    currentState = RegistrationUiState.Content(
                        validForm()
                    ),
                    exception = RegistrationException(
                        error = RegistrationError.Success
                    ),
                    expectedState = RegistrationUiState.Error(
                        message = TextOrResource.Resource(
                            R.string.login_unknown_error
                        ),
                        formState = validForm()
                    )
                ),

                /**
                 * Ещё один неизвестный тип ошибки также попадает в else.
                 *
                 * Если в вашем RegistrationError есть конкретный enum
                 * для неизвестной ошибки (например UnknownError),
                 * здесь нужно использовать именно его.
                 */
                RegistrationUiStateMapperTestCase(
                    scenario = Scenario.ERROR,
                    currentState = RegistrationUiState.Content(
                        validForm()
                    ),
                    exception = RegistrationException(
                        error = RegistrationError.UnknownError
                    ),
                    expectedState = RegistrationUiState.Error(
                        message = TextOrResource.Resource(
                            R.string.login_unknown_error
                        ),
                        formState = validForm()
                    )
                )
            )

        /**
         * Фабрика валидного состояния формы.
         *
         * Используется только как тестовый fixture, чтобы не писать
         * 17 полей RegistrationFormState в каждом сценарии.
         *
         * Такая функция не содержит поведения mapper и поэтому не является
         * отдельным тестируемым сценарием.
         */
        private fun validForm(): RegistrationFormState =
            RegistrationFormState(
                nickname = "Alex",
                nicknameError = null,
                email = "alex@example.com",
                emailError = null,
                password = "Password1!",
                passwordError = null,
                confirmPassword = "Password1!",
                confirmPasswordError = null,
                isPdAccepted = true,
                isOfferAccepted = true,
                isMailingAccepted = true,
                isPasswordVisible = false,
                isConfirmPasswordVisible = false,
                isSubmitEnabled = true,
                isEmailTouched = false,
                isPasswordTouched = false,
                isConfirmPasswordTouched = false,
            )
    }
}