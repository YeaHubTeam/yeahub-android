package ru.yeahub.network_api.models

data class GetCompanyResponse(
    val id: String,
    val title: String,
    val legalName: String?,
    val description: String?,
    val imageSrc: String?,
    val inn: String?,
    val kpp: String?,
    val createdAt: String,
    val updatedAt: String
)