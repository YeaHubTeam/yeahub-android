package ru.yeahub.profile.impl.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import ru.yeahub.core_ui.component.ErrorScreen
import ru.yeahub.core_ui.component.SkillButton
import ru.yeahub.core_ui.component.TagContainer
import ru.yeahub.core_ui.example.dynamicPreview.ProvidePreviewCompositionLocals
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.profile.impl.R

private val CARD_CORNER_RADIUS = 12.dp
private val CARD_SHADOW_ELEVATION = 2.dp
private val CARD_CONTENT_PADDING = 16.dp
private val SECTION_BOTTOM_PADDING = 24.dp

private val CONTENT_PADDING_HORIZONTAL = 16.dp
private val CONTENT_PADDING_VERTICAL = 23.dp
private val PROFILE_TITLE_BOTTOM_PADDING = 32.dp

private val SKILL_CHIP_PADDING = PaddingValues(
    start = 12.dp,
    end = 12.dp,
    top = 6.dp,
    bottom = 6.dp
)
private val SKILL_CHIP_IMAGE_SIZE = 20.dp
private val SKILLS_FLOW_SPACING = 8.dp
private val SKILLS_TITLE_SPACING = 12.dp

private val AVATAR_HEIGHT = 263.dp
private val AVATAR_CORNER_RADIUS = 12.dp

private val USER_INFO_TOP_PADDING = 12.dp
private val USER_NICKNAME_TOP_PADDING = 12.dp
private val USER_SPECIALIZATION_TOP_PADDING = 12.dp
private val USER_LOCATION_TOP_PADDING = 4.dp

private const val MAX_CHARS_COLLAPSED = 500
private val ABOUT_ME_SPACER_HEIGHT = 16.dp
private val ABOUT_ME_LINE_HEIGHT = 20.sp
private val ABOUT_ME_BUTTON_TOP_PADDING = 8.dp
private val ARROW_ICON_PADDING = 8.dp

private const val ROTATION_ANGLE_EXPANDED = 180f
private const val ROTATION_ANGLE_COLLAPSED = 0f

@Composable
fun ProfileScreen(
    state: ProfileScreenState
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (state) {
            is ProfileScreenState.Loading -> ProfileScreenLoading()

            is ProfileScreenState.Success -> ProfileContent(
                userData = state.userData
            )

            is ProfileScreenState.Error -> ErrorScreen(
                error = state.throwable.localizedMessage,
                errorText = TextOrResource.Resource(R.string.error_screen_text),
                titleText = TextOrResource.Resource(R.string.error_screen_title_text),
                backText = TextOrResource.Resource(R.string.on_back_button_text),
                unknownErrorText = TextOrResource.Resource(R.string.unknown_error_screen_text),
                onBack = { /* TODO */ }
            )

            ProfileScreenState.Unauthorized -> ErrorScreen(
                error = stringResource(R.string.unauthorized),
                errorText = TextOrResource.Resource(R.string.unauthorized),
                titleText = TextOrResource.Resource(R.string.error_screen_title_text),
                backText = TextOrResource.Resource(R.string.on_back_button_text),
                unknownErrorText = TextOrResource.Resource(R.string.unknown_error_screen_text),
                onBack = { /* TODO */ }
            )

            ProfileScreenState.UserDeleted -> ErrorScreen(
                error = stringResource(R.string.account_deleted),
                errorText = TextOrResource.Resource(R.string.account_deleted),
                titleText = TextOrResource.Resource(R.string.error_screen_title_text),
                backText = TextOrResource.Resource(R.string.on_back_button_text),
                unknownErrorText = TextOrResource.Resource(R.string.unknown_error_screen_text),
                onBack = { /* TODO */ }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileContent(
    userData: UserData
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.black25)
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = CONTENT_PADDING_HORIZONTAL,
                vertical = CONTENT_PADDING_VERTICAL
            )
    ) {
        Text(
            text = stringResource(R.string.profile),
            style = Theme.typography.head5,
            color = Theme.colors.black900,
            modifier = Modifier.padding(bottom = PROFILE_TITLE_BOTTOM_PADDING)
        )

        UserAvatarSection(userData)

        if (userData.aboutMe != null)
            AboutMeSection(userData.aboutMe)

        SkillsSection(userData.skills)
    }
}

