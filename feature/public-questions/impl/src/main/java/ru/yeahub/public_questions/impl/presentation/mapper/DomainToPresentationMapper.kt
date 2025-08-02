package ru.yeahub.public_questions.impl.presentation.mapper

import ru.yeahub.public_questions.impl.domain.entity.QuestionModel
import ru.yeahub.public_questions.impl.presentation.screen.QuestionUiModel

class DomainToPresentationMapper {

    fun mapQuestionModelToUiModel(questionModel: QuestionModel): QuestionUiModel {
        return QuestionUiModel(
            id = questionModel.id,
            title = questionModel.title
        )
    }

    fun mapQuestionModelListToUiModelList(questionModels: List<QuestionModel>): List<QuestionUiModel> {
        return questionModels.map { mapQuestionModelToUiModel(it) }
    }
}
