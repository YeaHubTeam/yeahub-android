package ru.yeahub.profile_edit.impl.presentation

import android.net.Uri
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.yeahub.core_utils.BaseViewModel
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditData
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSocialPlatform
import ru.yeahub.profile_edit.impl.domain.usecase.DeleteAvatarUseCase
import ru.yeahub.profile_edit.impl.domain.usecase.GetProfileUseCase
import ru.yeahub.profile_edit.impl.domain.usecase.SaveProfileUseCase
import ru.yeahub.profile_edit.impl.domain.usecase.UploadAvatarUseCase
import ru.yeahub.profile_edit.impl.presentation.intents.ProfileEditScreenCommand
import ru.yeahub.profile_edit.impl.presentation.intents.ProfileEditScreenEvent
import ru.yeahub.profile_edit.impl.ui.cropper.ImageValidationException
import ru.yeahub.profile_edit.impl.R as ProfileEditR

internal class ProfileEditViewModel(
    private val getProfile: GetProfileUseCase,
    private val saveProfile: SaveProfileUseCase,
    private val uploadAvatar: UploadAvatarUseCase,
    private val deleteAvatar: DeleteAvatarUseCase,
    private val mapper: ProfileEditScreenMapper,
) : BaseViewModel() {

    private var viewModelStaticData: ViewModelStaticData = ViewModelStaticData(
        initialUserInput = UserInput(
            avatarUrl = "",
            nickname = "",
            specialization = "",
            location = "",
            socialLinks = emptyMap(),
            aboutMe = "",
            selectedSkills = persistentListOf(),
        ),
        staticData = StaticDomainData(
            email = "",
            specializationList = persistentListOf(),
            isSpecializationEditable = false,
            allSkills = persistentListOf(),
        ),
    )
    private val mutableState = MutableStateFlow<ProfileEditMutableState>(
        value = ProfileEditMutableState(
            userInput = viewModelStaticData.initialUserInput,
            operationError = null,
            showUnsavedChangesDialog = false,
        ),
    )
    private val loadRetryTrigger = MutableSharedFlow<Unit>(replay = 1).apply { tryEmit(Unit) }
    private var pendingOperationRetry: (() -> Unit)? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val screenState: StateFlow<ProfileEditState> = loadRetryTrigger
        .flatMapLatest {
            flow {
                emit(mapper.getScreenState(ProfileEditMapperInput.Loading))
                val domainData = withContext(Dispatchers.IO) {
                    getProfile()
                }
                viewModelStaticData = ViewModelStaticData(
                    initialUserInput = UserInput(
                        avatarUrl = domainData.avatarUrl,
                        nickname = domainData.nickname,
                        specialization = domainData.specialization.orEmpty(),
                        location = domainData.location,
                        socialLinks = domainData.socialLinks,
                        aboutMe = domainData.aboutMe,
                        selectedSkills = domainData.selectedSkills.toPersistentList(),
                    ),
                    staticData = StaticDomainData(
                        email = domainData.email,
                        specializationList = domainData.specializationList.toPersistentList(),
                        isSpecializationEditable = domainData.specialization == null,
                        allSkills = domainData.allSkills.toPersistentList(),
                    ),
                )
                updateMutableState { copy(userInput = viewModelStaticData.initialUserInput) }
                emitAll(
                    mutableState.filterNotNull().map {
                        mapper.getScreenState(
                            ProfileEditMapperInput.Loaded(
                                mutableState = it,
                                viewModelStaticData,
                            ),
                        )
                    },
                )
            }.catch { cause ->
                emit(mapper.getScreenState(ProfileEditMapperInput.Error(cause)))
            }
        }.stateIn(
            scope = viewModelScopeSafe,
            started = SharingStarted.WhileSubscribed(TIME_TO_CLEAN_UP_RESOURCES),
            initialValue = ProfileEditState.Loading,
        )

    private val _commands = MutableSharedFlow<ProfileEditScreenCommand>()
    val commands: SharedFlow<ProfileEditScreenCommand> = _commands.asSharedFlow()

    @Suppress("ComplexMethod")
    fun onEvent(event: ProfileEditScreenEvent) = when (event) {
        is ProfileEditScreenEvent.RetryPressed -> onRetry()
        is ProfileEditScreenEvent.SaveProfile -> onSaveProfile()
        is ProfileEditScreenEvent.BackPressed -> onBackPressed()
        is ProfileEditScreenEvent.DiscardChanges -> emitCommand(ProfileEditScreenCommand.NavigateBack)
        is ProfileEditScreenEvent.UnsavedChangesDialogDismissed ->
            updateMutableState { copy(showUnsavedChangesDialog = false) }

        is ProfileEditScreenEvent.SnackbarRetryPressed -> onRetrySnackbar()
        is ProfileEditScreenEvent.ErrorSnackbarDismissed -> {
            updateMutableState { copy(operationError = null) }
            pendingOperationRetry = null
        }

        is ProfileEditScreenEvent.UploadAvatar -> emitCommand(ProfileEditScreenCommand.ShowPhotoPicker)
        is ProfileEditScreenEvent.AvatarSelected -> onAvatarSelected(event.uri)
        is ProfileEditScreenEvent.ImageValidationFailed -> handleOperationFailure(
            ImageValidationException(event.error),
            TextOrResource.Resource(ProfileEditR.string.error_action_image_validation),
        ) { emitCommand(ProfileEditScreenCommand.ShowPhotoPicker) }

        is ProfileEditScreenEvent.DeleteAvatar -> onDeleteAvatar()
        is ProfileEditScreenEvent.NicknameChanged -> updateUserInput { copy(nickname = event.nickname) }
        is ProfileEditScreenEvent.SpecializationSelected ->
            updateUserInput { copy(specialization = event.specialization) }

        is ProfileEditScreenEvent.ChangeSpecializationClicked ->
            emitCommand(ProfileEditScreenCommand.ShowCannotChangeSpecializationToast)

        is ProfileEditScreenEvent.LocationChanged -> updateUserInput { copy(location = event.location) }
        is ProfileEditScreenEvent.SocialLinkChanged ->
            updateUserInput { copy(socialLinks = socialLinks + (event.platform to event.url)) }

        is ProfileEditScreenEvent.AboutMeChanged -> updateUserInput { copy(aboutMe = event.text) }
        is ProfileEditScreenEvent.AddSkill -> onAddSkill(event.skillName)
        is ProfileEditScreenEvent.RemoveSkill ->
            updateUserInput { copy(selectedSkills = selectedSkills.remove(event.skill)) }
    }

    private fun updateMutableState(transform: ProfileEditMutableState.() -> ProfileEditMutableState) {
        mutableState.update { it.transform() }
    }

    private fun updateUserInput(transform: UserInput.() -> UserInput) {
        mutableState.update { it.copy(userInput = it.userInput.transform()) }
    }

    private fun emitCommand(command: ProfileEditScreenCommand) {
        viewModelScopeSafe.launch(Dispatchers.IO) { _commands.emit(command) }
    }

    private fun handleOperationFailure(
        throwable: Throwable,
        failedOperationMessage: TextOrResource,
        retry: () -> Unit,
    ) {
        pendingOperationRetry = retry
        updateMutableState {
            copy(
                operationError = OperationError(
                    throwable,
                    failedOperationMessage,
                ),
            )
        }
    }

    private fun onRetrySnackbar() {
        updateMutableState { copy(operationError = null) }
        pendingOperationRetry?.invoke()
        pendingOperationRetry = null
    }

    private fun onRetry() {
        viewModelScopeSafe.launch(Dispatchers.IO) { loadRetryTrigger.emit(Unit) }
    }

    private fun onBackPressed() {
        if (mutableState.value.userInput != viewModelStaticData.initialUserInput) {
            updateMutableState { copy(showUnsavedChangesDialog = true) }
        } else {
            emitCommand(ProfileEditScreenCommand.NavigateBack)
        }
    }

    private fun onAddSkill(skillName: String) {
        val skill: DomainProfileEditSkill =
            viewModelStaticData.staticData.allSkills.find { it.name == skillName }!!
        updateUserInput { copy(selectedSkills = selectedSkills.add(skill)) }
    }

    private fun onSaveProfile() {
        val loaded = screenState.value as ProfileEditState.Loaded
        if (loaded.hasValidationErrors) return
        val input = mutableState.value.userInput
        val data = viewModelStaticData.staticData
        viewModelScopeSafe.launch(Dispatchers.IO) {
            runCatching {
                saveProfile(
                    DomainProfileEditData(
                        email = data.email,
                        avatarUrl = input.avatarUrl,
                        nickname = input.nickname,
                        specialization = input.specialization.takeIf { it.isNotEmpty() },
                        specializationList = data.specializationList,
                        location = input.location,
                        socialLinks = input.socialLinks,
                        aboutMe = input.aboutMe,
                        selectedSkills = input.selectedSkills,
                        allSkills = data.allSkills,
                    ),
                )
            }.onSuccess { emitCommand(ProfileEditScreenCommand.NavigateToProfile) }.onFailure {
                handleOperationFailure(
                    it,
                    TextOrResource.Resource(ProfileEditR.string.error_action_save_profile),
                    ::onSaveProfile,
                )
            }
        }
    }

    private fun onAvatarSelected(uri: Uri) {
        val previousAvatarUrl = mutableState.value.userInput.avatarUrl
        updateUserInput { copy(avatarUrl = uri.toString()) }
        viewModelScopeSafe.launch(Dispatchers.IO) {
            runCatching { uploadAvatar(uri) }.onSuccess { newUrl -> updateUserInput { copy(avatarUrl = newUrl) } }
                .onFailure {
                    updateUserInput { copy(avatarUrl = previousAvatarUrl) }
                    handleOperationFailure(
                        it,
                        TextOrResource.Resource(ProfileEditR.string.error_action_upload_avatar),
                    ) { onAvatarSelected(uri) }
                }
        }
    }

    private fun onDeleteAvatar() {
        val previousAvatarUrl = mutableState.value.userInput.avatarUrl
        updateUserInput { copy(avatarUrl = "") }
        viewModelScopeSafe.launch(Dispatchers.IO) {
            runCatching { deleteAvatar() }.onFailure {
                updateUserInput { copy(avatarUrl = previousAvatarUrl) }
                handleOperationFailure(
                    it,
                    TextOrResource.Resource(ProfileEditR.string.error_action_delete_avatar),
                    ::onDeleteAvatar,
                )
            }
        }
    }
}

private const val TIME_TO_CLEAN_UP_RESOURCES = 5_000L

internal data class OperationError(
    val throwable: Throwable,
    val failedOperationMessage: TextOrResource,
)

internal data class ProfileEditMutableState(
    val userInput: UserInput,
    val operationError: OperationError?,
    val showUnsavedChangesDialog: Boolean,
)

internal data class ViewModelStaticData(
    val initialUserInput: UserInput,
    val staticData: StaticDomainData,
)

internal data class UserInput(
    val avatarUrl: String,
    val nickname: String,
    val specialization: String,
    val location: String,
    val socialLinks: Map<DomainProfileEditSocialPlatform, String>,
    val aboutMe: String,
    val selectedSkills: PersistentList<DomainProfileEditSkill>,
)

internal data class StaticDomainData(
    val email: String?,
    val specializationList: PersistentList<String>,
    val isSpecializationEditable: Boolean,
    val allSkills: PersistentList<DomainProfileEditSkill>,
)
