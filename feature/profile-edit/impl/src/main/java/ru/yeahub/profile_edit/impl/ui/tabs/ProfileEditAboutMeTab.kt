package ru.yeahub.profile_edit.impl.ui.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.component.textInput.TextInputColorsDefaults
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState
import ru.yeahub.profile_edit.impl.ui.SectionTitle
import ru.yeahub.ui.R

private val ABOUT_ME_TOP_PADDING = 2.dp
private val ABOUT_ME_TOP_SPACER = 10.dp
private val ABOUT_ME_MIDDLE_SPACER = 5.dp
private val PURPLE_AREA_TEXT_SPACER = 8.dp

@Composable
fun AboutMeContent(
    state: ProfileEditState.AboutMeTabState
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(Theme.colors.white900)
            .padding(top = ABOUT_ME_TOP_PADDING),
    ) {
        item {
            Spacer(Modifier.padding(ABOUT_ME_TOP_SPACER))
            SectionTitle(title = stringResource(R.string.about_me_tab))

            Spacer(Modifier.padding(ABOUT_ME_MIDDLE_SPACER))
            Text(
                text = stringResource(R.string.about_me_tab_label),
                style = Theme.typography.body7Alt,
                color = Theme.colors.black900,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        item {
            Spacer(Modifier.padding(PURPLE_AREA_TEXT_SPACER))
            PurpleTextArea(
                value = state.aboutMeField,
                onValueChange = { /* TODO */ }
            )
        }
    }
}

@Composable
fun PurpleTextArea(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp),
        shape = RoundedCornerShape(20.dp),
        placeholder = {
             Text("Android разработчик с фокусом на Compose и архитектуру.")
        },
        maxLines = Int.MAX_VALUE,
        singleLine = false,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = TextInputColorsDefaults.activeBorder(),
            unfocusedBorderColor = TextInputColorsDefaults.defaultsBorder(),
            disabledBorderColor = TextInputColorsDefaults.defaultsBorder(),
            cursorColor = TextInputColorsDefaults.activeBorder()
        ),
        textStyle = Theme.typography.body3
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileEditAboutMePreview() {
    val aboutMeState = ProfileEditState.AboutMeTabState(
        aboutMeField = "Android разработчик с фокусом на Compose и архитектуру. ",
    )

    AboutMeContent(state = aboutMeState)
}