@Composable
private fun UserAvatarSection(userData: UserData) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = SECTION_BOTTOM_PADDING),
        color = Theme.colors.white900,
        shape = RoundedCornerShape(CARD_CORNER_RADIUS),
        shadowElevation = CARD_SHADOW_ELEVATION,
    ) {
        Column(
            modifier = Modifier.padding(CARD_CONTENT_PADDING)
        ) {
            UserAvatarImage(userData)

            UserInfoSection(userData)

            SocialNetworksSection(userData)
        }
    }
}

@Composable
private fun UserAvatarImage(userData: UserData) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(AVATAR_HEIGHT)
            .clip(RoundedCornerShape(AVATAR_CORNER_RADIUS))
            .background(Theme.colors.black50),
        contentAlignment = Alignment.Center
    ) {
        if (!userData.avatarUrl.isNullOrBlank()) {
            AsyncImage(
                model = userData.avatarUrl,
                contentDescription = stringResource(R.string.profile_photo),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                error = painterResource(id = R.drawable.surikatik),
                placeholder = painterResource(id = R.drawable.surikatik)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.surikatik),
                contentDescription = stringResource(R.string.profile_photo),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun SocialNetworksSection(userData: UserData) {
    val socialNetworks = userData.socialNetworks

    if (socialNetworks.isNotEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            socialNetworks.forEach { network ->
                SocialNetworkIcon(
                    network = network,
                    onClick = {
                        val url = network.getUrlFromCode()
                        url?.let { /* Открыть браузер */ }
                    }
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}

@Composable
private fun SocialNetworkIcon(
    network: SocialNetwork,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(20.dp)
    ) {
        Icon(
            painter = painterResource(network.getIconRes()),
            contentDescription = network.code,
            tint = Theme.colors.purple700,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun UserInfoSection(userData: UserData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = USER_INFO_TOP_PADDING)
    ) {
        UserRolesSection(userData)

        UserNickname(userData)

        UserSpecialization(userData)

        UserLocation(userData)
    }
}

@Composable
private fun UserRolesSection(userData: UserData) {
    TagContainer(tags = userData.roles)
}

@Composable
private fun UserNickname(userData: UserData) {
    Text(
        text = userData.username,
        style = Theme.typography.body3Strong,
        color = Theme.colors.black900,
        modifier = Modifier.padding(top = USER_NICKNAME_TOP_PADDING)
    )
}

@Composable
private fun UserSpecialization(userData: UserData) {
    if (userData.specialization != null) {
        Text(
            text = userData.specialization,
            style = Theme.typography.body2Strong,
            color = Theme.colors.black900,
            modifier = Modifier.padding(top = USER_SPECIALIZATION_TOP_PADDING)
        )
    }
}

@Composable
private fun UserLocation(userData: UserData) {
    Row(
        modifier = Modifier.padding(top = USER_LOCATION_TOP_PADDING),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${userData.city}, ${userData.country}",
            style = Theme.typography.bodyAccent,
            color = Theme.colors.black700
        )
    }
}

@Composable
private fun AboutMeSection(description: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = SECTION_BOTTOM_PADDING),
        color = Theme.colors.white900,
        shape = RoundedCornerShape(CARD_CORNER_RADIUS),
        shadowElevation = CARD_SHADOW_ELEVATION,
    ) {
        Column(
            modifier = Modifier.padding(CARD_CONTENT_PADDING)
        ) {
            AboutMeTitle()

            Spacer(modifier = Modifier.height(ABOUT_ME_SPACER_HEIGHT))

            AboutMeDescription(description)
        }
    }
}

@Composable
private fun AboutMeTitle() {
    Text(
        text = stringResource(R.string.about_me),
        style = Theme.typography.body3Accent,
        color = Theme.colors.black900
    )
}

@Composable
private fun AboutMeDescription(description: String) {
    var isExpanded by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        if (isExpanded) ROTATION_ANGLE_EXPANDED else ROTATION_ANGLE_COLLAPSED
    )

    val isTextOverflow = description.length > MAX_CHARS_COLLAPSED
    val shouldShowButton = isTextOverflow || isExpanded

    Column(
        modifier = Modifier.animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isExpanded) {
                description
            } else {
                if (isTextOverflow) {
                    description.take(MAX_CHARS_COLLAPSED) + "..."
                } else {
                    description
                }
            },
            style = Theme.typography.body3,
            color = Theme.colors.black700,
            lineHeight = ABOUT_ME_LINE_HEIGHT
        )

        if (shouldShowButton) {
            ExpandCollapseButton(
                isExpanded = isExpanded,
                rotationAngle = rotationAngle,
                onToggle = { isExpanded = !isExpanded }
            )
        }
    }
}

