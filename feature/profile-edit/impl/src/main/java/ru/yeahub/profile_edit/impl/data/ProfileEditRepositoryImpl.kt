package ru.yeahub.profile_edit.impl.data

import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.yeahub.network_api.ApiService
import ru.yeahub.network_api.models.GetProfileForUserResponse
import ru.yeahub.network_api.models.GetSkillResponse
import ru.yeahub.network_api.models.GetSpecializationResponse
import ru.yeahub.network_api.models.GetUserProfileResponse
import ru.yeahub.network_api.models.UpdateProfileRequest
import ru.yeahub.network_api.models.UpdateUserRequest
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

    /**
     * При загрузке профиля мы сохраняем исходные ответы в cachedUser и cachedProfile.
     * На save прогоняем эти данные через тот же mapper, что и текущую форму, а затем
     * сравниваем готовые request-ы. Так не приходится отдельно описывать, какие поля
     * относятся к user, а какие к profile.
     * После сравнения отправляем только изменённые request-ы или не отправляем ничего.
     */
    override suspend fun saveProfile(profile: DomainProfileEditData) {
        val user = cachedUser ?: error("Profile not loaded")
        val activeProfile = cachedProfile ?: error("Profile not loaded")
        val cachedDomainProfile = mapperDataToDomain.mapProfileToDomain(
            user = user,
            activeProfile = activeProfile,
            allSkills = cachedAllSkillResponses,
            specializations = cachedAllSpecializations,
        )

        val currentProfileRequest = mapperDomainToData.mapToUpdateProfileRequest(
            profile = profile,
            description = resolveDescription(profile, activeProfile),
            cachedProfile = activeProfile,
            cachedUser = user,
            cachedAllSkills = cachedAllSkillResponses,
            allSpecializations = cachedAllSpecializations,
        )
        val cachedProfileRequest = mapperDomainToData.mapToUpdateProfileRequest(
            profile = cachedDomainProfile,
            description = activeProfile.description.orEmpty(),
            cachedProfile = activeProfile,
            cachedUser = user,
            cachedAllSkills = cachedAllSkillResponses,
            allSpecializations = cachedAllSpecializations,
        )
        val currentUserRequest = mapperDomainToData.mapToUpdateUserRequest(
            profile = profile,
            cachedUser = user,
            pendingAvatarChange = pendingAvatarChange,
        )
        val cachedUserRequest = mapperDomainToData.mapToUpdateUserRequest(
            profile = cachedDomainProfile,
            cachedUser = user,
            pendingAvatarChange = PendingAvatarChange.None,
        )

        val shouldUpdateProfile = currentProfileRequest.toComparableProfileRequest() !=
                cachedProfileRequest.toComparableProfileRequest()
        val shouldUpdateUser = currentUserRequest.toComparableUserRequest() !=
                cachedUserRequest.toComparableUserRequest()

        if (!shouldUpdateProfile && !shouldUpdateUser) return

        withContext(Dispatchers.IO) {
            if (shouldUpdateProfile) {
                launch {
                    apiService.updateProfile(activeProfile.id, currentProfileRequest)
                }
            }

            if (shouldUpdateUser) {
                launch {
                    apiService.updateUser(user.id, currentUserRequest)
                }
            }
        }
    }

    /**
     * Если текст AboutMe не меняли, отправляем HTML с backend без пересборки.
     * Иначе можно случайно потерять форматирование, которое не видно в plain text.
     */
    private fun resolveDescription(
        profile: DomainProfileEditData,
        cachedProfile: GetProfileForUserResponse,
    ): String {
        val cachedDescription = cachedProfile.description.orEmpty()
        val normalizedCachedDescription =
            mapperDataToDomain.mapDescriptionToPlainText(cachedDescription)

        return if (profile.aboutMe == normalizedCachedDescription) {
            cachedDescription
        } else {
            mapperDomainToData.mapAboutMeToHtml(profile.aboutMe)
        }
    }

    /**
     * Перед сравнением сортируем списки, где порядок не важен для сохранения.
     * Перестановка skills или соцсетей не должна приводить к обновлению профиля.
     */
    private fun UpdateProfileRequest.toComparableProfileRequest(): UpdateProfileRequest {
        return copy(
            socialNetwork = socialNetwork?.sortedWith(
                compareBy(
                    { socialNetwork -> socialNetwork.code },
                    { socialNetwork -> socialNetwork.title },
                ),
            ),
            profileSkills = profileSkills.sorted(),
        )
    }

    /**
     * В форме пустые user-поля представлены пустой строкой, а backend может вернуть null.
     * Для сравнения считаем это одним и тем же значением.
     */
    private fun UpdateUserRequest.toComparableUserRequest(): UpdateUserRequest {
        return copy(
            username = username.orEmpty(),
            city = city.orEmpty(),
            avatarUrl = avatarUrl.orEmpty(),
        )
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
