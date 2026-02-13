package ru.yeahub.profile_edit.impl.ui.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.component.DropDownMenu
import ru.yeahub.core_ui.component.UploadPhotoButton
import ru.yeahub.core_ui.component.textInput.DefaultTextField
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState
import ru.yeahub.profile_edit.impl.presentation.intents.ProfileEditScreenEvent


@Composable
fun PersonalInfoContent(
    state: ProfileEditState.Loaded,
    onAction: (ProfileEditScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        SectionTitle(title = "Фото профиля")
        Text(
            text = "Ваше фото будет видно всем членам сообщества Yeahub",
            style = Theme.typography.body2,
            color = Theme.colors.black900,
        )
        Spacer(Modifier.height(16.dp))

        ProfileAvatarSection(
            avatarUrl = state.avatarUrl,
            onRemoveClick = { onAction(ProfileEditScreenEvent.ToDo) },

            )

        Spacer(Modifier.height(8.dp))

        UploadPhotoButton(
            onClick = { onAction(ProfileEditScreenEvent.ToDo) },
        )

        Spacer(Modifier.height(20.dp))

        SectionTitle(title = "Личная информация")
        Text(
            text = "Ваша подробная информация",
            style = Theme.typography.body7,
            color = Theme.colors.black500,
        )
        Spacer(Modifier.height(12.dp))

        FieldLabel(text = "Никнейм *")
        DefaultTextField(
            value = state.nickname,
            onValueChange = { onAction(ProfileEditScreenEvent.ToDo) },
            placeholder = "Придумайте никнейм",
            modifier = Modifier.fillMaxWidth(),
            onExpandedChange = {},
        )
        Spacer(Modifier.height(12.dp))

        FieldLabel(text = "IT Специальность *")
        DropDownMenu(
            placeholder = "Граф. дизайнер",
            items = state.specializationList,
            selected = state.specialization,
            onSelected = { onAction(ProfileEditScreenEvent.ToDo) },
            modifier = Modifier.fillMaxWidth(),

        )
        Spacer(Modifier.height(12.dp))

        FieldLabel(text = "Email для связи")
        DefaultTextField(
            value = state.email,
            onValueChange = { },
            placeholder = "Email@gmail.com",
            modifier = Modifier.fillMaxWidth(),
            onExpandedChange = {},
            readOnly = true,
        )
        Spacer(Modifier.height(12.dp))

        FieldLabel(text = "Локация")
        DefaultTextField(
            value = state.location,
            onValueChange = { onAction(ProfileEditScreenEvent.ToDo) },
            placeholder = "Напр. Санкт-Петербург, Россия",
            modifier = Modifier.fillMaxWidth(),
            onExpandedChange = {},
        )

        Spacer(Modifier.height(20.dp))

        SectionTitle(title = "Личные ссылки")
        Text(
            text = "Поделитесь своими профилями в других соц. сетях",
            style = Theme.typography.body7,
            color = Theme.colors.black500,
        )
        Spacer(Modifier.height(12.dp))

        FieldLabel(text = "Платформа")

        ProfileEditState.Loaded.SocialLinks.entries.forEach { platform ->
            SocialLinkField(
                platform = platform,
                value = state.socialLinksUrlMap[platform].orEmpty(),
                onValueChange = { onAction(ProfileEditScreenEvent.ToDo) },
            )
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
//        if (avatarUrl != null) {
//            AsyncImage(
//                model = avatarUrl,
//                contentDescription = "Аватар",
//                modifier = Modifier
//                    .size(96.dp)
//                    .clip(CircleShape),
//                contentScale = ContentScale.Crop,
//            )
//        } else {
//            AsyncImage(
//                model = avatarUrl,
//                contentDescription = "Аватар",
//                modifier = Modifier
//                    .size(96.dp)
//                    .clip(CircleShape),
//                contentScale = ContentScale.Crop,
//            )
//        }
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onRemoveClick) {
            Text(
                text = "Удалить фото",
                style = Theme.typography.body7Alt,
                color = Theme.colors.red700,
            )
        }
    }
}

@Composable
private fun SocialLinkField(
    platform: ProfileEditState.Loaded.SocialLinks,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(vertical = 4.dp)) {
        Text(
            text = platform.name,
            style = Theme.typography.body3Accent,
            color = Theme.colors.black900,
        )
        Spacer(Modifier.height(4.dp))
        DefaultTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = "Ссылка",
            modifier = Modifier.fillMaxWidth(),
            onExpandedChange = {},
        )
    }
}

@Composable
private fun SectionTitle(
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
private fun FieldLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = Theme.typography.body5,
        color = Theme.colors.black900,
        modifier = modifier.padding(bottom = 4.dp),
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileEditPersonalInfoPreview() {
    val personalInfoState = ProfileEditState.Loaded(
        avatarUrl = null,
        nickname = "John Doe",
        specializationList = emptyList(),
        specialization = "Android Разработчик",
        email = "johndoe@gmail.com",
        location = "Санкт-Петербург",
        selectedTab = ProfileEditState.ProfileEditTabs.PersonalInfo,
        socialLinksUrlMap = emptyMap(),
    )
    PersonalInfoContent(state = personalInfoState, onAction = {})
}