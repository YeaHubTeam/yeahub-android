package ru.yeahub.detail_question.impl.presentation.mapper

import ru.yeahub.detail_question.impl.domain.models.GuruEntity
import ru.yeahub.detail_question.impl.domain.models.NestedSkillEntity
import ru.yeahub.detail_question.impl.domain.models.NestedSpecializationEntity
import ru.yeahub.detail_question.impl.domain.models.NestedUserReferenceEntity
import ru.yeahub.detail_question.impl.domain.models.PublicQuestionEntity
import ru.yeahub.detail_question.impl.presentation.state.DetailQuestionState
import ru.yeahub.detail_question.impl.presentation.state.GuruVO
import ru.yeahub.detail_question.impl.presentation.state.NestedSkillVO
import ru.yeahub.detail_question.impl.presentation.state.NestedSpecializationVO
import ru.yeahub.detail_question.impl.presentation.state.NestedUserReferenceVO

class DetailQuestionScreenMapper {
    fun getScreenState(publicQuestionEntity: PublicQuestionEntity): DetailQuestionState {
        return DetailQuestionState.Success(
            DetailQuestionState.Success.PublicQuestionVO(
                id = publicQuestionEntity.id,
                title = publicQuestionEntity.title,
                description = publicQuestionEntity.description,
                code = publicQuestionEntity.code,
                imageSrc = publicQuestionEntity.imageSrc,
                longAnswer = publicQuestionEntity.longAnswer,
                shortAnswer = publicQuestionEntity.shortAnswer,
                keywords = publicQuestionEntity.keywords,
                status = publicQuestionEntity.status,
                rate = publicQuestionEntity.rate,
                complexity = publicQuestionEntity.complexity,
                createdById = publicQuestionEntity.createdById,
                updatedById = publicQuestionEntity.updatedById,
                questionSpecializations = publicQuestionEntity.questionSpecializations.map {
                    getQuestionSpecializationsToVO(
                        it
                    )
                },
                questionSkills = publicQuestionEntity.questionSkills.map {
                    getQuestionSkillsToVO(
                        it
                    )
                },
                createdAt = publicQuestionEntity.createdAt,
                updatedAt = publicQuestionEntity.updatedAt,
                createdBy = getNestedUserReferenceToVO(publicQuestionEntity.createdBy),
                updatedBy = publicQuestionEntity.updatedBy?.let { getNestedUserReferenceToVO(it) },
                guru = getGuruToVO(publicQuestionEntity.guru)
            )
        )
    }

    private fun getGuruToVO(guruEntity: GuruEntity): GuruVO {
        return GuruVO(
            title = guruEntity.title,
            name = guruEntity.name,
            specializationId = guruEntity.specializationId,
            photoUrl = guruEntity.photoUrl,
            telegramUrl = guruEntity.telegramUrl,
            youtubeUrl = guruEntity.youtubeUrl
        )
    }

    private fun getQuestionSpecializationsToVO(
        nestedSpecializationEntity: NestedSpecializationEntity
    ): NestedSpecializationVO {
        return NestedSpecializationVO(
            id = nestedSpecializationEntity.id,
            title = nestedSpecializationEntity.title,
            description = nestedSpecializationEntity.description,
            imageSrc = nestedSpecializationEntity.imageSrc,
            createdAt = nestedSpecializationEntity.createdAt,
            updatedAt = nestedSpecializationEntity.updatedAt
        )
    }

    private fun getQuestionSkillsToVO(nestedSkillEntity: NestedSkillEntity): NestedSkillVO {
        return NestedSkillVO(
            id = nestedSkillEntity.id,
            title = nestedSkillEntity.title,
            description = nestedSkillEntity.description,
            imageSrc = nestedSkillEntity.imageSrc,
            createdAt = nestedSkillEntity.createdAt,
            updatedAt = nestedSkillEntity.updatedAt
        )
    }

    private fun getNestedUserReferenceToVO(
        nestedUserReferenceEntity: NestedUserReferenceEntity
    ): NestedUserReferenceVO {
        return NestedUserReferenceVO(
            id = nestedUserReferenceEntity.id,
            username = nestedUserReferenceEntity.username
        )
    }
}