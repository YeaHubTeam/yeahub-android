package ru.yeahub.public_questions.impl.domain.usecase

import ru.yeahub.public_questions.impl.domain.entity.PublicQuestionsModel
import ru.yeahub.public_questions.impl.domain.repository.PublicQuestionsRepositoryApi
import ru.yeahub.public_questions.impl.presentation.viewmodel.RequestPublicQuestionsData

class GetPublicQuestionsUseCase(private val repository: PublicQuestionsRepositoryApi) {

    suspend fun invoke(requestPublicQuestionsData: RequestPublicQuestionsData): PublicQuestionsModel {
        return repository.getPublicQuestionsQuestion(
            page = requestPublicQuestionsData.page,
            limit = requestPublicQuestionsData.limit,
            skillFilter = requestPublicQuestionsData.skillFilter,
            idCollection = requestPublicQuestionsData.idCollection,
            idSpecialization = requestPublicQuestionsData.idSpecialization,
        )
    }
}