@Composable
private fun ExpandCollapseButton(
    isExpanded: Boolean,
    rotationAngle: Float,
    onToggle: () -> Unit
) {
    TextButton(
        onClick = onToggle,
        modifier = Modifier.padding(top = ABOUT_ME_BUTTON_TOP_PADDING)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isExpanded)
                    stringResource(R.string.collapse)
                else
                    stringResource(R.string.read_in_full),
                style = Theme.typography.body3Strong,
                color = Theme.colors.purple700
            )

            Image(
                painter = painterResource(R.drawable.arrow_vector),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = ARROW_ICON_PADDING)
                    .rotate(rotationAngle)
            )
        }
    }
}

@Composable
private fun SkillsSection(skills: List<String>) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = SECTION_BOTTOM_PADDING),
        color = Theme.colors.white900,
        shape = RoundedCornerShape(CARD_CORNER_RADIUS),
        shadowElevation = CARD_SHADOW_ELEVATION,
    ) {
        Column(
            modifier = Modifier.padding(CARD_CONTENT_PADDING)
        ) {
            SkillsTitle()

            Spacer(modifier = Modifier.height(SKILLS_TITLE_SPACING))

            SkillsList(skills)
        }
    }
}

@Composable
private fun SkillsTitle() {
    Text(
        text = stringResource(R.string.skills),
        style = Theme.typography.body4,
        color = Theme.colors.black900
    )
}

@Composable
private fun SkillsList(skills: List<String>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(SKILLS_FLOW_SPACING)
    ) {
        skills.forEach { skill ->
            SkillChip(skill = skill)
        }
    }
}

@Composable
private fun SkillChip(skill: String) {
    SkillButton(
        enabled = true,
        activeButton = true,
        contentPadding = SKILL_CHIP_PADDING,
        imageLeft = R.drawable.icon_true_button,
        imageSizeLeftWith = SKILL_CHIP_IMAGE_SIZE,
        imageSizeLeftHigh = SKILL_CHIP_IMAGE_SIZE,
        text = skill,
        onClick = {}
    )
}

class ProfileScreenStateParamProvider : PreviewParameterProvider<ProfileScreenState> {

    override val values: Sequence<ProfileScreenState>
        get() = sequenceOf(
            ProfileScreenState.Loading,

            ProfileScreenState.Success(
                userData = UserData(
                    id = "1",
                    username = "john_doe",
                    specialization = "Senior Android Developer",
                    aboutMe = "Опытный Android разработчик с 5+ лет опыта. " +
                            "Специализируюсь на Kotlin, Compose, Clean Architecture. " +
                            "Участвую в open-source проектах и люблю делиться знаниями.",
                    skills = listOf(
                        "Kotlin",
                        "Jetpack Compose",
                        "Clean Architecture",
                        "Coroutines"
                    ),
                    avatarUrl = null,
                    roles = listOf("Кандидат", "Участник сообщества", "Ментор"),
                    country = "Россия",
                    city = "Москва",
                    telegramUsername = "john_doe",
                    socialNetworks = listOf(
                        SocialNetwork("instagram", "john_doe"),
                        SocialNetwork("linkedin", "john-doe-123"),
                        SocialNetwork("github", "johndoe"),
                        SocialNetwork("twitter", "john_doe")
                    )
                )
            ),

            ProfileScreenState.Error(
                throwable = Throwable("Не удалось загрузить данные профиля")
            ),

            ProfileScreenState.Unauthorized,

            ProfileScreenState.UserDeleted
        )
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    heightDp = 800
)
@Composable
fun DynamicPreviewUI() {
    val mockViewModel = viewModelCreator<ProfileViewModel> {
        ProfileViewModel(ProfileScreenMapper())
    }

    val state by mockViewModel.screenState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.black25)
    ) {
        ProvidePreviewCompositionLocals {
            ProfileScreen(
                state = state
            )
        }
    }
}


typealias ViewModelCreator = () -> ViewModel?

class ViewModelFactory(
    private val viewModelCreator: ViewModelCreator = { null },
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModelCreator() as T
}

@Composable
inline fun <reified VM : ViewModel> viewModelCreator(noinline creator: ViewModelCreator): VM =
    viewModel(factory = remember { ViewModelFactory(creator) })

@StaticPreview
@Composable
fun StaticPreviewUI(
    @PreviewParameter(ProfileScreenStateParamProvider::class)
    state: ProfileScreenState
) {
    ProfileScreen(
        state = state
    )
}