package ru.yeahub.profile_edit.impl.ui

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import ru.yeahub.core_ui.component.CoreTopTabs
import ru.yeahub.core_ui.component.ErrorScreen
import ru.yeahub.core_ui.component.PrimaryButton
import ru.yeahub.core_ui.component.TopAppBarWithBottomBorder
import ru.yeahub.core_ui.component.UnsavedChangesDialog
import ru.yeahub.core_ui.example.dynamicPreview.ProvidePreviewCompositionLocals
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditData
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSocialPlatform
import ru.yeahub.profile_edit.impl.domain.usecase.GetProfileUseCase
import ru.yeahub.profile_edit.impl.domain.usecase.SaveProfileUseCase
import ru.yeahub.profile_edit.impl.domain.usecase.UploadAvatarUseCase
import ru.yeahub.profile_edit.impl.presentation.ProfileEditScreenMapper
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState.ProfileEditTabs
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState.ProfileEditTabs.AboutMe
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState.ProfileEditTabs.PersonalInfo
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState.ProfileEditTabs.Skills
import ru.yeahub.profile_edit.impl.presentation.ProfileEditViewModel
import ru.yeahub.profile_edit.impl.presentation.intents.ProfileEditScreenCommand
import ru.yeahub.profile_edit.impl.presentation.intents.ProfileEditScreenEvent
import ru.yeahub.profile_edit.impl.presentation.intents.ProfileEditScreenResult
import ru.yeahub.profile_edit.impl.presentation.profileEditViewModelCreator
import ru.yeahub.profile_edit.impl.ui.tabs.AboutMeContent
import ru.yeahub.profile_edit.impl.ui.tabs.PersonalInfoContent
import ru.yeahub.profile_edit.impl.ui.tabs.SkillsContent
import ru.yeahub.ui.R
import ru.yeahub.profile_edit.impl.R as ProfileEditR

@Composable
internal fun ProfileEditScreen(
    state: ProfileEditState,
    onEvent: (ProfileEditScreenEvent) -> Unit,
) {
    BackHandler {
        onEvent(ProfileEditScreenEvent.BackPressed)
    }
    Scaffold(
        containerColor = Theme.colors.black10,
        topBar = {
            TopAppBarWithBottomBorder(
                title = TextOrResource.Resource(ProfileEditR.string.profile_edit_title),
                onBackClick = { onEvent(ProfileEditScreenEvent.BackPressed) },
            )
        },
        bottomBar = {
            if (state is ProfileEditState.Loaded) {
                PrimaryButton(
                    onClick = { onEvent(ProfileEditScreenEvent.SaveProfile) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                ) {
                    Text(text = stringResource(R.string.save))
                }
            }
        },
    ) { paddingValues ->
        when (state) {
            is ProfileEditState.Loading -> ProfileEditLoadingScreen(paddingValues)
            is ProfileEditState.Loaded -> ProfileEditContent(
                state = state,
                onEvent = onEvent,
                paddingValues = paddingValues,
            )

            is ProfileEditState.Error -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                ErrorScreen(
                    error = state.throwable.message,
                    onBack = { onEvent(ProfileEditScreenEvent.LoadData) },
                    errorText = TextOrResource.Resource(R.string.error_screen_text),
                    titleText = TextOrResource.Resource(R.string.error_screen_title_text),
                    backText = TextOrResource.Resource(R.string.retry),
                    unknownErrorText = TextOrResource.Resource(R.string.unknown_error_screen_text),
                )
            }
        }
    }
}

@Composable
internal fun HandleCommands(
    commands: SharedFlow<ProfileEditScreenCommand>,
    onResult: (ProfileEditScreenResult) -> Unit,
) {
    LaunchedEffect(Unit) {
        commands.collect { command ->
            when (command) {
                is ProfileEditScreenCommand.NavigateBack ->
                    onResult(ProfileEditScreenResult.NavigateBack)

                is ProfileEditScreenCommand.NavigateToProfile ->
                    onResult(ProfileEditScreenResult.NavigateToProfile)

                is ProfileEditScreenCommand.ShowPhotoPicker -> { /* TODO */
                }

                is ProfileEditScreenCommand.ShowError -> { /* TODO */
                }

                is ProfileEditScreenCommand.ShowCannotChangeSpecializationToast -> { /* TODO */
                }
            }
        }
    }
}

