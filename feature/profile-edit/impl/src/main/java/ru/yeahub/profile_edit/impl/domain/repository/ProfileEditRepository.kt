package ru.yeahub.profile_edit.impl.domain.repository

import android.net.Uri
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditData
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill

internal interface ProfileEditRepository {

    suspend fun getProfileData(
        skills: List<DomainProfileEditSkill>,
        specializations: List<String>,
    ): DomainProfileEditData

    suspend fun getAllSkills(): List<DomainProfileEditSkill>

    suspend fun getSpecializations(): List<String>

    suspend fun saveProfile(profile: DomainProfileEditData)

    suspend fun cacheAvatar(uri: Uri): String

    suspend fun markAvatarDeleted()
}
