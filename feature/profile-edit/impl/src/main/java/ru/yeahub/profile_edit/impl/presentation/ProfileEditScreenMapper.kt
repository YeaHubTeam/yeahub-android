package ru.yeahub.profile_edit.impl.presentation

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentMap
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.ui.R

internal class ProfileEditScreenMapper {

    fun getScreenState(
        loadResult: Result<ProfileEditUserInput>?,
        staticData: ProfileEditStaticData?,
    ): ProfileEditState = when {
        loadResult == null -> ProfileEditState.Loading
        loadResult.isFailure -> ProfileEditState.Error(loadResult.exceptionOrNull()!!)
        staticData == null -> ProfileEditState.Loading
        else -> mapToLoaded(loadResult.getOrThrow(), staticData)
    }

    private fun mapToLoaded(
        userInput: ProfileEditUserInput,
        staticData: ProfileEditStaticData,
    ): ProfileEditState.Loaded = ProfileEditState.Loaded(
        personalInfoState = mapPersonalInfoState(userInput, staticData),
        aboutMeTabState = mapAboutMeState(userInput.aboutMe),
        skillsTabState = mapSkillsState(staticData.allSkills, userInput.chosenSkills),
        showUnsavedChangesDialog = userInput.showUnsavedChangesDialog,
    )

    private fun mapPersonalInfoState(
        userInput: ProfileEditUserInput,
        staticData: ProfileEditStaticData,
    ): ProfileEditState.PersonalInfoTabState = ProfileEditState.PersonalInfoTabState(
        avatarUrl = userInput.avatarUrl,
        nickname = validateNickname(userInput.nickname),
        specializationList = staticData.specializationList,
        specialization = userInput.specialization,
        isSpecializationEditable = staticData.isSpecializationEditable,
        email = staticData.email,
        location = validateLength(userInput.location),
        socialLinks = userInput.socialLinks.entries.associate { (link, url) ->
            link to validateLength(url)
        }.toPersistentMap(),
    )

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

    private fun validateLength(value: String): ProfileEditState.ValidatedField {
        val error = if (value.length > MAX_FIELD_LENGTH) {
            TextOrResource.Resource(R.string.error_max_length_255)
        } else {
            null
        }
        return ProfileEditState.ValidatedField(value, error)
    }

    @Suppress("NoMagicNumber")
    private companion object {
        const val MIN_NICKNAME_LENGTH = 2
        const val MAX_NICKNAME_LENGTH = 30
        const val MAX_FIELD_LENGTH = 255
    }
}
