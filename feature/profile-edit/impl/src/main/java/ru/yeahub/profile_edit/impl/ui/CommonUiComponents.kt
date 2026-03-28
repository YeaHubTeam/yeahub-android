package ru.yeahub.profile_edit.impl.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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

internal val SECTION_TITLE_BOTTOM_SPACING = 6.dp
internal val TAB_CONTENT_TOP_PADDING = 22.dp
private val SECTION_DESCRIPTION_BOTTOM_SPACING = 12.dp

@Composable
internal fun SectionHeader(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        SectionTitle(title = title)
        Spacer(Modifier.height(SECTION_TITLE_BOTTOM_SPACING))
        Text(
            text = description,
            style = Theme.typography.body7Alt,
            color = Theme.colors.black900,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(SECTION_DESCRIPTION_BOTTOM_SPACING))
    }
}

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
internal fun LabelWithField(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = Theme.typography.body7,
            color = Theme.colors.black900,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        content()
        Spacer(Modifier.height(8.dp))
    }
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
    if (field.error != null) {
        ValidationErrorText(error = field.error)
    }
}

@Composable
private fun ValidationErrorText(error: TextOrResource) {
    Text(
        text = error.getString(LocalContext.current),
        style = Theme.typography.body1,
        color = Theme.colors.red700,
    )
}