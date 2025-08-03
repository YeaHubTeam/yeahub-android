package ru.yeahub.public_questions.impl.data.mapper

import ru.yeahub.network_api.models.GetPublicQuestionsResponse
import ru.yeahub.network_api.models.GetQuestionResponse
import ru.yeahub.public_questions.impl.domain.entity.PublicQuestionModel
import ru.yeahub.public_questions.impl.domain.entity.PublicQuestionsModel

class PublicQuestionsDataToDomainMapper {

    fun mapGetPublicQuestionsResponseToDomain(dto: GetPublicQuestionsResponse): PublicQuestionsModel {
        return PublicQuestionsModel(
            page = dto.page ?: 0,
            limit = dto.limit,
            data = dto.data.map { mapGetQuestionResponseToDomain(it) },
            total = dto.total
        )
    }

    private fun mapGetQuestionResponseToDomain(getQuestionResponse: GetQuestionResponse): PublicQuestionModel {
        return PublicQuestionModel(
            id = getQuestionResponse.id,
            title = getQuestionResponse.title,
        )
    }
}
