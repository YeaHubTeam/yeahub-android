package ru.yeahub.profile_edit.impl.ui.tabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.collections.immutable.persistentMapOf
import ru.yeahub.core_ui.component.DropDownMenu
import ru.yeahub.core_ui.component.UploadPhotoButton
import ru.yeahub.core_ui.component.textInput.DefaultTextField
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState
import ru.yeahub.profile_edit.impl.presentation.intents.ProfileEditScreenEvent
import ru.yeahub.profile_edit.impl.ui.FieldLabel
import ru.yeahub.profile_edit.impl.ui.SectionTitle
import ru.yeahub.ui.R

private val PERSONAL_INFO_TOP_PADDING = 2.dp
private val PERSONAL_INFO_TOP_SPACER = 10.dp

private val SECTION_TITLE_BOTTOM_SPACER = 6.dp
private val PROFILE_DESCRIPTION_BOTTOM_SPACER = 16.dp
private val AVATAR_BOTTOM_SPACER = 12.dp
private val UPLOAD_BUTTON_BOTTOM_SPACER = 24.dp

private val SECTION_SUBTITLE_BOTTOM_SPACER = 12.dp
private val FIELD_BOTTOM_SPACER = 12.dp
private val LINKS_SECTION_TOP_SPACER = 24.dp
private val LINKS_SECTION_TITLE_BOTTOM_SPACER = 6.dp
private val SOCIAL_LINK_VERTICAL_PADDING = 4.dp

private val TEXT_FIELD_HEIGHT = 52.dp
private val AVATAR_HEIGHT = 263.dp
private val AVATAR_WIDTH = 326.dp
private val REMOVE_PHOTO_BUTTON_HEIGHT = 30.dp

