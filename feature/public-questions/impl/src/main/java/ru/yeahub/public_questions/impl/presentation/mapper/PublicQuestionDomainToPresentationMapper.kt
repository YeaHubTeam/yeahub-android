package ru.yeahub.public_questions.impl.presentation.mapper

import ru.yeahub.public_questions.impl.domain.entity.PublicQuestionModel
import ru.yeahub.public_questions.impl.presentation.screen.PublicQuestionUiModel

class PublicQuestionDomainToPresentationMapper {

    private fun mapQuestionModelToUiModel(
        publicQuestionModel: PublicQuestionModel
    ): PublicQuestionUiModel {
        return PublicQuestionUiModel(
            id = publicQuestionModel.id,
            title = publicQuestionModel.title
        )
    }

    fun mapQuestionModelListToUiModelList(
        publicQuestionModels: List<PublicQuestionModel>
    ): List<PublicQuestionUiModel> {
        return publicQuestionModels.map { mapQuestionModelToUiModel(it) }
    }
}
