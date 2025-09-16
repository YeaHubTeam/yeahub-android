package ru.yeahub.example_home.impl.presentation.mapper

import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.example_home.impl.presentation.model.QuestionMainItemType
import ru.yeahub.example_home.impl.presentation.model.QuestionMainUiModel
import ru.yeahub.ui.R

class QuestionMainScreenMapper {
    fun getInitialUiModels(): List<QuestionMainUiModel> {
        return listOf(
            QuestionMainUiModel(
                type = QuestionMainItemType.BaseQuestions,
                title = TextOrResource.Resource(R.string.base_questions_title),
                description = TextOrResource.Resource(R.string.base_questions_description),
                imageRes = R.drawable.icon_base_question
            ),
            QuestionMainUiModel(
                type = QuestionMainItemType.Collections,
                title = TextOrResource.Resource(R.string.collections_title),
                description = TextOrResource.Resource(R.string.collections_description),
                imageRes = R.drawable.icon_collections
            )
        )
    }
}