@Composable
private fun ProfileEditContent(
    state: ProfileEditState.Loaded,
    onEvent: (ProfileEditScreenEvent) -> Unit,
    paddingValues: PaddingValues,
) {
    val tabTitles = remember {
        ProfileEditTabs.entries.map { tab ->
            when (tab) {
                PersonalInfo -> R.string.profile_personal_information
                AboutMe -> R.string.profile_about_me
                Skills -> R.string.profile_skills
            }
        }
    }
    val pagerState = rememberPagerState(
        pageCount = { ProfileEditTabs.entries.size },
    )
    val coroutineScope = rememberCoroutineScope()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        color = Theme.colors.white900,
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 16.dp,
                    vertical = 16.dp,
                ),
        ) {
            CoreTopTabs(
                selectedIndex = pagerState.currentPage,
                onSelected = { index ->
                    coroutineScope.launch { pagerState.animateScrollToPage(index) }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),
                tabs = tabTitles.map { stringResource(it) },
                edgePadding = (-16).dp,
                indicatorHeight = 10.dp,
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) { page ->
                key(page) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        when (ProfileEditTabs.entries[page]) {
                            PersonalInfo -> PersonalInfoContent(
                                state = state.personalInfoState,
                                onEvent = onEvent,
                            )

                            AboutMe -> AboutMeContent(
                                state = state.aboutMeTabState,
                                onEvent = onEvent,
                            )

                            Skills -> SkillsContent(
                                state = state.skillsTabState,
                                onEvent = onEvent,
                            )
                        }
                    }
                }
            }
        }
    }

    if (state.showUnsavedChangesDialog) {
        UnsavedChangesDialog(
            onLeave = { onEvent(ProfileEditScreenEvent.DiscardChanges) },
            onStay = { onEvent(ProfileEditScreenEvent.UnsavedChangesDialogDismissed) },
        )
    }
}

@Preview
@Composable
fun ProfileEditPreview() {
    val screenState = ProfileEditState.Loaded(
        personalInfoState = ProfileEditState.PersonalInfoTabState(
            avatarUrl = null,
            nickname = ProfileEditState.ValidatedField(value = "Joe", error = null),
            specializationList = persistentListOf(
                "Android разработчик",
                "iOS разработчик",
                "Backend разработчик",
                "Frontend разработчик",
            ),
            specialization = "",
            isSpecializationEditable = true,
            email = "johndoe@gmail.com",
            location = ProfileEditState.ValidatedField("Санкт-Петербург", null),
            socialLinks = persistentMapOf(
                Pair(
                    DomainProfileEditSocialPlatform.LinkedIn,
                    ProfileEditState.ValidatedField(
                        "",
                        TextOrResource.Resource(R.string.error_max_length_255),
                    ),
                ),
            ),
        ),
        aboutMeTabState = ProfileEditState.AboutMeTabState(aboutMeField = ""),
        skillsTabState = ProfileEditState.SkillsTabState(
            listOfSkills = persistentListOf(
                DomainProfileEditSkill(imageRes = R.drawable.icon_true_button, name = "Kotlin"),
                DomainProfileEditSkill(
                    imageRes = R.drawable.icon_true_button,
                    name = "Jetpack Compose",
                ),
                DomainProfileEditSkill(imageRes = R.drawable.icon_true_button, name = "Coroutines"),
            ),
            listOfChosenSkills = persistentListOf(
                DomainProfileEditSkill(imageRes = R.drawable.icon_true_button, name = "Figma"),
                DomainProfileEditSkill(imageRes = R.drawable.icon_true_button, name = "Wireframe"),
            ),
        ),
        showUnsavedChangesDialog = false,
    )

    var state by remember { mutableStateOf(screenState) }

    ProfileEditScreen(
        state = state,
        onEvent = { event ->
            when (event) {
                is ProfileEditScreenEvent.SpecializationSelected -> {
                    state = state.copy(
                        personalInfoState = state.personalInfoState.copy(
                            specialization = event.specialization,
                        ),
                    )
                }

                is ProfileEditScreenEvent.AboutMeChanged -> {
                    state =
                        state.copy(aboutMeTabState = state.aboutMeTabState.copy(aboutMeField = event.text))
                }

                else -> {}
            }
        },
    )
}

