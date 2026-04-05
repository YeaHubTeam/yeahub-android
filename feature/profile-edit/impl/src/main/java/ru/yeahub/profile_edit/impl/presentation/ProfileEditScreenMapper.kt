package ru.yeahub.profile_edit.impl.presentation

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentMap
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.ui.R

internal class ProfileEditScreenMapper {

    fun getScreenState(
        userInput: UserInput,
        staticData: StaticData,
    ): ProfileEditState {
        val nicknameField = validateNickname(userInput.nickname)
        val locationField = validateMaxLength(userInput.location)
        val socialLinksFields = userInput.socialLinks.entries.associate { (link, url) ->
            link to validateMaxLength(url)
        }.toPersistentMap()

        val validatedFields = listOf(nicknameField, locationField) + socialLinksFields.values

        return ProfileEditState.Loaded(
            personalInfoState = ProfileEditState.PersonalInfoTabState(
                avatarUrl = userInput.avatarUrl,
                nickname = nicknameField,
                specializationList = staticData.specializationList,
                specialization = userInput.specialization,
                isSpecializationEditable = staticData.isSpecializationEditable,
                email = staticData.email,
                location = locationField,
                socialLinks = socialLinksFields,
            ),
            aboutMeTabState = mapAboutMeState(userInput.aboutMe),
            skillsTabState = mapSkillsState(staticData.allSkills, userInput.selectedSkills),
            showUnsavedChangesDialog = userInput.showUnsavedChangesDialog,
            hasValidationErrors = validatedFields.any { it.error != null },
        )
    }

    fun getScreenState(e: Throwable): ProfileEditState = ProfileEditState.Error(e)

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
