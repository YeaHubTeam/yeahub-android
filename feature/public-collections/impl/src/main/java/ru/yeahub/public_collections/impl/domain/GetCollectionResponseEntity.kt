package ru.yeahub.public_collections.impl.domain

data class GetCollectionResponseEntity(
    val id: Int,
    val title: String,
    val description: String,
    val imageSrc: String?,
  /*  val isFree: Boolean,
    val keywords: List<String>?,*/
    val questionsCount: Int,
 /*   val specializations: List<NestedSpecializationEntity>,
    val company: GetCompanyResponseEntity,
    val createdBy: NestedUserReferenceEntity,
    val createdAt: String,
    val updatedAt: String,*/
)