@Composable
fun PersonalInfoContent(
    state: ProfileEditState.PersonalInfoTabState,
    onEvent: (ProfileEditScreenEvent) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(Theme.colors.white900)
            .padding(top = PERSONAL_INFO_TOP_PADDING),
    ) {
        item {
            Spacer(Modifier.padding(PERSONAL_INFO_TOP_SPACER))
            SectionTitle(title = stringResource(R.string.profile_photo))
        }
        item {
            Spacer(Modifier.height(SECTION_TITLE_BOTTOM_SPACER))
            Text(
                text = stringResource(R.string.profile_photo_description),
                style = Theme.typography.body7Alt,
                color = Theme.colors.black900,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        item {
            Spacer(Modifier.height(PROFILE_DESCRIPTION_BOTTOM_SPACER))
        }
        item {
            ProfileAvatarSection(
                avatarUrl = state.avatarUrl,
                onRemoveClick = { onEvent(ProfileEditScreenEvent.ToDo) },
            )
            Spacer(Modifier.height(AVATAR_BOTTOM_SPACER))
        }
        item {
            UploadPhotoButton(
                onClick = { onEvent(ProfileEditScreenEvent.ToDo) },
            )
            Spacer(Modifier.height(UPLOAD_BUTTON_BOTTOM_SPACER))
        }
        item {
            SectionTitle(title = stringResource(R.string.profile_personal_info_title))
            Spacer(Modifier.height(SECTION_TITLE_BOTTOM_SPACER))
            Text(
                text = stringResource(R.string.profile_personal_info_subtitle),
                style = Theme.typography.body7Alt,
                color = Theme.colors.black500,
            )
            Spacer(Modifier.height(SECTION_SUBTITLE_BOTTOM_SPACER))
        }
        item {
            FieldLabel(text = stringResource(R.string.profile_nickname_label))
            DefaultTextField(
                value = state.nickname,
                onValueChange = { onEvent(ProfileEditScreenEvent.ToDo) },
                placeholder = stringResource(R.string.profile_nickname_placeholder),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(TEXT_FIELD_HEIGHT),
                onExpandedChange = {},
            )
            Spacer(Modifier.height(FIELD_BOTTOM_SPACER))
        }
        item {
            FieldLabel(text = stringResource(R.string.profile_specialization_label))
            DropDownMenu(
                placeholder = stringResource(R.string.profile_nickname_placeholder),
                items = state.specializationList,
                selected = state.specialization,
                onSelected = { onEvent(ProfileEditScreenEvent.ToDo) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(TEXT_FIELD_HEIGHT),
            )
            Spacer(Modifier.height(FIELD_BOTTOM_SPACER))
        }
        item {
            FieldLabel(text = stringResource(R.string.profile_email_label))
            DefaultTextField(
                value = state.email,
                onValueChange = { },
                placeholder = stringResource(R.string.profile_nickname_placeholder),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(TEXT_FIELD_HEIGHT),
                onExpandedChange = {},
                readOnly = true,
            )
            Spacer(Modifier.height(FIELD_BOTTOM_SPACER))
        }
        item {
            FieldLabel(text = stringResource(R.string.profile_location_label))
            DefaultTextField(
                value = state.location,
                onValueChange = { onEvent(ProfileEditScreenEvent.ToDo) },
                placeholder = stringResource(R.string.profile_location_placeholder),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(TEXT_FIELD_HEIGHT),
                onExpandedChange = {},
            )
            Spacer(Modifier.height(LINKS_SECTION_TOP_SPACER))
        }
        item {
            SectionTitle(title = stringResource(R.string.profile_links_title))
            Spacer(Modifier.height(LINKS_SECTION_TITLE_BOTTOM_SPACER))
            Text(
                text = stringResource(R.string.profile_links_subtitle),
                style = Theme.typography.body7,
                color = Theme.colors.black500,
            )
            Spacer(Modifier.height(LINKS_SECTION_TITLE_BOTTOM_SPACER))
        }
        ProfileEditState.SocialLinks.entries.forEach { platform ->
            item {
                SocialLinkField(
                    platform = platform,
                    value = state.socialLinksUrlMap[platform].orEmpty(),
                    onValueChange = { onEvent(ProfileEditScreenEvent.ToDo) },
                )
            }
        }
    }
}

@Composable
private fun ProfileAvatarSection(
    avatarUrl: String?,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val avatarModifier = Modifier
            .height(AVATAR_HEIGHT)
            .width(AVATAR_WIDTH)

        if (avatarUrl != null) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = stringResource(R.string.profile_photo),
                modifier = avatarModifier,
                contentScale = ContentScale.Crop,
            )
        } else {
            Image(
                painter = painterResource(R.drawable.profile_edit_placeholder),
                contentDescription = stringResource(R.string.profile_photo),
                modifier = avatarModifier,
                contentScale = ContentScale.Crop,
            )
        }
        TextButton(
            onClick = onRemoveClick,
            modifier = Modifier.height(REMOVE_PHOTO_BUTTON_HEIGHT),
        ) {
            Text(
                text = stringResource(R.string.profile_remove_photo),
                style = Theme.typography.body7Alt,
                color = Theme.colors.red700,
            )
        }
    }
}

@Composable
private fun SocialLinkField(
    platform: ProfileEditState.SocialLinks,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(vertical = SOCIAL_LINK_VERTICAL_PADDING)) {
        Text(
            text = platform.name,
            style = Theme.typography.body3Accent,
            color = Theme.colors.black900,
        )
        Spacer(Modifier.height(SOCIAL_LINK_VERTICAL_PADDING))
        DefaultTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = stringResource(R.string.profile_link_placeholder),
            modifier = Modifier
                .fillMaxWidth()
                .height(TEXT_FIELD_HEIGHT),
            onExpandedChange = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileEditPersonalInfoPreview() {
    val personalInfoState = ProfileEditState.PersonalInfoTabState(
        avatarUrl = null,
        nickname = "John Doe",
        specializationList = emptyList(),
        specialization = "Android Разработчик",
        email = "johndoe@gmail.com",
        location = "Санкт-Петербург",
        socialLinksUrlMap = persistentMapOf(),
    )
    PersonalInfoContent(state = personalInfoState, onEvent = {})
}