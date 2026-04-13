package ru.yeahub.profile_edit.impl.presentation

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.toPersistentMap
import retrofit2.HttpException
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSocialPlatform
import ru.yeahub.ui.R
import java.io.IOException
import ru.yeahub.profile_edit.impl.R as ProfileEditR

internal sealed interface ProfileEditMapperInput {
    data object Loading : ProfileEditMapperInput
    data class Loaded(
        val mutableState: ProfileEditMutableState,
        val staticData: ViewModelStaticData,
    ) : ProfileEditMapperInput

    data class Error(val throwable: Throwable) : ProfileEditMapperInput
}

internal class ProfileEditScreenMapper {

    fun getScreenState(input: ProfileEditMapperInput): ProfileEditState = when (input) {
        is ProfileEditMapperInput.Loading -> ProfileEditState.Loading
        is ProfileEditMapperInput.Loaded -> mapToLoaded(input.mutableState, input.staticData)
        is ProfileEditMapperInput.Error -> ProfileEditState.Error(mapThrowableToMessage(input.throwable))
    }

    private fun mapToLoaded(
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
            personalInfoState = mapPersonalInfoState(
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

    private fun mapThrowableToMessage(throwable: Throwable): TextOrResource = when (throwable) {
        is IOException -> TextOrResource.Resource(ProfileEditR.string.error_no_internet)
        is HttpException -> when (throwable.code()) {
            HTTP_UNAUTHORIZED -> TextOrResource.Resource(ProfileEditR.string.error_session_expired)
            HTTP_FORBIDDEN -> TextOrResource.Resource(ProfileEditR.string.error_forbidden)
            HTTP_NOT_FOUND -> TextOrResource.Resource(ProfileEditR.string.error_profile_not_found)
            HTTP_PAYLOAD_TOO_LARGE -> TextOrResource.Resource(ProfileEditR.string.error_file_too_large)
            HTTP_BAD_REQUEST, HTTP_UNPROCESSABLE ->
                TextOrResource.Resource(ProfileEditR.string.error_invalid_data)

            in HTTP_SERVER_ERROR_RANGE -> TextOrResource.Resource(ProfileEditR.string.error_server)
            else -> TextOrResource.Resource(R.string.error_screen_text)
        }

        else -> TextOrResource.Resource(R.string.error_screen_text)
    }

    private fun mapSnackbarState(throwable: Throwable?): ProfileEditState.SnackbarState? {
        if (throwable == null) return null
        return ProfileEditState.SnackbarState(
            message = mapThrowableToMessage(throwable),
            throwableMessage = throwable.localizedMessage ?: throwable.toString(),
        )
    }

    private fun mapPersonalInfoState(
        avatarUrl: String,
        nickname: ProfileEditState.ValidatedField,
        specializationList: PersistentList<String>,
        specialization: String,
        isSpecializationEditable: Boolean,
        email: String?,
        location: ProfileEditState.ValidatedField,
        socialLinks: PersistentMap<DomainProfileEditSocialPlatform, ProfileEditState.ValidatedField>,
    ): ProfileEditState.PersonalInfoTabState = ProfileEditState.PersonalInfoTabState(
        avatarUrl = avatarUrl,
        nickname = nickname,
        specializationList = specializationList,
        specialization = specialization,
        isSpecializationEditable = isSpecializationEditable,
        email = email,
        location = location,
        socialLinks = socialLinks,
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
        listOfSkills = allSkills.removeAll(chosenSkills),
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
        const val HTTP_UNAUTHORIZED = 401
        const val HTTP_BAD_REQUEST = 400
        const val HTTP_FORBIDDEN = 403
        const val HTTP_NOT_FOUND = 404
        const val HTTP_PAYLOAD_TOO_LARGE = 413
        const val HTTP_UNPROCESSABLE = 422
        val HTTP_SERVER_ERROR_RANGE = 500..599
    }
}
