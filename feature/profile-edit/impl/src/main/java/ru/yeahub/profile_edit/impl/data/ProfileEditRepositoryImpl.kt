package ru.yeahub.profile_edit.impl.data

import android.content.Context
import android.net.Uri
import android.util.Base64
import ru.yeahub.network_api.ApiService
import ru.yeahub.network_api.models.GetProfileForUserResponse
import ru.yeahub.network_api.models.GetSkillResponse
import ru.yeahub.network_api.models.GetUserProfileResponse
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditData
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSpecialization
import ru.yeahub.profile_edit.impl.domain.repository.ProfileEditRepository

internal class ProfileEditRepositoryImpl(
    private val apiService: ApiService,
    private val mapper: ProfileEditDataToDomainMapper,
    private val context: Context,
) : ProfileEditRepository {

    private var cachedUser: GetUserProfileResponse? = null
    private var cachedProfile: GetProfileForUserResponse? = null
    private var cachedAllSkillResponses: List<GetSkillResponse> = emptyList()
    private var cachedAllSpecializations: List<DomainProfileEditSpecialization> = emptyList()
    private var avatarBase64: String? = null
    private var avatarDeleted: Boolean = false

    override suspend fun getProfileData(
        skills: List<DomainProfileEditSkill>,
        specializations: List<DomainProfileEditSpecialization>,
    ): DomainProfileEditData {
        val user = apiService.getProfile()
        cachedUser = user
        val activeProfile = user.profiles.find { it.isActive == true } ?: user.profiles.first()
        cachedProfile = activeProfile
        return mapper.mapProfileToDomain(user, activeProfile, skills, specializations)
    }

    override suspend fun getAllSkills(): List<DomainProfileEditSkill> {
        val response = apiService.getSkills(page = 1, limit = 1000)
        cachedAllSkillResponses = response.data
        return response.data.map { mapper.mapSkillToDomain(it) }
    }

    override suspend fun getSpecializations(): List<DomainProfileEditSpecialization> {
        val response = apiService.getSpecializations(page = 1, limit = 1000)
        val specializations = response.data.map {
            DomainProfileEditSpecialization(
                id = it.id,
                title = it.title,
            )
        }
        cachedAllSpecializations = specializations
        return specializations
    }

    override suspend fun saveProfile(profile: DomainProfileEditData) {
        val user = cachedUser ?: error("Profile not loaded")
        val activeProfile = cachedProfile ?: error("Profile not loaded")

        val updateProfileRequest = mapper.mapToUpdateProfileRequest(
            profile = profile,
            cachedProfile = activeProfile,
            cachedUser = user,
            cachedAllSkills = cachedAllSkillResponses,
            allSpecializations = cachedAllSpecializations,
        )
        apiService.updateProfile(activeProfile.id, updateProfileRequest)

        val updateUserRequest = mapper.mapToUpdateUserRequest(
            profile = profile,
            cachedUser = user,
            avatarBase64 = avatarBase64,
            avatarDeleted = avatarDeleted,
        )
        apiService.updateUser(user.id, updateUserRequest)

        avatarBase64 = null
        avatarDeleted = false
    }

    override suspend fun cacheAvatar(uri: Uri): String {
        val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            ?: error("Cannot read avatar from uri: $uri")
        avatarBase64 = Base64.encodeToString(bytes, Base64.NO_WRAP)
        avatarDeleted = false
        return uri.toString()
    }

    override suspend fun markAvatarDeleted() {
        avatarBase64 = null
        avatarDeleted = true
    }
}
