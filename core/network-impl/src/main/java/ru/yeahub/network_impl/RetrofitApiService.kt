package ru.yeahub.network_impl

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import ru.yeahub.network_api.ApiService
import ru.yeahub.network_api.models.GetCollectionsResponse
import ru.yeahub.network_api.models.GetPublicQuestionResponse
import ru.yeahub.network_api.models.GetPublicQuestionsResponse
import ru.yeahub.network_api.models.GetSkillsResponse
import ru.yeahub.network_api.models.GetSpecializationResponse
import ru.yeahub.network_api.models.GetSpecializationsResponse
import ru.yeahub.network_api.models.GetUserProfileResponse
import ru.yeahub.network_api.models.UpdateProfileRequest
import ru.yeahub.network_api.models.UpdatePublicUserResponse
import ru.yeahub.network_api.models.UpdateUserRequest

interface RetrofitApiService : ApiService {

    @GET("questions/public-questions")
    override suspend fun getQuestions(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("title") title: String?,
        @Query("titleOrDescription") titleOrDescription: String?,
        @Query("skills") skills: List<String>?,
        @Query("skillFilterMode") skillFilterMode: String?,
        @Query("complexity") complexity: List<Int>?,
        @Query("collection") collection: Int?,
        @Query("rate") rate: List<Int>?,
        @Query("keywords") keywords: List<String>?,
        @Query("specialization") specialization: Int?,
        @Query("orderBy") orderBy: String?,
        @Query("order") order: String?,
        @Query("random") random: Boolean?
    ): GetPublicQuestionsResponse

    @GET("questions/public-questions/{id}")
    override suspend fun getQuestionById(
        @Path("id") id: Long
    ): GetPublicQuestionResponse

    @GET("skills")
    override suspend fun getSkills(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("specializations") specializations: List<Int>?
    ): GetSkillsResponse

    @GET("specializations")
    override suspend fun getSpecializations(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): GetSpecializationsResponse

    @GET("specializations/{id}")
    override suspend fun getSpecializationById(
        @Path("id") id: Long
    ): GetSpecializationResponse

    @GET("collections/public")
    override suspend fun getPublicCollections(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("specializations") specializationsId: Long,
        @Query("isFree") isFree: Boolean
    ): GetCollectionsResponse

    @GET("auth/profile")
    override suspend fun getProfile(): GetUserProfileResponse

    @PUT("profiles/{id}")
    override suspend fun updateProfile(
        @Path("id") id: String,
        @Body body: UpdateProfileRequest,
    ): Any

    @PATCH("users/{id}")
    override suspend fun updateUser(
        @Path("id") id: String,
        @Body body: UpdateUserRequest,
    ): UpdatePublicUserResponse
}
