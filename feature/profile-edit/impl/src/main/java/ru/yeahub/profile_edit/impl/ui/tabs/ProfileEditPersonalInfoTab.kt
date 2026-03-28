package ru.yeahub.profile_edit.impl.ui.tabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import ru.yeahub.core_ui.component.DropDownMenu
import ru.yeahub.core_ui.component.UploadPhotoButton
import ru.yeahub.core_ui.component.textInput.DefaultTextField
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState.SocialLinks
import ru.yeahub.profile_edit.impl.presentation.intents.ProfileEditScreenEvent
import ru.yeahub.profile_edit.impl.ui.LabelWithField
import ru.yeahub.profile_edit.impl.ui.SECTION_TITLE_BOTTOM_SPACING
import ru.yeahub.profile_edit.impl.ui.SectionHeader
import ru.yeahub.profile_edit.impl.ui.TAB_CONTENT_TOP_PADDING
import ru.yeahub.profile_edit.impl.ui.ValidatedTextField
import ru.yeahub.ui.R

private val SECTION_BOTTOM_SPACING = 18.dp
private val UPLOAD_BUTTON_BOTTOM_SPACING = 24.dp
private val FIELD_GROUP_SPACING = 12.dp
private val AVATAR_HEIGHT = 263.dp
private val AVATAR_WIDTH = 326.dp
private val REMOVE_PHOTO_TEXT_HEIGHT = 25.dp
private val REMOVE_PHOTO_TEXT_PADDING = 8.dp

@Composable
fun PersonalInfoContent(
    state: ProfileEditState.PersonalInfoTabState,
    onEvent: (ProfileEditScreenEvent) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(Theme.colors.white900),
        contentPadding = PaddingValues(top = TAB_CONTENT_TOP_PADDING),
    ) {
        item {
            SectionHeader(
                title = stringResource(R.string.profile_photo),
                description = stringResource(R.string.profile_photo_description),
            )
            Spacer(Modifier.height(SECTION_TITLE_BOTTOM_SPACING))
        }
        item {
            ProfileAvatarSection(
                avatarUrl = state.avatarUrl,
                onRemoveClick = { onEvent(ProfileEditScreenEvent.DeleteAvatar) },
            )
            Spacer(Modifier.height(SECTION_BOTTOM_SPACING))
            UploadPhotoButton(
                onClick = { onEvent(ProfileEditScreenEvent.UploadAvatar) },
            )
            Spacer(Modifier.height(UPLOAD_BUTTON_BOTTOM_SPACING))
        }
        item {
            SectionHeader(
                title = stringResource(R.string.profile_personal_info_title),
                description = stringResource(R.string.profile_personal_info_subtitle),
            )
            LabelWithField(label = stringResource(R.string.profile_nickname_label)) {
                ValidatedTextField(
                    field = state.nickname,
                    onValueChange = { onEvent(ProfileEditScreenEvent.NicknameChanged(it)) },
                    placeholder = stringResource(R.string.profile_nickname_placeholder),
                )
            }
        }
        item {
            LabelWithField(label = stringResource(R.string.profile_specialization_label)) {
                DropDownMenu(
                    placeholder = stringResource(R.string.profile_specialization_placeholder),
                    items = state.specializationList,
                    selected = state.specialization,
                    onSelected = { onEvent(ProfileEditScreenEvent.ChooseSpecialization(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.icon_search),
                            contentDescription = "Поиск",
                            modifier = Modifier.size(20.dp),
                        )
                    },
                    isEnabled = state.isSpecializationEditable,
                )
            }
            Text(
                text = stringResource(R.string.profile_change_specialization),
                style = Theme.typography.head7,
                color = Theme.colors.purple700,
                modifier = Modifier.clickable {
                    onEvent(ProfileEditScreenEvent.CannotChangeSpecializationToast)
                },
            )
            Spacer(Modifier.height(FIELD_GROUP_SPACING))
        }
        item {
            LabelWithField(label = stringResource(R.string.profile_email_label)) {
                DefaultTextField(
                    value = state.email,
                    onValueChange = { },
                    placeholder = stringResource(R.string.profile_nickname_placeholder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    onExpandedChange = {},
                    readOnly = true,
                    isEnabled = false,
                )
            }
        }
        item {
            LabelWithField(label = stringResource(R.string.profile_location_label)) {
                ValidatedTextField(
                    field = state.location,
                    onValueChange = { onEvent(ProfileEditScreenEvent.LocationChanged(it)) },
                    placeholder = stringResource(R.string.profile_location_placeholder),
                )
            }
            Spacer(Modifier.height(FIELD_GROUP_SPACING))
            SectionHeader(
                title = stringResource(R.string.profile_links_title),
                description = stringResource(R.string.profile_links_subtitle),
            )
        }
        SocialLinks.entries.forEach { platform ->
            item {
                LabelWithField(label = platform.name) {
                    ValidatedTextField(
                        field = state.socialLinks[platform]
                            ?: ProfileEditState.ValidatedField("", null),
                        onValueChange = {
                            onEvent(
                                ProfileEditScreenEvent.SocialLinkChanged(
                                    link = platform,
                                    url = it,
                                ),
                            )
                        },
                        placeholder = stringResource(R.string.profile_link_placeholder),
                    )
                }
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
                .height(REMOVE_PHOTO_TEXT_HEIGHT)
                .padding(top = REMOVE_PHOTO_TEXT_PADDING)
                .clickable(onClick = onRemoveClick),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileEditPersonalInfoPreview() {
    val personalInfoState = ProfileEditState.PersonalInfoTabState(
        avatarUrl = null,
        nickname = ProfileEditState.ValidatedField("John Doe", null),
        specializationList = persistentListOf(),
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