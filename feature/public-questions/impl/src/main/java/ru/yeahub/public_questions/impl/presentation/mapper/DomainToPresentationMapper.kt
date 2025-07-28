package ru.yeahub.public_questions.impl.presentation.mapper

import ru.yeahub.public_questions.impl.domain.entity.QuestionsModel
import ru.yeahub.public_questions.impl.presentation.screen.QuestionUiModel

class DomainToPresentationMapper {

    fun domainQuestionsModelToUiModel(domainQuestionsModel: QuestionsModel): List<QuestionUiModel> {
        return domainQuestionsModel.data.map { question ->
            QuestionUiModel(
                id = question.id,
                title = question.title,
                rate = question.rate.toString(),
                complexity = question.complexity.toString(),
                imageSc = question.imageSc.toString(),
                description = question.description
            )
        }
    }
}
