package ru.yeahub.public_questions.impl.presentation.mapper

import ru.yeahub.public_questions.impl.domain.entity.QuestionsModel
import ru.yeahub.public_questions.impl.presentation.screen.QuestionUiModel

class DomainToPresentationMapper {
    /**
     * Обработка нулабельного состояния будет происходит в data слое. снизу - > это временное решение
     */
    fun domainQuestionsModelToUiModel(domainQuestionsModel: QuestionsModel): QuestionUiModel {
        return QuestionUiModel(
            id = domainQuestionsModel.id,
            title = domainQuestionsModel.title,
            rate = domainQuestionsModel.rate ?: "N/A",
            complexity = domainQuestionsModel.complexity ?: "",
            imageSc = domainQuestionsModel.imageSc ?: "",
            description = domainQuestionsModel.description
        )
    }
}