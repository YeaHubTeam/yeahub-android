package ru.yeahub.selection_specializations.impl.domain

data class DomainSpecilializationListResponse(
    val page: Long,
    val limit: Long,
    val data: List<DomainSpecilialization>,
    val total: Long
)