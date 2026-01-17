package ru.yeahub.interview_trainer.impl.createQuiz.domain

data class DomainSpecializationListResponse(
    val data: List<DomainSpecialization>,
    val total: Long,
)