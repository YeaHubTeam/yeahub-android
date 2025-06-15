package ru.yeahub.network_impl

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.yeahub.network_api.models.GetPublicQuestionResponse
import ru.yeahub.network_api.models.GetPublicQuestionsResponse
import ru.yeahub.network_api.models.GetSkillsResponse
import ru.yeahub.network_api.models.GetSpecializationResponse
import ru.yeahub.network_api.models.GetSpecializationsResponse

interface ApiService {

    @GET("questions/public-questions")
    suspend fun getQuestions(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("title") title: String? = null,
        @Query("titleOrDescription") titleOrDescription: String? = null,
        @Query("skills") skills: List<String>? = null,
        @Query("skillFilterMode") skillFilterMode: String? = null,
        @Query("complexity") complexity: List<Int>? = null,
        @Query("collection") collection: Int? = null,
        @Query("rate") rate: List<Int>? = null,
        @Query("keywords") keywords: List<String>? = null,
        @Query("specialization") specialization: Int? = null,
        @Query("orderBy") orderBy: String? = null,
        @Query("order") order: String? = null,
        @Query("random") random: Boolean? = null
    ): GetPublicQuestionsResponse

    @GET("questions/public-questions/{id}")
    suspend fun getQuestionById(
        @Path("id") id: Long
    ): GetPublicQuestionResponse

    @GET("skills")
    suspend fun getSkills(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("specializations") specializations: List<Int>? = null
    ): GetSkillsResponse

    @GET("specializations")
    suspend fun getSpecializations(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): GetSpecializationsResponse

    @GET("specializations/{id}")
    suspend fun getSpecializationById(
        @Path("id") id: Long
    ): GetSpecializationResponse
}