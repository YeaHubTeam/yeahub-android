package ru.yeahub.profile_edit.impl.ui.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.component.textInput.DefaultTextField
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState
import ru.yeahub.profile_edit.impl.presentation.intents.ProfileEditScreenEvent
import ru.yeahub.profile_edit.impl.ui.SectionTitle
import ru.yeahub.ui.R

private val ABOUT_ME_TOP_PADDING = 2.dp
private val ABOUT_ME_TOP_SPACER = 10.dp
private val ABOUT_ME_MIDDLE_SPACER = 5.dp
private val PURPLE_AREA_TEXT_SPACER = 8.dp

@Composable
fun AboutMeContent(
    state: ProfileEditState.AboutMeTabState,
    onEvent: (ProfileEditScreenEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Theme.colors.white900)
            .padding(top = ABOUT_ME_TOP_PADDING),
    ) {
        Spacer(Modifier.padding(ABOUT_ME_TOP_SPACER))
        SectionTitle(title = stringResource(R.string.about_me_tab))

        Spacer(Modifier.padding(ABOUT_ME_MIDDLE_SPACER))
        Text(
            text = stringResource(R.string.about_me_tab_label),
            style = Theme.typography.body7Alt,
            color = Theme.colors.black900,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.padding(PURPLE_AREA_TEXT_SPACER))

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
        onEvent = {}
    )
}
