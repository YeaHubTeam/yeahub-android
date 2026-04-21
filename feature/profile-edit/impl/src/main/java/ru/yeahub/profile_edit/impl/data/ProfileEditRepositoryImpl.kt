package ru.yeahub.profile_edit.impl.data

import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import ru.yeahub.network_api.ApiService
import ru.yeahub.network_api.models.GetProfileForUserResponse
import ru.yeahub.network_api.models.GetSkillResponse
import ru.yeahub.network_api.models.GetSpecializationResponse
import ru.yeahub.network_api.models.GetUserProfileResponse
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditData
import ru.yeahub.profile_edit.impl.domain.repository.ProfileEditRepository

internal class ProfileEditRepositoryImpl(
    private val apiService: ApiService,
    private val mapperDataToDomain: ProfileEditDataToDomainMapper,
    private val mapperDomainToData: ProfileEditDomainToDataMapper,
) : ProfileEditRepository {

    private var cachedUser: GetUserProfileResponse? = null
    private var cachedProfile: GetProfileForUserResponse? = null
    private var cachedAllSkillResponses: List<GetSkillResponse> = emptyList()
    private var cachedAllSpecializations: List<GetSpecializationResponse> = emptyList()

    private var pendingAvatarChange: PendingAvatarChange = PendingAvatarChange.None

    override suspend fun getProfileData(): DomainProfileEditData {
        pendingAvatarChange = PendingAvatarChange.None
        return coroutineScope {
            val skillsDeferred = async(Dispatchers.IO) { getAllSkills() }
            val specsDeferred = async(Dispatchers.IO) { getAllSpecializations() }
            val userDeferred = async(Dispatchers.IO) { apiService.getProfile() }

            val skills = skillsDeferred.await()
            val specializations = specsDeferred.await()
            val user = userDeferred.await()

            cachedUser = user
            val activeProfile = user.profiles.find { it.isActive == true } ?: user.profiles.first()
            cachedProfile = activeProfile

            mapperDataToDomain.mapProfileToDomain(user, activeProfile, skills, specializations)
        }
    }

    private suspend fun getAllSkills(): List<GetSkillResponse> {
        val response = apiService.getSkills(page = 1, limit = 200)
        cachedAllSkillResponses = response.data
        return response.data
    }

    private suspend fun getAllSpecializations(): List<GetSpecializationResponse> {
        val response = apiService.getSpecializations(page = 1, limit = 200)
        cachedAllSpecializations = response.data
        return response.data
    }

    override suspend fun saveProfile(profile: DomainProfileEditData) {
        val user = cachedUser ?: error("Profile not loaded")
        val activeProfile = cachedProfile ?: error("Profile not loaded")
        val cachedDescription = activeProfile.description.orEmpty()
        val normalizedCachedDescription =
            mapperDataToDomain.mapDescriptionToPlainText(cachedDescription)
        val description = if (profile.aboutMe == normalizedCachedDescription) {
            cachedDescription
        } else {
            mapperDomainToData.mapAboutMeToHtml(profile.aboutMe)
        }

        val updateProfileRequest = mapperDomainToData.mapToUpdateProfileRequest(
            profile = profile,
            description = description,
            cachedProfile = activeProfile,
            cachedUser = user,
            cachedAllSkills = cachedAllSkillResponses,
            allSpecializations = cachedAllSpecializations,
        )

        val updateUserRequest = mapperDomainToData.mapToUpdateUserRequest(
            profile = profile,
            cachedUser = user,
            pendingAvatarChange = pendingAvatarChange,
        )
        withContext(Dispatchers.IO) {
            val updateProfile = async {
                apiService.updateProfile(activeProfile.id, updateProfileRequest)
            }
            val updateUser = async {
                apiService.updateUser(user.id, updateUserRequest)
            }
            awaitAll(updateProfile, updateUser)
        }
    }

    override suspend fun cacheAvatar(avatarBytes: ByteArray) {
        pendingAvatarChange = PendingAvatarChange.Upload(
            avatarBase64 = Base64.encodeToString(avatarBytes, Base64.NO_WRAP),
        )
    }

    override suspend fun markAvatarDeleted() {
        pendingAvatarChange = PendingAvatarChange.Delete
    }
}

internal sealed interface PendingAvatarChange {
    data object None : PendingAvatarChange
    data class Upload(val avatarBase64: String) : PendingAvatarChange
    data object Delete : PendingAvatarChange
}
