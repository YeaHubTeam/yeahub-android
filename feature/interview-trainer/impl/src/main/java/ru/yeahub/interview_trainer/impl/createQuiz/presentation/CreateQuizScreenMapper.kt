package ru.yeahub.interview_trainer.impl.createQuiz.presentation

object CreateQuizScreenMapper {

    fun getScreenState(
        specializations: List<CreateQuizState.Loaded.VoSpecialization>,
        selectedSpecializationId: Long,
        questionsCount: Int,
    ): CreateQuizState = CreateQuizState.Loaded(
        specializations = specializations,
        selectedSpecializationId = selectedSpecializationId,
        questionsCount = questionsCount
    )
}