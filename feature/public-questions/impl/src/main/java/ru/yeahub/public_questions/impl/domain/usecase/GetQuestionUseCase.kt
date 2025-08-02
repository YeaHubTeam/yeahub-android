package ru.yeahub.public_questions.impl.domain.usecase

import ru.yeahub.public_questions.impl.domain.entity.QuestionsModel
import ru.yeahub.public_questions.impl.domain.repository.QuestionsRepositoryApi
import ru.yeahub.public_questions.impl.presentation.RequestQuestionsData

class GetQuestionUseCase(private val repository: QuestionsRepositoryApi) {

    suspend fun invoke(requestQuestionsData: RequestQuestionsData): QuestionsModel {
        return repository.getQuestion(
            page = requestQuestionsData.page,
            limit = requestQuestionsData.limit,
            skills = requestQuestionsData.skills,
            skillFilter = requestQuestionsData.skillFilter
        )
    }
}
