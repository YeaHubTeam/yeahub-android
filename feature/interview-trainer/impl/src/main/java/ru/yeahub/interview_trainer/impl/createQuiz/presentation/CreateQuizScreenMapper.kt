package ru.yeahub.interview_trainer.impl.createQuiz.presentation

import ru.yeahub.interview_trainer.impl.createQuiz.domain.DomainSpecialization

object CreateQuizScreenMapper {

    fun getScreenState(
        specializations: List<DomainSpecialization>,
        selectedSpecializationId: Long,
        questionsCount: Int,
    ): CreateQuizState = CreateQuizState.Loaded(
        specializations = specializations.map { domainSpec ->
            CreateQuizState.Loaded.VoSpecialization(
                id = domainSpec.id,
                title = domainSpec.title
            )
        },
        selectedSpecializationId = selectedSpecializationId,
        questionsCount = questionsCount
    )
}