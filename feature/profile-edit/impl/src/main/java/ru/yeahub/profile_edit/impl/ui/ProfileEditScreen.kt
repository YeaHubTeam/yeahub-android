package ru.yeahub.profile_edit.impl.ui

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody.Companion.toResponseBody
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import retrofit2.HttpException
import retrofit2.Response
import ru.yeahub.core_ui.component.CoreTopTabs
import ru.yeahub.core_ui.component.ErrorScreen
import ru.yeahub.core_ui.component.PrimaryButton
import ru.yeahub.core_ui.component.TopAppBarWithBottomBorder
import ru.yeahub.core_ui.component.YeahubAlertDialog
import ru.yeahub.core_ui.component.YeahubSnackbar
import ru.yeahub.core_ui.example.dynamicPreview.DynamicPreview
import ru.yeahub.core_ui.example.dynamicPreview.ProvideDynamicPreview
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditData
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSocialPlatform
import ru.yeahub.profile_edit.impl.domain.usecase.DeleteAvatarUseCase
import ru.yeahub.profile_edit.impl.domain.usecase.GetProfileUseCase
import ru.yeahub.profile_edit.impl.domain.usecase.SaveProfileUseCase
import ru.yeahub.profile_edit.impl.domain.usecase.UploadAvatarUseCase
import ru.yeahub.profile_edit.impl.presentation.ProfileEditImageValidationError
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
import ru.yeahub.profile_edit.impl.ui.cropper.CropBottomSheet
import ru.yeahub.profile_edit.impl.ui.cropper.ImageReadResult
import ru.yeahub.profile_edit.impl.ui.cropper.readImageBytesAndValidate
import ru.yeahub.profile_edit.impl.ui.tabs.AboutMeContent
import ru.yeahub.profile_edit.impl.ui.tabs.PersonalInfoContent
import ru.yeahub.profile_edit.impl.ui.tabs.SkillsContent
import ru.yeahub.ui.R
import java.io.IOException
import ru.yeahub.profile_edit.impl.R as ProfileEditR

@Composable
fun ProfileEditScreen(
    state: ProfileEditState,
    commands: SharedFlow<ProfileEditScreenCommand>,
    onResult: (ProfileEditScreenResult) -> Unit,
    onEvent: (ProfileEditScreenEvent) -> Unit,
) {
    ProfileEditScreenUI(state, onEvent)
    HandleCommands(commands, onResult, onEvent)
}

@Composable
internal fun ProfileEditScreenUI(
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
        snackbarHost = {
            if (state is ProfileEditState.Loaded && state.snackbarState != null) {
                val context = LocalContext.current
                val snackbar = state.snackbarState
                YeahubSnackbar(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    title = snackbar.actionMessage.getString(context),
                    description = snackbar.errorMessage.getString(context),
                    buttonText = stringResource(R.string.repeat),
                    onButtonClick = { onEvent(ProfileEditScreenEvent.SnackbarRetryPressed) },
                    onDismissIconClick = { onEvent(ProfileEditScreenEvent.ErrorSnackbarDismissed) },
                )
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
                    error = null,
                    onBack = { onEvent(ProfileEditScreenEvent.RetryPressed) },
                    errorText = TextOrResource.Resource(R.string.error_screen_text),
                    titleText = TextOrResource.Resource(R.string.error_screen_title_text),
                    backText = TextOrResource.Resource(R.string.try_again),
                    unknownErrorText = state.message,
                )
            }
        }
    }
}

@Composable
internal fun HandleCommands(
    commands: SharedFlow<ProfileEditScreenCommand>,
    onResult: (ProfileEditScreenResult) -> Unit,
    onEvent: (ProfileEditScreenEvent) -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var cropSheetUri by remember { mutableStateOf<Uri?>(null) }

    val pickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri ->
        if (uri != null) {
            cropSheetUri = uri
        }
    }

    if (cropSheetUri != null) {
        CropBottomSheet(
            sourceUri = cropSheetUri!!,
            onCropped = { croppedUri ->
                cropSheetUri = null
                coroutineScope.launch {
                    when (val result = croppedUri.readImageBytesAndValidate(context)) {
                        is ImageReadResult.Success -> {
                            onEvent(
                                ProfileEditScreenEvent.AvatarSelected(
                                    previewUrl = croppedUri.toString(),
                                    avatarBytes = result.avatarBytes,
                                ),
                            )
                        }

                        is ImageReadResult.Error -> {
                            onEvent(ProfileEditScreenEvent.ImageValidationFailed(result.error))
                        }
                    }
                }
            },
            onCropFailure = {
                cropSheetUri = null
                onEvent(
                    ProfileEditScreenEvent.ImageValidationFailed(
                        ProfileEditImageValidationError.CropFailed,
                    ),
                )
            },
            onChangePhoto = {
                cropSheetUri = null
                onEvent(ProfileEditScreenEvent.UploadAvatar)
            },
            onDismiss = { cropSheetUri = null },
        )
    }

    LaunchedEffect(commands) {
        commands.collect { command ->
            when (command) {
                is ProfileEditScreenCommand.NavigateBack -> onResult(ProfileEditScreenResult.NavigateBack)

                is ProfileEditScreenCommand.NavigateToProfile -> onResult(ProfileEditScreenResult.NavigateToProfile)

                is ProfileEditScreenCommand.ShowPhotoPicker -> pickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                )

                is ProfileEditScreenCommand.ShowCannotChangeSpecializationToast -> Toast.makeText(
                    context,
                    context.getString(ProfileEditR.string.profile_edit_cannot_change_specialization),
                    Toast.LENGTH_SHORT,
                ).show()
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
        YeahubAlertDialog(
            onDismissRequest = { onEvent(ProfileEditScreenEvent.UnsavedChangesDialogDismissed) },
            titleText = stringResource(R.string.confirm_action),
            descriptionText = stringResource(R.string.unsaved_changes_description),
            leftButtonText = stringResource(R.string.yes),
            rightButtonText = stringResource(R.string.no),
            onLeftButtonClick = { onEvent(ProfileEditScreenEvent.DiscardChanges) },
            onRightButtonClick = { onEvent(ProfileEditScreenEvent.UnsavedChangesDialogDismissed) },
        )
    }
}

