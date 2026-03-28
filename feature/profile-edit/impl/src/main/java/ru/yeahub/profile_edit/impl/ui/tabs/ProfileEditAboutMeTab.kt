package ru.yeahub.profile_edit.impl.ui.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.component.textInput.DefaultTextField
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState
import ru.yeahub.profile_edit.impl.presentation.intents.ProfileEditScreenEvent
import ru.yeahub.profile_edit.impl.ui.SECTION_TITLE_BOTTOM_SPACING
import ru.yeahub.profile_edit.impl.ui.SectionHeader
import ru.yeahub.profile_edit.impl.ui.TAB_CONTENT_TOP_PADDING
import ru.yeahub.ui.R

@Composable
fun AboutMeContent(
    state: ProfileEditState.AboutMeTabState,
    onEvent: (ProfileEditScreenEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Theme.colors.white900)
            .padding(top = TAB_CONTENT_TOP_PADDING),
    ) {
        SectionHeader(
            title = stringResource(R.string.about_me_tab),
            description = stringResource(R.string.about_me_tab_label),
        )

        Spacer(Modifier.height(SECTION_TITLE_BOTTOM_SPACING))

        DefaultTextField(
            value = state.aboutMeField,
            placeholder = stringResource(R.string.about_me_tab_placeholder),
            onValueChange = { onEvent(ProfileEditScreenEvent.AboutMeChanged(it)) },
            onExpandedChange = {},
            singleLine = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileEditAboutMePreview() {
    val aboutMeState = ProfileEditState.AboutMeTabState(
        aboutMeField = "Android разработчик с фокусом на Compose и архитектуру. ",
    )

    AboutMeContent(
        state = aboutMeState,
        onEvent = {},
    )
}
