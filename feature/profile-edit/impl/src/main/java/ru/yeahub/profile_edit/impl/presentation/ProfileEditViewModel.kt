package ru.yeahub.profile_edit.impl.presentation

import android.net.Uri
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.yeahub.core_utils.BaseViewModel
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditData
import ru.yeahub.profile_edit.impl.domain.usecase.GetProfileUseCase
import ru.yeahub.profile_edit.impl.domain.usecase.SaveProfileUseCase
import ru.yeahub.profile_edit.impl.domain.usecase.UploadAvatarUseCase
import ru.yeahub.profile_edit.impl.presentation.intents.ProfileEditScreenCommand
import ru.yeahub.profile_edit.impl.presentation.intents.ProfileEditScreenEvent
import ru.yeahub.ui.R

internal class ProfileEditViewModel(
    private val mapper: ProfileEditScreenMapper,
    private val getProfile: GetProfileUseCase,
    private val saveProfile: SaveProfileUseCase,
    private val uploadAvatar: UploadAvatarUseCase,
) : BaseViewModel() {

    private val userInputState = MutableStateFlow<Result<ProfileEditUserInput>?>(null)
    private var staticData: ProfileEditStaticData? = null
    private var initialUserInput: ProfileEditUserInput? = null

    val screenState: StateFlow<ProfileEditState> = userInputState
        .map { mapper.getScreenState(it, staticData) }
        .stateIn(
            scope = viewModelScopeSafe,
            started = SharingStarted.WhileSubscribed(TIME_TO_CLEAN_UP_RESOURCES),
            initialValue = ProfileEditState.Loading,
        )

    private val _commands = MutableSharedFlow<ProfileEditScreenCommand>()
    val commands: SharedFlow<ProfileEditScreenCommand> = _commands.asSharedFlow()

    fun onEvent(event: ProfileEditScreenEvent) = when (event) {
        is ProfileEditScreenEvent.LoadData -> loadData()
        is ProfileEditScreenEvent.SaveProfile -> onSaveProfile()
        is ProfileEditScreenEvent.BackPressed -> onBackPressed()
        is ProfileEditScreenEvent.DiscardChanges -> emitCommand(ProfileEditScreenCommand.NavigateBack)
        is ProfileEditScreenEvent.UnsavedChangesDialogDismissed ->
            updateInput { copy(showUnsavedChangesDialog = false) }

        is ProfileEditScreenEvent.UploadAvatar -> emitCommand(ProfileEditScreenCommand.ShowPhotoPicker)
        is ProfileEditScreenEvent.AvatarSelected -> onAvatarSelected(event.uri)
        is ProfileEditScreenEvent.DeleteAvatar -> updateInput { copy(avatarUrl = null) }
        is ProfileEditScreenEvent.NicknameChanged -> updateInput { copy(nickname = event.nickname) }
        is ProfileEditScreenEvent.ChooseSpecialization ->
            updateInput { copy(specialization = event.specialization) }

        is ProfileEditScreenEvent.CannotChangeSpecializationToast ->
            emitCommand(ProfileEditScreenCommand.ShowCannotChangeSpecializationToast)

        is ProfileEditScreenEvent.LocationChanged -> updateInput { copy(location = event.location) }
        is ProfileEditScreenEvent.SocialLinkChanged ->
            updateInput { copy(socialLinks = socialLinks + (event.link to event.url)) }

        is ProfileEditScreenEvent.AboutMeChanged -> updateInput { copy(aboutMe = event.text) }
        is ProfileEditScreenEvent.AddSkill -> onAddSkill(event.skillName)
        is ProfileEditScreenEvent.RemoveSkill ->
            updateInput { copy(chosenSkills = chosenSkills.remove(event.skill)) }
    }

    private fun updateInput(transform: ProfileEditUserInput.() -> ProfileEditUserInput) {
        userInputState.update { it?.map { input -> input.transform() } }
    }

    private fun emitCommand(command: ProfileEditScreenCommand) {
        viewModelScopeSafe.launch { _commands.emit(command) }
    }

    private fun onBackPressed() {
        if (userInputState.value?.getOrNull() != initialUserInput) {
            updateInput { copy(showUnsavedChangesDialog = true) }
        } else {
            emitCommand(ProfileEditScreenCommand.NavigateBack)
        }
    }

    private fun onAddSkill(skillName: String) {
        val skill = staticData?.allSkills?.find { it.name == skillName } ?: return
        updateInput { copy(chosenSkills = chosenSkills.add(skill)) }
    }

    private fun loadData() {
        viewModelScopeSafe.launch {
            val result = runCatching { getProfile() }
            result.onSuccess { domainData ->
                staticData = ProfileEditStaticData(
                    email = domainData.email,
                    specializationList = domainData.specializationList.toPersistentList(),
                    isSpecializationEditable = domainData.specialization == null,
                    allSkills = domainData.allSkills.toPersistentList(),
                )
                val input = ProfileEditUserInput(
                    avatarUrl = domainData.avatarUrl,
                    nickname = domainData.nickname,
                    specialization = domainData.specialization.orEmpty(),
                    location = domainData.location,
                    socialLinks = domainData.socialLinks,
                    aboutMe = domainData.aboutMe,
                    chosenSkills = domainData.chosenSkills.toPersistentList(),
                    showUnsavedChangesDialog = false,
                )
                initialUserInput = input
                userInputState.value = Result.success(input)
            }
            result.onFailure { userInputState.value = Result.failure(it) }
        }
    }

    private fun onSaveProfile() {
        val input = userInputState.value?.getOrNull() ?: return
        val data = staticData ?: return
        viewModelScopeSafe.launch {
            val result = runCatching {
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
                        chosenSkills = input.chosenSkills,
                        allSkills = data.allSkills,
                    ),
                )
            }
            result.onSuccess { emitCommand(ProfileEditScreenCommand.NavigateToProfile) }
            result.onFailure {
                emitCommand(ProfileEditScreenCommand.ShowError(TextOrResource.Resource(R.string.error_screen_text)))
            }
        }
    }

    private fun onAvatarSelected(uri: Uri) {
        updateInput { copy(avatarUrl = uri.toString()) }
        viewModelScopeSafe.launch {
            val result = runCatching { uploadAvatar(uri) }
            result.onSuccess { avatarUrl -> updateInput { copy(avatarUrl = avatarUrl) } }
            result.onFailure {
                emitCommand(ProfileEditScreenCommand.ShowError(TextOrResource.Resource(R.string.error_screen_text)))
            }
        }
    }
}

private const val TIME_TO_CLEAN_UP_RESOURCES = 5_000L
