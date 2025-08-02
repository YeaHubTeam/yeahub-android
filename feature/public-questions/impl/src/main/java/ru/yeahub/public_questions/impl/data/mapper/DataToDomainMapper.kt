package ru.yeahub.public_questions.impl.data.mapper

import ru.yeahub.network_api.models.GetPublicQuestionsResponse
import ru.yeahub.network_api.models.GetQuestionResponse
import ru.yeahub.public_questions.impl.domain.entity.QuestionModel
import ru.yeahub.public_questions.impl.domain.entity.QuestionsModel

class DataToDomainMapper {

    fun mapGetPublicQuestionsResponseToDomain(dto: GetPublicQuestionsResponse): QuestionsModel {
        return QuestionsModel(
            page = dto.page ?: 0,
            limit = dto.limit,
            data = dto.data.map { mapGetQuestionResponseToDomain(it) },
            total = dto.total
        )
    }

    fun mapGetQuestionResponseToDomain(getQuestionResponse: GetQuestionResponse): QuestionModel {
        return QuestionModel(
            id = getQuestionResponse.id,
            title = getQuestionResponse.title,
        )
    }
}