@Preview
@Composable
fun ProfileEditWithDialogPreview() {
    val screenState = ProfileEditState.Loaded(
        personalInfoState = ProfileEditState.PersonalInfoTabState(
            avatarUrl = null,
            nickname = ProfileEditState.ValidatedField("John Doe", null),
            specializationList = persistentListOf(),
            specialization = "Android Разработчик",
            isSpecializationEditable = false,
            email = "johndoe@gmail.com",
            location = ProfileEditState.ValidatedField("Санкт-Петербург", null),
            socialLinks = persistentMapOf(),
        ),
        aboutMeTabState = ProfileEditState.AboutMeTabState(aboutMeField = ""),
        skillsTabState = ProfileEditState.SkillsTabState(
            listOfSkills = persistentListOf(),
            listOfChosenSkills = persistentListOf(),
        ),
        showUnsavedChangesDialog = true,
    )

    ProfileEditScreen(
        state = screenState,
        onEvent = {},
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileEditErrorPreview() {
    ProfileEditScreen(
        state = ProfileEditState.Error(Throwable("Не удалось загрузить данные")),
        onEvent = {},
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileEditLoadingPreview() {
    ProfileEditScreen(
        state = ProfileEditState.Loading,
        onEvent = {},
    )
}

@Preview(showBackground = true)
@Composable
internal fun ProfileEditScreenDynamicPreview() {
    val mockGetProfile = object : GetProfileUseCase {
        override suspend fun invoke(): DomainProfileEditData {
            delay(DYNAMIC_PREVIEW_LOAD_DELAY)
            return DomainProfileEditData(
                email = "johndoe@gmail.com",
                avatarUrl = null,
                nickname = "JohnDoe",
                specialization = null,
                specializationList = listOf(
                    "Android разработчик",
                    "iOS разработчик",
                    "Backend разработчик",
                    "Frontend разработчик",
                ),
                location = "Санкт-Петербург",
                socialLinks = mapOf(
                    DomainProfileEditSocialPlatform.LinkedIn to "linkedin.com/in/johndoe",
                    DomainProfileEditSocialPlatform.Telegram to "t.me/johndoe",
                ),
                aboutMe = "Android разработчик с фокусом на Compose и архитектуру.",
                selectedSkills = listOf(
                    DomainProfileEditSkill(imageRes = R.drawable.icon_true_button, name = "Kotlin"),
                    DomainProfileEditSkill(
                        imageRes = R.drawable.icon_true_button,
                        name = "Jetpack Compose",
                    ),
                ),
                allSkills = listOf(
                    DomainProfileEditSkill(imageRes = R.drawable.icon_true_button, name = "Kotlin"),
                    DomainProfileEditSkill(
                        imageRes = R.drawable.icon_true_button,
                        name = "Jetpack Compose",
                    ),
                    DomainProfileEditSkill(
                        imageRes = R.drawable.icon_true_button,
                        name = "Coroutines",
                    ),
                    DomainProfileEditSkill(imageRes = R.drawable.icon_true_button, name = "Git"),
                    DomainProfileEditSkill(imageRes = R.drawable.icon_true_button, name = "Java"),
                ),
            )
        }
    }
    val mockSaveProfile = object : SaveProfileUseCase {
        override suspend fun invoke(profile: DomainProfileEditData) = Unit
    }
    val mockUploadAvatar = object : UploadAvatarUseCase {
        override suspend fun invoke(uri: Uri): String = uri.toString()
    }

    val mockViewModel: ProfileEditViewModel = profileEditViewModelCreator {
        ProfileEditViewModel(
            mapper = ProfileEditScreenMapper(),
            getProfile = mockGetProfile,
            saveProfile = mockSaveProfile,
            uploadAvatar = mockUploadAvatar,
        )
    }

    val state by mockViewModel.screenState.collectAsState()

    LaunchedEffect(Unit) {
        mockViewModel.onEvent(ProfileEditScreenEvent.LoadData)
    }

    ProvidePreviewCompositionLocals {
        ProfileEditScreen(
            state = state,
            onEvent = mockViewModel::onEvent,
        )
    }
}

private const val DYNAMIC_PREVIEW_LOAD_DELAY = 1_500L
