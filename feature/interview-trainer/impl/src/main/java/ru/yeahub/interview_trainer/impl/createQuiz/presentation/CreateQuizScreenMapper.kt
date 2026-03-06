package ru.yeahub.interview_trainer.impl.createQuiz.presentation

import kotlinx.collections.immutable.toImmutableList
import ru.yeahub.interview_trainer.impl.createQuiz.domain.DomainSpecialization

class CreateQuizScreenMapper {

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
        }.toImmutableList(),
        selectedSpecializationId = selectedSpecializationId,
        questionsCount = questionsCount
    )

    fun getScreenState(
        throwable: Throwable,
    ): CreateQuizState = CreateQuizState.Error(throwable)
}