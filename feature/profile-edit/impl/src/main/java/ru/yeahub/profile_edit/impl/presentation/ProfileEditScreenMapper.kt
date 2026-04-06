package ru.yeahub.profile_edit.impl.presentation

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentMap
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.ui.R

internal class ProfileEditScreenMapper {

    fun getScreenState(
        mutableState: ProfileEditMutableState,
        viewModelStaticData: ViewModelStaticData,
    ): ProfileEditState {
        val nicknameField = validateNickname(mutableState.userInput.nickname)
        val locationField = validateMaxLength(mutableState.userInput.location)
        val socialLinksFields =
            mutableState.userInput.socialLinks.entries.associate { (platform, url) ->
                platform to validateMaxLength(url)
            }.toPersistentMap()
        val validatedFields = listOf(nicknameField, locationField) + socialLinksFields.values
        return ProfileEditState.Loaded(
            personalInfoState = ProfileEditState.PersonalInfoTabState(
                avatarUrl = mutableState.userInput.avatarUrl,
                nickname = nicknameField,
                specializationList = viewModelStaticData.staticData.specializationList,
                specialization = mutableState.userInput.specialization,
                isSpecializationEditable = viewModelStaticData.staticData.isSpecializationEditable,
                email = viewModelStaticData.staticData.email,
                location = locationField,
                socialLinks = socialLinksFields,
            ),
            aboutMeTabState = mapAboutMeState(mutableState.userInput.aboutMe),
            skillsTabState = mapSkillsState(
                allSkills = viewModelStaticData.staticData.allSkills,
                chosenSkills = mutableState.userInput.selectedSkills,
            ),
            showUnsavedChangesDialog = mutableState.showUnsavedChangesDialog,
            snackbarState = mapSnackbarState(mutableState.throwable),
            hasValidationErrors = validatedFields.any { it.error != null },
        )
    }

    fun getScreenState(e: Throwable): ProfileEditState = ProfileEditState.Error(e)

    private fun mapSnackbarState(throwable: Throwable?): ProfileEditState.SnackbarState? {
        if (throwable == null) return null
        return ProfileEditState.SnackbarState(
            message = TextOrResource.Resource(R.string.error_screen_text),
            throwableMessage = throwable.localizedMessage ?: throwable.toString(),
        )
    }

    private fun mapAboutMeState(
        aboutMe: String,
    ): ProfileEditState.AboutMeTabState = ProfileEditState.AboutMeTabState(
        aboutMeField = aboutMe,
    )

    private fun mapSkillsState(
        allSkills: PersistentList<DomainProfileEditSkill>,
        chosenSkills: PersistentList<DomainProfileEditSkill>,
    ): ProfileEditState.SkillsTabState = ProfileEditState.SkillsTabState(
        listOfSkills = allSkills,
        listOfChosenSkills = chosenSkills,
    )

    private fun validateNickname(nickname: String): ProfileEditState.ValidatedField {
        val error = when {
            nickname.length < MIN_NICKNAME_LENGTH ->
                TextOrResource.Resource(R.string.error_minimal_length_2)

            nickname.length > MAX_NICKNAME_LENGTH ->
                TextOrResource.Resource(R.string.error_max_length_30)

            else -> null
        }
        return ProfileEditState.ValidatedField(nickname, error)
    }

    private fun validateMaxLength(value: String): ProfileEditState.ValidatedField {
        val error = if (value.length > MAX_FIELD_LENGTH) {
            TextOrResource.Resource(R.string.error_max_length_255)
        } else {
            null
        }
        return ProfileEditState.ValidatedField(value, error)
    }

    private companion object {
        const val MIN_NICKNAME_LENGTH = 2
        const val MAX_NICKNAME_LENGTH = 30
        const val MAX_FIELD_LENGTH = 255
    }
}
