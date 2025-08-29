package ru.yeahub.public_collections.impl.domain

data class GetCompanyResponseEntity(
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