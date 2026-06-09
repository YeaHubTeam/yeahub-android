package ru.yeahub.authentication.impl.registration.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import ru.yeahub.authentication.impl.R
import ru.yeahub.core_ui.component.PrimaryButton
import ru.yeahub.core_ui.component.PrimaryTextField
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_ui.theme.YeaHubTheme

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel = koinViewModel(),
    onRegistrationSuccess: () -> Unit,
    onOpenPdPolicy: () -> Unit,
    onOpenOffer: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.commands.collect { command ->
            when (command) {
                is RegistrationCommand.NavigateToSuccess -> onRegistrationSuccess()
                is RegistrationCommand.ShowError -> {
                    android.widget.Toast.makeText(
                        context,
                        command.message.getString(context),
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    RegistrationContent(
        state = state,
        onAction = viewModel::onAction,
        onOpenPdPolicy = onOpenPdPolicy,
        onOpenOffer = onOpenOffer
    )
}

@Composable
fun RegistrationContent(
    state: RegistrationUiState,
    onAction: (RegistrationAction) -> Unit,
    onOpenPdPolicy: () -> Unit,
    onOpenOffer: () -> Unit
) {
    val linkColor = Theme.colors.purple700
    val form = state.formState

    Scaffold(
        containerColor = Theme.colors.white900
    ) { paddings ->
        Column(
            modifier =
                Modifier
                    .padding(paddings)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = stringResource(R.string.registration_title),
                style = Theme.typography.body6,
                color = Theme.colors.black900
            )

            PrimaryTextField(
                title = stringResource(R.string.nickname_title),
                placeholder = stringResource(R.string.nickname_placeholder),
                value = form.nickname,
                onValueChange = {
                    onAction(RegistrationAction.NicknameChanged(it))
                },
                error = form.nicknameError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii)
            )

            PrimaryTextField(
                title = stringResource(R.string.email_title),
                placeholder = stringResource(R.string.email_placeholder),
                value = form.email,
                onValueChange = { onAction(RegistrationAction.EmailChanged(it)) },
                error = form.emailError,
                onFocusChanged = { hasFocus ->
                    onAction(RegistrationAction.EmailFocusChanged(hasFocus))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            PrimaryTextField(
                title = stringResource(R.string.password_title),
                placeholder = stringResource(R.string.password_placeholder),
                value = form.password,
                onValueChange = {
                    onAction(RegistrationAction.PasswordChanged(it))
                },
                error = form.passwordError,
                onFocusChanged = { hasFocus ->
                    onAction(RegistrationAction.PasswordFocusChanged(hasFocus))
                },
                visualTransformation = if (form.isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = rememberVectorPainter(
                    if (form.isPasswordVisible) {
                        Icons.Filled.VisibilityOff
                    } else {
                        Icons.Filled.Visibility
                    }
                ),
                onTrailingIconClick = {
                    onAction(RegistrationAction.TogglePasswordVisible)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            PrimaryTextField(
                title = stringResource(R.string.confirm_password_title),
                placeholder = stringResource(R.string.password_placeholder),
                value = form.confirmPassword,
                onValueChange = {
                    onAction(RegistrationAction.ConfirmPasswordChanged(it))
                },
                error = form.confirmPasswordError,
                onFocusChanged = { hasFocus ->
                    onAction(RegistrationAction.ConfirmPasswordFocusChanged(hasFocus))
                },
                visualTransformation = if (form.isConfirmPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = rememberVectorPainter(
                    if (form.isConfirmPasswordVisible) {
                        Icons.Filled.VisibilityOff
                    } else {
                        Icons.Filled.Visibility
                    }
                ),
                onTrailingIconClick = {
                    onAction(RegistrationAction.ToggleConfirmPasswordVisible)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            ConsentRow(
                checked = form.isPdAccepted,
                onCheckedChange = {
                    onAction(RegistrationAction.PdAcceptedChanged(it))
                },
                text = pdConsentText(linkColor),
                onLinkClicked = { tag -> if (tag == "pd") onOpenPdPolicy() }
            )

            ConsentRow(
                checked = form.isOfferAccepted,
                onCheckedChange = {
                    onAction(RegistrationAction.OfferAcceptedChanged(it))
                },
                text = offerConsentText(linkColor),
                onLinkClicked = { tag -> if (tag == "offer") onOpenOffer() }
            )

            ConsentRow(
                checked = form.isMailingAccepted,
                onCheckedChange = {
                    onAction(RegistrationAction.MailingAcceptedChanged(it))
                },
                text =
                    AnnotatedString(
                        stringResource(R.string.marketing_opt_in_text)
                    ),
                onLinkClicked = {}
            )

            Spacer(Modifier.height(8.dp))

            PrimaryButton(
                onClick = { onAction(RegistrationAction.SubmitClicked) },
                enabled = form.isSubmitEnabled,
                isLoading = state is RegistrationUiState.Loading,
                modifier = Modifier.fillMaxWidth(),
            ) { Text(stringResource(R.string.registration_button)) }
        }
    }
}

@Composable
private fun pdConsentText(linkColor: Color): AnnotatedString = buildAnnotatedString {
    append(stringResource(R.string.pd_consent_prefix))
    pushStringAnnotation(tag = "pd", annotation = "pd")
    pushStyle(SpanStyle(color = linkColor))
    append(stringResource(R.string.pd_consent_link))
    pop()
    pop()
    append(stringResource(R.string.pd_consent_suffix))
}

@Composable
private fun offerConsentText(linkColor: Color): AnnotatedString = buildAnnotatedString {
    append(stringResource(R.string.offer_consent_prefix))
    pushStringAnnotation(tag = "offer", annotation = "offer")
    pushStyle(SpanStyle(color = linkColor))
    append(stringResource(R.string.offer_consent_link))
    pop()
    pop()
}

@Composable
private fun ConsentRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: AnnotatedString,
    onLinkClicked: (tag: String) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors =
                CheckboxDefaults.colors(
                    checkedColor = Theme.colors.purple700,
                    uncheckedColor = Theme.colors.purple200,
                    checkmarkColor = Theme.colors.white900
                )
        )
        Spacer(Modifier.width(8.dp))
        ClickableText(
            text = text,
            style =
                Theme.typography.body1.copy(
                    color = Theme.colors.black900,
                    textDecoration = null
                ),
            onClick = { offset ->
                text.getStringAnnotations(start = offset, end = offset)
                    .firstOrNull()
                    ?.let { onLinkClicked(it.tag) }
                    ?: onCheckedChange(!checked)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    YeaHubTheme {
        RegistrationContent(
            state =
                RegistrationUiState.Content(
                    RegistrationFormState(
                        nickname = "admin",
                        nicknameError = null,
                        email = "admin@mail.ru",
                        emailError = null,
                        password = "1234",
                        passwordError = null,
                        confirmPassword = "1234",
                        confirmPasswordError = null,
                        isPasswordVisible = true,
                        isConfirmPasswordVisible = true,
                        isPdAccepted = true,
                        isOfferAccepted = true,
                        isMailingAccepted = false,
                        isSubmitEnabled = true,
                        isEmailTouched = true,
                        isPasswordTouched = true,
                        isConfirmPasswordTouched = true
                    )
                ),
            onAction = {},
            onOpenPdPolicy = {},
            onOpenOffer = {}
        )
    }
}
