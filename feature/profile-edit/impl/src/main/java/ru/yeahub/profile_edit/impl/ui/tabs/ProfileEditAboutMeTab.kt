package ru.yeahub.profile_edit.impl.ui.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.component.PrimaryTextField
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState
import ru.yeahub.profile_edit.impl.presentation.intents.ProfileEditScreenEvent
import ru.yeahub.profile_edit.impl.ui.SECTION_DESCRIPTION_ADDITIONAL_BOTTOM_SPACING
import ru.yeahub.profile_edit.impl.ui.SectionHeader
import ru.yeahub.profile_edit.impl.ui.TAB_CONTENT_TOP_PADDING
import ru.yeahub.ui.R

@Composable
internal fun AboutMeContent(
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

        Spacer(Modifier.height(SECTION_DESCRIPTION_ADDITIONAL_BOTTOM_SPACING))

        PrimaryTextField(
            value = state.aboutMeField,
            placeholder = stringResource(R.string.about_me_tab_placeholder),
            onValueChange = { onEvent(ProfileEditScreenEvent.AboutMeChanged(it)) },
            singleLine = false,
            modifier = Modifier,
            textFieldModifier = Modifier
                .defaultMinSize(minHeight = 180.dp),
        )
    }
}

private class AboutMeTabPreviewProvider :
    PreviewParameterProvider<ProfileEditState.AboutMeTabState> {
    override val values = sequenceOf(
        ProfileEditState.AboutMeTabState(
            aboutMeField = "Android разработчик с фокусом на Compose и архитектуру.",
        ),
        ProfileEditState.AboutMeTabState(
            aboutMeField = "",
        ),
    )
}

@StaticPreview
@Composable
internal fun ProfileEditAboutMePreview(
    @PreviewParameter(AboutMeTabPreviewProvider::class) state: ProfileEditState.AboutMeTabState,
) {
    AboutMeContent(
        state = state,
        onEvent = {},
    )
}
