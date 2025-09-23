package ru.yeahub.network_api.models

data class GetCollectionResponse(
    val id: Int,
    val title: String,
    val description: String,
    val imageSrc: String?,
    val isFree: Boolean,
    val keywords: List<String>?,
    val questionsCount: Int,
    val specializations: List<NestedSpecializationResponse>,
    val company: GetCompanyResponse,
    val createdBy: NestedUserReferenceDto,
    val createdAt: String,
    val updatedAt: String,
)