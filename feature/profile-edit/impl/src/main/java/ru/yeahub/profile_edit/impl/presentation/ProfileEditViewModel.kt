package ru.yeahub.profile_edit.impl.presentation

import android.net.Uri
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.yeahub.core_utils.BaseViewModel
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditData
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSocialPlatform
import ru.yeahub.profile_edit.impl.domain.usecase.DeleteAvatarUseCase
import ru.yeahub.profile_edit.impl.domain.usecase.GetProfileUseCase
import ru.yeahub.profile_edit.impl.domain.usecase.SaveProfileUseCase
import ru.yeahub.profile_edit.impl.domain.usecase.UploadAvatarUseCase
import ru.yeahub.profile_edit.impl.presentation.intents.ProfileEditScreenCommand
import ru.yeahub.profile_edit.impl.presentation.intents.ProfileEditScreenEvent

internal class ProfileEditViewModel(
    private val getProfile: GetProfileUseCase,
    private val saveProfile: SaveProfileUseCase,
    private val uploadAvatar: UploadAvatarUseCase,
    private val deleteAvatar: DeleteAvatarUseCase,
    private val mapper: ProfileEditScreenMapper,
) : BaseViewModel() {

    private val loadSignal =
        MutableSharedFlow<Unit>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val userInputState = MutableStateFlow<UserInput?>(null)
    private var staticData: StaticData? = null
    private var initialUserInput: UserInput? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val screenState: StateFlow<ProfileEditState> = loadSignal
        .flatMapLatest {
            flow {
                emit(ProfileEditState.Loading)
                val domainData = getProfile()
                val data = StaticData(
                    email = domainData.email,
                    specializationList = domainData.specializationList.toPersistentList(),
                    isSpecializationEditable = domainData.specialization == null,
                    allSkills = domainData.allSkills.toPersistentList(),
                )
                staticData = data
                val input = UserInput(
                    avatarUrl = domainData.avatarUrl,
                    nickname = domainData.nickname,
                    specialization = domainData.specialization.orEmpty(),
                    location = domainData.location,
                    socialLinks = domainData.socialLinks,
                    aboutMe = domainData.aboutMe,
                    selectedSkills = domainData.selectedSkills.toPersistentList(),
                    showUnsavedChangesDialog = false,
                )
                initialUserInput = input
                userInputState.value = input
                emitAll(
                    userInputState.mapNotNull { ui ->
                        ui?.let { mapper.getScreenState(it, data) }
                    },
                )
            }.catch { e -> emit(mapper.getScreenState(e)) }
        }
        .stateIn(
            scope = viewModelScopeSafe,
            started = SharingStarted.WhileSubscribed(TIME_TO_CLEAN_UP_RESOURCES),
            initialValue = ProfileEditState.Loading,
        )

    private val _commands = MutableSharedFlow<ProfileEditScreenCommand>()
    val commands: SharedFlow<ProfileEditScreenCommand> = _commands.asSharedFlow()

    @Suppress("ComplexMethod")
    fun onEvent(event: ProfileEditScreenEvent) = when (event) {
        is ProfileEditScreenEvent.LoadData -> onLoadData()
        is ProfileEditScreenEvent.SaveProfile -> onSaveProfile()
        is ProfileEditScreenEvent.BackPressed -> onBackPressed()
        is ProfileEditScreenEvent.DiscardChanges -> emitCommand(ProfileEditScreenCommand.NavigateBack)
        is ProfileEditScreenEvent.UnsavedChangesDialogDismissed ->
            updateInput { copy(showUnsavedChangesDialog = false) }

        is ProfileEditScreenEvent.UploadAvatar -> emitCommand(ProfileEditScreenCommand.ShowPhotoPicker)
        is ProfileEditScreenEvent.AvatarSelected -> onAvatarSelected(event.uri)
        is ProfileEditScreenEvent.DeleteAvatar -> onDeleteAvatar()
        is ProfileEditScreenEvent.NicknameChanged -> updateInput { copy(nickname = event.nickname) }
        is ProfileEditScreenEvent.SpecializationSelected ->
            updateInput { copy(specialization = event.specialization) }

        is ProfileEditScreenEvent.ChangeSpecializationClicked ->
            emitCommand(ProfileEditScreenCommand.ShowCannotChangeSpecializationToast)

        is ProfileEditScreenEvent.LocationChanged -> updateInput { copy(location = event.location) }
        is ProfileEditScreenEvent.SocialLinkChanged ->
            updateInput { copy(socialLinks = socialLinks + (event.platform to event.url)) }

        is ProfileEditScreenEvent.AboutMeChanged -> updateInput { copy(aboutMe = event.text) }
        is ProfileEditScreenEvent.AddSkill -> onAddSkill(event.skillName)
        is ProfileEditScreenEvent.RemoveSkill ->
            updateInput { copy(selectedSkills = selectedSkills.remove(event.skill)) }
    }

    private fun updateInput(transform: UserInput.() -> UserInput) {
        userInputState.update { it?.transform() }
    }

    private fun emitCommand(command: ProfileEditScreenCommand) {
        viewModelScopeSafe.launch { _commands.emit(command) }
    }

    private fun onLoadData() {
        val retryAction = userInputState.value?.retryAction
        if (retryAction != null) {
            retryAction()
        } else {
            loadData()
        }
    }

    private fun loadData() {
        loadSignal.tryEmit(Unit)
    }

    private fun onBackPressed() {
        if (userInputState.value?.pendingError != null) {
            updateInput { copy(pendingError = null, retryAction = null) }
            return
        }
        if (userInputState.value != initialUserInput) {
            updateInput { copy(showUnsavedChangesDialog = true) }
        } else {
            emitCommand(ProfileEditScreenCommand.NavigateBack)
        }
    }

    private fun onAddSkill(skillName: String) {
        val skill = staticData?.allSkills?.find { it.name == skillName } ?: return
        updateInput { copy(selectedSkills = selectedSkills.add(skill)) }
    }

    private fun onSaveProfile() {
        val loaded = screenState.value as? ProfileEditState.Loaded ?: return
        if (loaded.hasValidationErrors) return
        updateInput { copy(pendingError = null, retryAction = null) }
        val input = userInputState.value ?: return
        val data = staticData ?: return
        viewModelScopeSafe.launch {
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
            }
                .onSuccess { emitCommand(ProfileEditScreenCommand.NavigateToProfile) }
                .onFailure { e ->
                    updateInput {
                        copy(
                            pendingError = e,
                            retryAction = ::onSaveProfile,
                        )
                    }
                }
        }
    }

    private fun onAvatarSelected(uri: Uri) {
        val previousAvatarUrl = userInputState.value?.avatarUrl
        updateInput { copy(avatarUrl = uri.toString(), pendingError = null, retryAction = null) }
        viewModelScopeSafe.launch {
            runCatching { uploadAvatar(uri) }
                .onSuccess { newUrl -> updateInput { copy(avatarUrl = newUrl) } }
                .onFailure { e ->
                    updateInput {
                        copy(
                            avatarUrl = previousAvatarUrl,
                            pendingError = e,
                            retryAction = { onAvatarSelected(uri) },
                        )
                    }
                }
        }
    }

    private fun onDeleteAvatar() {
        val previousAvatarUrl = userInputState.value?.avatarUrl
        updateInput { copy(avatarUrl = null, pendingError = null, retryAction = null) }
        viewModelScopeSafe.launch {
            runCatching { deleteAvatar() }
                .onFailure { e ->
                    updateInput {
                        copy(
                            avatarUrl = previousAvatarUrl,
                            pendingError = e,
                            retryAction = ::onDeleteAvatar,
                        )
                    }
                }
        }
    }
}

private const val TIME_TO_CLEAN_UP_RESOURCES = 5_000L

internal data class UserInput(
    val avatarUrl: String?,
    val nickname: String,
    val specialization: String,
    val location: String,
    val socialLinks: Map<DomainProfileEditSocialPlatform, String>,
    val aboutMe: String,
    val selectedSkills: PersistentList<DomainProfileEditSkill>,
    val showUnsavedChangesDialog: Boolean,
    val pendingError: Throwable? = null,
    val retryAction: (() -> Unit)? = null,
)

internal data class StaticData(
    val email: String,
    val specializationList: PersistentList<String>,
    val isSpecializationEditable: Boolean,
    val allSkills: PersistentList<DomainProfileEditSkill>,
)
