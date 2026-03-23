package ru.yeahub.profile_edit.impl.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.component.textInput.DefaultTextField
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState

@Composable
internal fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = Theme.typography.head7,
        color = Theme.colors.black900,
        modifier = modifier,
    )
}

@Composable
internal fun FieldLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = Theme.typography.body7,
        color = Theme.colors.black900,
        modifier = modifier.padding(bottom = 4.dp),
    )
}

@Composable
internal fun ValidatedTextField(
    field: ProfileEditState.ValidatedField,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    DefaultTextField(
        value = field.value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        onExpandedChange = {},
        isError = field.error != null,
    )
    Box(modifier = Modifier.height(16.dp)) {
        if (field.error != null) {
            ValidationErrorText(error = field.error)
        }
    }
}

@Composable
private fun ValidationErrorText(error: TextOrResource) {
    Text(
        text = error.getString(LocalContext.current),
        style = Theme.typography.body7Alt,
        color = Theme.colors.red700,
    )
}