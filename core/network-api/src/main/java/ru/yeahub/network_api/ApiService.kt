package ru.yeahub.network_api

import ru.yeahub.network_api.models.GetPublicQuestionResponse
import ru.yeahub.network_api.models.GetPublicQuestionsResponse
import ru.yeahub.network_api.models.GetSkillsResponse
import ru.yeahub.network_api.models.GetSpecializationResponse
import ru.yeahub.network_api.models.GetSpecializationsResponse

interface ApiService {

    suspend fun getQuestions(
        page: Int,
        limit: Int,
        title: String? = null,
        titleOrDescription: String? = null,
        skills: List<String>? = null,
        skillFilterMode: String? = null,
        complexity: List<Int>? = null,
        collection: Int? = null,
        rate: List<Int>? = null,
        keywords: List<String>? = null,
        specialization: Int? = null,
        orderBy: String? = null,
        order: String? = null,
        random: Boolean? = null
    ): GetPublicQuestionsResponse

    suspend fun getQuestionById(
        id: Long
    ): GetPublicQuestionResponse

    suspend fun getSkills(
        page: Int,
        limit: Int,
        specializations: List<Int>? = null
    ): GetSkillsResponse

    suspend fun getSpecializations(
        page: Int,
        limit: Int
    ): GetSpecializationsResponse

    suspend fun getSpecializationById(
        id: Long
    ): GetSpecializationResponse
}
