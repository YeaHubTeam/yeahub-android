package com.example.network_impl

import com.example.network_api.models.QuestionDto
import com.example.network_api.models.QuestionResponseDto
import com.example.network_api.models.SkillsResponseDto
import com.example.network_api.models.SpecializationDto
import com.example.network_api.models.SpecializationResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("questions/public-questions")
    suspend fun getQuestions(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("title") title: String? = null,
        @Query("titleOrDescription") titleOrDescription: String? = null,
        @Query("skills") skills: List<String>? = null,
        @Query("skillFilterMode") skillFilterMode: String? = "ALL",
        @Query("complexity") complexity: List<Int>? = null,
        @Query("collection") collection: Int? = null,
        @Query("rate") rate: List<Int>? = null,
        @Query("keywords") keywords: List<String>? = null,
        @Query("specialization") specialization: Int? = null,
        @Query("orderBy") orderBy: String? = null,
        @Query("order") order: String? = "ASC",
        @Query("random") random: Boolean? = null
    ): QuestionResponseDto

    @GET("questions/public-questions/{id}")
    suspend fun getQuestionById(
        @Path("id") id: Long
    ): QuestionDto

    @GET("skills")
    suspend fun getSkills(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("specializations") specializations: List<Int>? = null
    ): SkillsResponseDto

    @GET("specializations")
    suspend fun getAllSpecializations(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): SpecializationResponseDto

    @GET("specializations/{id}")
    suspend fun getSpecializationById(
        @Path("id") id: Long
    ): SpecializationDto
}