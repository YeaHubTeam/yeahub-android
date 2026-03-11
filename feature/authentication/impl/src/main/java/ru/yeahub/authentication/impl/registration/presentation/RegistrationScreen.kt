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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.yeahub.authentication.impl.R
import ru.yeahub.core_ui.component.PrimaryButton
import ru.yeahub.core_ui.theme.Theme

@Composable
fun RegistrationScreen(
        state: RegistrationUiState,
        onAction: (RegistrationAction) -> Unit,
        onOpenPdPolicy: () -> Unit,
        onOpenOffer: () -> Unit
) {
        val linkColor = MaterialTheme.colorScheme.primary

        Scaffold { paddings ->
                Column(
                        modifier =
                                Modifier.padding(paddings)
                                        .fillMaxSize()
                                        .verticalScroll(rememberScrollState())
                                        .padding(horizontal = 16.dp, vertical = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                        Text(
                                text = stringResource(R.string.registration_title),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.SemiBold
                        )

                        FormTextField(
                                title = stringResource(R.string.nickname_title),
                                placeholder = stringResource(R.string.nickname_placeholder),
                                value = state.nickname,
                                onValueChange = {
                                        onAction(RegistrationAction.NicknameChanged(it))
                                },
                                keyboardType = KeyboardType.Ascii
                        )

                        FormTextField(
                                title = stringResource(R.string.email_title),
                                placeholder = stringResource(R.string.email_placeholder),
                                value = state.email,
                                onValueChange = { onAction(RegistrationAction.EmailChanged(it)) },
                                keyboardType = KeyboardType.Email
                        )

                        FormPasswordField(
                                title = stringResource(R.string.password_title),
                                placeholder = stringResource(R.string.password_placeholder),
                                value = state.password,
                                isVisible = state.isPasswordVisible,
                                onValueChange = {
                                        onAction(RegistrationAction.PasswordChanged(it))
                                },
                                onToggleVisibility = {
                                        onAction(RegistrationAction.TogglePasswordVisible)
                                }
                        )

                        FormPasswordField(
                                title = stringResource(R.string.confirm_password_title),
                                placeholder = stringResource(R.string.password_placeholder),
                                value = state.confirmPassword,
                                isVisible = state.isConfirmPasswordVisible,
                                onValueChange = {
                                        onAction(RegistrationAction.ConfirmPasswordChanged(it))
                                },
                                onToggleVisibility = {
                                        onAction(RegistrationAction.ToggleConfirmPasswordVisible)
                                }
                        )

                        ConsentRow(
                                checked = state.isPdAccepted,
                                onCheckedChange = {
                                        onAction(RegistrationAction.PdAcceptedChanged(it))
                                },
                                text = pdConsentText(linkColor),
                                onLinkClicked = { tag -> if (tag == "pd") onOpenPdPolicy() }
                        )

                        ConsentRow(
                                checked = state.isOfferAccepted,
                                onCheckedChange = {
                                        onAction(RegistrationAction.OfferAcceptedChanged(it))
                                },
                                text = offerConsentText(linkColor),
                                onLinkClicked = { tag -> if (tag == "offer") onOpenOffer() }
                        )

                        ConsentRow(
                                checked = state.isMailingAccepted,
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
                                enabled = state.isSubmitEnabled,
                                modifier = Modifier.fillMaxWidth(),
                        ) { Text(stringResource(R.string.registration_button)) }
                }
        }
}

@Composable
private fun FormTextField(
        title: String,
        placeholder: String,
        value: String,
        onValueChange: (String) -> Unit,
        keyboardType: KeyboardType,
        modifier: Modifier = Modifier,
) {
        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = title)

                OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = value,
                        onValueChange = onValueChange,
                        placeholder = {
                                Text(
                                        text = placeholder,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                        colors =
                                OutlinedTextFieldDefaults.colors(
                                        focusedTextColor =
                                                MaterialTheme.colorScheme.onSurfaceVariant,
                                        unfocusedTextColor =
                                                MaterialTheme.colorScheme.onSurfaceVariant
                                )
                )
        }
}

@Composable
private fun FormPasswordField(
        title: String,
        placeholder: String,
        value: String,
        isVisible: Boolean,
        onValueChange: (String) -> Unit,
        onToggleVisibility: () -> Unit,
        modifier: Modifier = Modifier,
) {
        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = title)

                PasswordField(
                        placeholder = placeholder,
                        value = value,
                        isVisible = isVisible,
                        onValueChange = onValueChange,
                        onToggleVisibility = onToggleVisibility
                )
        }
}

@Composable
private fun PasswordField(
        placeholder: String,
        value: String,
        isVisible: Boolean,
        onValueChange: (String) -> Unit,
        onToggleVisibility: () -> Unit,
        modifier: Modifier = Modifier,
) {
        OutlinedTextField(
                modifier = modifier.fillMaxWidth(),
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                        Text(text = placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant)
                },
                singleLine = true,
                visualTransformation =
                        if (isVisible) {
                                VisualTransformation.None
                        } else {
                                PasswordVisualTransformation()
                        },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                        IconButton(onClick = onToggleVisibility) {
                                Icon(
                                        imageVector =
                                                if (isVisible) {
                                                        Icons.Filled.VisibilityOff
                                                } else {
                                                        Icons.Filled.Visibility
                                                },
                                        contentDescription = null
                                )
                        }
                },
                colors =
                        OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
        )
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
                                MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurface,
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
        MaterialTheme {
                RegistrationScreen(
                        state =
                                RegistrationUiState(
                                        nickname = "admin",
                                        email = "admin@mail.ru",
                                        password = "1234",
                                        confirmPassword = "1234",
                                        isPasswordVisible = true,
                                        isConfirmPasswordVisible = true,
                                        isPdAccepted = true,
                                        isOfferAccepted = true,
                                        isMailingAccepted = false,
                                        isSubmitEnabled = true,
                                        isLoading = false,
                                        error = null
                                ),
                        onAction = {},
                        onOpenPdPolicy = {},
                        onOpenOffer = {}
                )
        }
}
