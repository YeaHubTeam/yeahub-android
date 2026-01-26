package ru.yeahub.interview_trainer.impl.createQuiz.domain

data class DomainSpecializationListResponse(
    val total: Long,
    val data: List<DomainSpecialization>,
)