private class ProfileEditScreenPreviewProvider : PreviewParameterProvider<ProfileEditState> {
    override val values = sequenceOf(
        ProfileEditState.Loaded(
            personalInfoState = ProfileEditState.PersonalInfoTabState(
                avatarUrl = "",
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
                    DomainProfileEditSocialPlatform.LinkedIn to ProfileEditState.ValidatedField(
                        value = "",
                        error = TextOrResource.Resource(R.string.error_max_length_255),
                    ),
                ),
            ),
            aboutMeTabState = ProfileEditState.AboutMeTabState(aboutMeField = ""),
            skillsTabState = ProfileEditState.SkillsTabState(
                listOfSkills = persistentListOf(
                    DomainProfileEditSkill(imageUrl = "", name = "Kotlin"),
                    DomainProfileEditSkill(imageUrl = "", name = "Jetpack Compose"),
                    DomainProfileEditSkill(imageUrl = "", name = "Coroutines"),
                ),
                listOfChosenSkills = persistentListOf(
                    DomainProfileEditSkill(imageUrl = "", name = "Figma"),
                    DomainProfileEditSkill(imageUrl = "", name = "Wireframe"),
                ),
            ),
            showUnsavedChangesDialog = false,
            hasValidationErrors = true,
            snackbarState = null,
        ),
        ProfileEditState.Loaded(
            personalInfoState = ProfileEditState.PersonalInfoTabState(
                avatarUrl = "",
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
            snackbarState = null,
            hasValidationErrors = false,
        ),
        ProfileEditState.Loaded(
            personalInfoState = ProfileEditState.PersonalInfoTabState(
                avatarUrl = "",
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
            showUnsavedChangesDialog = false,
            snackbarState = ProfileEditState.SnackbarState(
                actionMessage = TextOrResource.Text("При сохранении произошла ошибка"),
                errorMessage = TextOrResource.Text("Нет интернета"),
            ),
            hasValidationErrors = false,
        ),
        ProfileEditState.Error(TextOrResource.Text("Не удалось загрузить данные")),
        ProfileEditState.Loading,
    )
}

@StaticPreview
@Composable
internal fun ProfileEditScreenUIPreview(
    @PreviewParameter(ProfileEditScreenPreviewProvider::class) state: ProfileEditState,
) {
    ProfileEditScreenUI(
        state = state,
        onEvent = {},
    )
}

@DynamicPreview
@Composable
internal fun ProfileEditScreenDynamicPreview() {
    ProvideDynamicPreview(
        moduleDeclaration = {
            single<GetProfileUseCase> {
                object : GetProfileUseCase {
                    private var numberOfCalls = 0
                    override suspend fun invoke(): DomainProfileEditData {
                        delay(DYNAMIC_PREVIEW_LOAD_DELAY)
                        numberOfCalls++
                        if (numberOfCalls < 2) throw IOException()
                        return DomainProfileEditData(
                            email = "johndoe@gmail.com",
                            avatarUrl = "",
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
                                DomainProfileEditSkill(imageUrl = "", name = "Kotlin"),
                                DomainProfileEditSkill(imageUrl = "", name = "Jetpack Compose"),
                            ),
                            allSkills = listOf(
                                DomainProfileEditSkill(imageUrl = "", name = "Kotlin"),
                                DomainProfileEditSkill(imageUrl = "", name = "Jetpack Compose"),
                                DomainProfileEditSkill(imageUrl = "", name = "Coroutines"),
                                DomainProfileEditSkill(imageUrl = "", name = "Git"),
                                DomainProfileEditSkill(imageUrl = "", name = "Java"),
                            ),
                        )
                    }
                }
            }
            single<SaveProfileUseCase> {
                object : SaveProfileUseCase {
                    override suspend fun invoke(profile: DomainProfileEditData) {
                        throw HttpException(Response.error<Any>(484, "".toResponseBody(null)))
                    }
                }
            }
            single<UploadAvatarUseCase> {
                object : UploadAvatarUseCase {
                    private var firstCall = true
                    override suspend fun invoke(avatarBytes: ByteArray) {
                        if (firstCall) {
                            firstCall = false
                            throw IOException("Unauthorized")
                        }
                    }
                }
            }
            single<DeleteAvatarUseCase> {
                object : DeleteAvatarUseCase {
                    private var firstCall = true
                    override suspend fun invoke() {
                        if (firstCall) {
                            firstCall = false
                            throw HttpException(Response.error<Any>(401, "".toResponseBody()))
                        }
                    }
                }
            }
            single { ProfileEditScreenMapper() }
            viewModel {
                ProfileEditViewModel(
                    getProfile = get(),
                    saveProfile = get(),
                    uploadAvatar = get(),
                    deleteAvatar = get(),
                    mapper = get(),
                )
            }
        },
        content = {
            val vm = koinViewModel<ProfileEditViewModel>()
            val state by vm.screenState.collectAsState()
            ProfileEditScreen(
                state = state,
                commands = vm.commands,
                onResult = {},
                onEvent = vm::onEvent,
            )
        },
    )
}

private const val DYNAMIC_PREVIEW_LOAD_DELAY = 1L
