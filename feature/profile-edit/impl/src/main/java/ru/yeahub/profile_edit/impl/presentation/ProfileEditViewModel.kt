package ru.yeahub.profile_edit.impl.presentation

import android.net.Uri
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
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
import kotlin.coroutines.resume

internal class ProfileEditViewModel(
    private val getProfile: GetProfileUseCase,
    private val saveProfile: SaveProfileUseCase,
    private val uploadAvatar: UploadAvatarUseCase,
    private val deleteAvatar: DeleteAvatarUseCase,
    private val mapper: ProfileEditScreenMapper,
) : BaseViewModel() {

    private var staticData: StaticData = StaticData(
        email = "",
        specializationList = persistentListOf(),
        isSpecializationEditable = false,
        allSkills = persistentListOf(),
    )
    private var initialUserInput: UserInput = UserInput(
        avatarUrl = null,
        nickname = "",
        specialization = "",
        location = "",
        socialLinks = emptyMap(),
        aboutMe = "",
        selectedSkills = persistentListOf(),
        showUnsavedChangesDialog = false,
        throwable = null,
    )
    private val userInputState = MutableStateFlow<UserInput>(initialUserInput)
    private var pendingRetry: (() -> Unit)? = null

    val screenState: StateFlow<ProfileEditState> = flow {
        emit(ProfileEditState.Loading)
        val domainData = getProfile()
        val data = StaticData(
            email = domainData.email,
            specializationList = domainData.specializationList.toPersistentList(),
            isSpecializationEditable = domainData.specialization == null,
            allSkills = domainData.allSkills.toPersistentList(),
        )
        val input = UserInput(
            avatarUrl = domainData.avatarUrl,
            nickname = domainData.nickname,
            specialization = domainData.specialization.orEmpty(),
            location = domainData.location,
            socialLinks = domainData.socialLinks,
            aboutMe = domainData.aboutMe,
            selectedSkills = domainData.selectedSkills.toPersistentList(),
            showUnsavedChangesDialog = false,
            throwable = null,
        )
        initialUserInput = input
        staticData = data
        userInputState.value = input
        emitAll(userInputState.filterNotNull().map { mapper.getScreenState(it, data) })
    }.retryWhen { cause, _ ->
        emit(mapper.getScreenState(cause))
        suspendCancellableCoroutine { cont ->
            pendingRetry = { cont.resume(Unit) }
        }
        true
    }.stateIn(
        scope = viewModelScopeSafe,
        started = SharingStarted.WhileSubscribed(TIME_TO_CLEAN_UP_RESOURCES),
        initialValue = ProfileEditState.Loading,
    )

    private val _commands = MutableSharedFlow<ProfileEditScreenCommand>()
    val commands: SharedFlow<ProfileEditScreenCommand> = _commands.asSharedFlow()

    @Suppress("ComplexMethod")
    fun onEvent(event: ProfileEditScreenEvent) = when (event) {
        is ProfileEditScreenEvent.LoadData -> loadData()
        is ProfileEditScreenEvent.SaveProfile -> onSaveProfile()
        is ProfileEditScreenEvent.BackPressed -> onBackPressed()
        is ProfileEditScreenEvent.DiscardChanges -> emitCommand(ProfileEditScreenCommand.NavigateBack)
        is ProfileEditScreenEvent.UnsavedChangesDialogDismissed ->
            updateInput { copy(showUnsavedChangesDialog = false) }

        is ProfileEditScreenEvent.RetryOperation -> onRetryOperation()
        is ProfileEditScreenEvent.OperationErrorDialogDismissed -> {
            updateInput { copy(throwable = null) }
            pendingRetry = null
        }

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
        userInputState.value = userInputState.value.transform()
    }

    private fun emitCommand(command: ProfileEditScreenCommand) {
        viewModelScopeSafe.launch { _commands.emit(command) }
    }

    private fun onRetryOperation() {
        updateInput { copy(throwable = null) }
        pendingRetry!!.invoke()
        pendingRetry = null
    }

    private fun loadData() {
        val retry = pendingRetry
        pendingRetry = null
        retry!!.invoke()
    }

    private fun onBackPressed() {
        if (userInputChangedExceptAvatarUrl(userInputState.value, initialUserInput)) {
            updateInput { copy(showUnsavedChangesDialog = true) }
        } else {
            emitCommand(ProfileEditScreenCommand.NavigateBack)
        }
    }

    private fun userInputChangedExceptAvatarUrl(
        value: UserInput,
        initialUserInput: UserInput,
    ): Boolean {
        return value.nickname != initialUserInput.nickname ||
                value.specialization != initialUserInput.specialization ||
                value.location != initialUserInput.location ||
                value.socialLinks != initialUserInput.socialLinks ||
                value.aboutMe != initialUserInput.aboutMe ||
                value.selectedSkills != initialUserInput.selectedSkills
    }

    private fun onAddSkill(skillName: String) {
        val skill: DomainProfileEditSkill = staticData.allSkills.find { it.name == skillName }!!
        updateInput { copy(selectedSkills = selectedSkills.add(skill)) }
    }

    private fun onSaveProfile() {
        val loaded = screenState.value as ProfileEditState.Loaded
        if (loaded.hasValidationErrors) return
        val input = userInputState.value
        val data = staticData
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
                .onFailure { throwable ->
                    pendingRetry = ::onSaveProfile
                    updateInput { copy(throwable = throwable) }
                }
        }
    }

    private fun onAvatarSelected(uri: Uri) {
        val previousAvatarUrl = userInputState.value.avatarUrl
        updateInput { copy(avatarUrl = uri.toString()) }
        viewModelScopeSafe.launch {
            runCatching { uploadAvatar(uri) }
                .onSuccess { newUrl -> updateInput { copy(avatarUrl = newUrl) } }
                .onFailure { throwable ->
                    pendingRetry = { onAvatarSelected(uri) }
                    updateInput {
                        copy(
                            avatarUrl = previousAvatarUrl,
                            throwable = throwable,
                        )
                    }
                }
        }
    }

    private fun onDeleteAvatar() {
        val previousAvatarUrl = userInputState.value.avatarUrl
        updateInput { copy(avatarUrl = null) }
        viewModelScopeSafe.launch {
            runCatching { deleteAvatar() }
                .onFailure { throwable ->
                    pendingRetry = ::onDeleteAvatar
                    updateInput {
                        copy(
                            avatarUrl = previousAvatarUrl,
                            throwable = throwable,
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
    val throwable: Throwable?,
)

internal data class StaticData(
    val email: String,
    val specializationList: PersistentList<String>,
    val isSpecializationEditable: Boolean,
    val allSkills: PersistentList<DomainProfileEditSkill>,
)
