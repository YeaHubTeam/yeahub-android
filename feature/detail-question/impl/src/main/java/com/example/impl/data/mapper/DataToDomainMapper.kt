package com.example.impl.data.mapper

import com.example.impl.data.local.Guru
import com.example.impl.data.local.getGuru
import com.example.impl.domain.models.GuruEntity
import com.example.impl.domain.models.NestedSkillEntity
import com.example.impl.domain.models.NestedSpecializationEntity
import com.example.impl.domain.models.NestedUserReferenceEntity
import com.example.impl.domain.models.PublicQuestionEntity
import ru.yeahub.network_api.models.GetPublicQuestionResponse
import ru.yeahub.network_api.models.NestedSkillResponse
import ru.yeahub.network_api.models.NestedSpecializationResponse
import ru.yeahub.network_api.models.NestedUserReferenceDto

class DataToDomainMapper {

    fun getPublicQuestionEntity(getPublicQuestionResponse: GetPublicQuestionResponse): PublicQuestionEntity {
        return PublicQuestionEntity(
            id = getPublicQuestionResponse.id,
            title = getPublicQuestionResponse.title,
            description = getPublicQuestionResponse.description,
            code = getPublicQuestionResponse.code,
            imageSrc = getPublicQuestionResponse.imageSrc,
            longAnswer = getPublicQuestionResponse.longAnswer,
            shortAnswer = getPublicQuestionResponse.shortAnswer,
            keywords = getPublicQuestionResponse.keywords,
            status = getPublicQuestionResponse.status,
            rate = getPublicQuestionResponse.rate,
            complexity = getPublicQuestionResponse.complexity,
            createdById = getPublicQuestionResponse.createdById,
            updatedById = getPublicQuestionResponse.updatedById,
            questionSpecializations = getPublicQuestionResponse.questionSpecializations.map {
                getQuestionSpecializationsToEntity(
                    it
                )
            },
            questionSkills = getPublicQuestionResponse.questionSkills.map {
                getQuestionSkillsToEntity(
                    it
                )
            },
            createdAt = getPublicQuestionResponse.createdAt,
            updatedAt = getPublicQuestionResponse.updatedAt,
            createdBy = getNestedUserReferenceToEntity(getPublicQuestionResponse.createdBy),
            updatedBy = getPublicQuestionResponse.updatedBy?.let { getNestedUserReferenceToEntity(it) },
            guru = getGuruToEntity(getGuru(getPublicQuestionResponse.questionSpecializations))
        )
    }

    private fun getGuruToEntity(guru: Guru): GuruEntity {
        return GuruEntity(
            title = guru.title,
            name = guru.name,
            specializationId = guru.specializationId,
            photoUrl = guru.photoUrl,
            telegramUrl = guru.telegramUrl,
            youtubeUrl = guru.youtubeUrl
        )
    }

    private fun getQuestionSpecializationsToEntity(
        nestedSpecializationResponse: NestedSpecializationResponse
    ): NestedSpecializationEntity {
        return NestedSpecializationEntity(
            id = nestedSpecializationResponse.id,
            title = nestedSpecializationResponse.title,
            description = nestedSpecializationResponse.description,
            imageSrc = nestedSpecializationResponse.imageSrc,
            createdAt = nestedSpecializationResponse.createdAt,
            updatedAt = nestedSpecializationResponse.updatedAt
        )
    }

    private fun getQuestionSkillsToEntity(nestedSkillResponse: NestedSkillResponse): NestedSkillEntity {
        return NestedSkillEntity(
            id = nestedSkillResponse.id,
            title = nestedSkillResponse.title,
            description = nestedSkillResponse.description,
            imageSrc = nestedSkillResponse.imageSrc,
            createdAt = nestedSkillResponse.createdAt,
            updatedAt = nestedSkillResponse.updatedAt
        )
    }

    private fun getNestedUserReferenceToEntity(
        nestedUserReferenceDto: NestedUserReferenceDto
    ): NestedUserReferenceEntity {
        return NestedUserReferenceEntity(
            id = nestedUserReferenceDto.id,
            username = nestedUserReferenceDto.username
        )
    }
}