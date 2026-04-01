package ru.yeahub.profile_edit.impl.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.theme.Theme

internal val SECTION_DESCRIPTION_ADDITIONAL_BOTTOM_SPACING = 4.dp
internal val TAB_CONTENT_TOP_PADDING = 22.dp
private val SECTION_DESCRIPTION_BOTTOM_SPACING = 12.dp
private val SECTION_TITLE_BOTTOM_SPACING = 6.dp


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
