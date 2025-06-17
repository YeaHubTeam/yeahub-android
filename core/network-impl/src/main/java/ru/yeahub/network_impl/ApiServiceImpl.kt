package ru.yeahub.network_impl

import ru.yeahub.network_api.ApiService

class ApiServiceImpl(
    private val retrofitApiService: RetrofitApiService
): ApiService {
    override suspend fun getQuestions(
        page: Int,
        limit: Int,
        title: String?,
        titleOrDescription: String?,
        skills: List<String>?,
        skillFilterMode: String?,
        complexity: List<Int>?,
        collection: Int?,
        rate: List<Int>?,
        keywords: List<String>?,
        specialization: Int?,
        orderBy: String?,
        order: String?,
        random: Boolean?
    ) = retrofitApiService.getQuestions(page, limit, title, titleOrDescription, skills,
                                        skillFilterMode, complexity, collection, rate, keywords,
                                        specialization, orderBy, order, random)

    override suspend fun getQuestionById(id: Long) = retrofitApiService.getQuestionById(id)

    override suspend fun getSkills(
        page: Int,
        limit: Int,
        specializations: List<Int>?
    ) = retrofitApiService.getSkills(page, limit, specializations)

    override suspend fun getSpecializations(
        page: Int,
        limit: Int
    ) = retrofitApiService.getSpecializations(page, limit)

    override suspend fun getSpecializationById(id: Long) = retrofitApiService.getSpecializationById(id)

}
