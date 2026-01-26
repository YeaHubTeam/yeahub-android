package ru.yeahub.interview_trainer.impl.createQuiz.domain

data class DomainSpecializationListResponse(
    val page: Long,
    val limit: Long,
    val total: Long,
    val data: List<DomainSpecialization>,
)