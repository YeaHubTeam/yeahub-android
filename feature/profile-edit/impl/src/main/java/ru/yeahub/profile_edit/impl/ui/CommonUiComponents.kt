package ru.yeahub.profile_edit.impl.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.theme.Theme

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