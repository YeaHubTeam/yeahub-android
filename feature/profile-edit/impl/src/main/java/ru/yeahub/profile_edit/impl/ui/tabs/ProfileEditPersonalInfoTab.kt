package ru.yeahub.profile_edit.impl.ui.tabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState.SocialLinks
import ru.yeahub.profile_edit.impl.presentation.intents.ProfileEditScreenEvent
import ru.yeahub.profile_edit.impl.ui.FieldLabel
import ru.yeahub.profile_edit.impl.ui.SectionTitle
import ru.yeahub.ui.R

private val PHOTO_SECTION_TOP_SPACER = 23.dp

private val SECTION_TITLE_BOTTOM_SPACER = 6.dp
private val PROFILE_DESCRIPTION_BOTTOM_SPACER = 18.dp
private val AVATAR_BOTTOM_SPACER = 18.dp
private val UPLOAD_BUTTON_BOTTOM_SPACER = 24.dp

private val SECTION_SUBTITLE_BOTTOM_SPACER = 12.dp
private val FIELD_BOTTOM_SPACER = 12.dp
private val LINKS_SECTION_TOP_SPACER = 8.dp
private val SPECIALIZATION_SPACER = 8.dp
private val LINKS_SECTION_TITLE_BOTTOM_SPACER = 6.dp
private val SOCIAL_LINK_VERTICAL_PADDING = 4.dp

private val TEXT_FIELD_HEIGHT = 52.dp
private val AVATAR_HEIGHT = 263.dp
private val AVATAR_WIDTH = 326.dp
private val REMOVE_PHOTO_BUTTON_HEIGHT = 25.dp
private val REMOVE_PHOTO_BUTTON_PADDING = 8.dp
private val VALIDATION_ERROR_HEIGHT = 16.dp

@Composable
fun PersonalInfoContent(
    state: ProfileEditState.PersonalInfoTabState,
    onEvent: (ProfileEditScreenEvent) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(Theme.colors.white900),
    ) {
        item {
            Spacer(Modifier.height(PHOTO_SECTION_TOP_SPACER))
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
                onRemoveClick = { onEvent(ProfileEditScreenEvent.DeleteAvatar) },
            )
            Spacer(Modifier.height(AVATAR_BOTTOM_SPACER))
        }
        item {
            UploadPhotoButton(
                onClick = { onEvent(ProfileEditScreenEvent.UploadAvatar) },
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
            ValidatedTextField(
                field = state.nickname,
                onValueChange = { onEvent(ProfileEditScreenEvent.OnNicknameChanged(it)) },
                placeholder = stringResource(R.string.profile_nickname_placeholder),
            )
        }
        item {
            FieldLabel(text = stringResource(R.string.profile_specialization_label))
            DropDownMenu(
                placeholder = stringResource(R.string.profile_specialization_placeholder),
                items = state.specializationList,
                selected = state.specialization,
                onSelected = { onEvent(ProfileEditScreenEvent.ChooseSpecialization(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(TEXT_FIELD_HEIGHT),
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.icon_search),
                        contentDescription = "Поиск",
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp),
                    )
                },
                isEnabled = state.isSpecializationEditable,
            )
            Spacer(Modifier.height(SPECIALIZATION_SPACER))
            Text(
                text = stringResource(R.string.profile_change_specialization),
                style = Theme.typography.head7,
                color = Theme.colors.purple700,
                modifier = Modifier.clickable {
                    onEvent(ProfileEditScreenEvent.CannotChangeSpecializationToast)
                },
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
                isEnabled = false,
            )
            Spacer(Modifier.height(FIELD_BOTTOM_SPACER))
        }
        item {
            FieldLabel(text = stringResource(R.string.profile_location_label))
            ValidatedTextField(
                field = state.location,
                onValueChange = { onEvent(ProfileEditScreenEvent.OnLocationChanged(it)) },
                placeholder = stringResource(R.string.profile_location_placeholder),
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
        SocialLinks.entries.forEach { platform ->
            item {
                SocialLinkField(
                    platform = platform,
                    field = state.socialLinks[platform]
                        ?: ProfileEditState.ValidatedField("", null),
                    onValueChange = {
                        onEvent(
                            ProfileEditScreenEvent.OnSocialLinkChanged(
                                link = platform,
                                url = it,
                            ),
                        )
                    },
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
        Text(
            text = stringResource(R.string.profile_remove_photo),
            style = Theme.typography.body7Alt,
            color = Theme.colors.red700,
            modifier = Modifier
                .height(REMOVE_PHOTO_BUTTON_HEIGHT)
                .padding(top = REMOVE_PHOTO_BUTTON_PADDING)
                .clickable(onClick = onRemoveClick),
        )
    }
}

@Composable
private fun SocialLinkField(
    platform: SocialLinks,
    field: ProfileEditState.ValidatedField,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = platform.name,
            style = Theme.typography.body3Accent,
            color = Theme.colors.black900,
        )
        Spacer(Modifier.height(SOCIAL_LINK_VERTICAL_PADDING))
        ValidatedTextField(
            field = field,
            onValueChange = onValueChange,
            placeholder = stringResource(R.string.profile_link_placeholder),
        )
    }
}

@Composable
private fun ValidatedTextField(
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
            .height(TEXT_FIELD_HEIGHT),
        onExpandedChange = {},
        isError = field.error != null,
    )
    ValidationErrorText(error = field.error)
}

@Composable
private fun ValidationErrorText(error: TextOrResource?) {
    Box(modifier = Modifier.height(VALIDATION_ERROR_HEIGHT)) {
        error?.let {
            Text(
                text = it.getString(LocalContext.current),
                style = Theme.typography.body7Alt,
                color = Theme.colors.red700,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileEditPersonalInfoPreview() {
    val personalInfoState = ProfileEditState.PersonalInfoTabState(
        avatarUrl = null,
        nickname = ProfileEditState.ValidatedField("John Doe", null),
        specializationList = emptyList(),
        specialization = "Android Разработчик",
        isSpecializationEditable = false,
        email = "johndoe@gmail.com",
        location = ProfileEditState.ValidatedField("Санкт-Петербург", null),
        socialLinks = persistentMapOf(
            Pair(
                SocialLinks.Linkedin,
                ProfileEditState.ValidatedField(
                    "",
                    TextOrResource.Resource(R.string.error_max_length_255),
                ),
            ),
        ),
    )
    PersonalInfoContent(state = personalInfoState, onEvent = {})
}