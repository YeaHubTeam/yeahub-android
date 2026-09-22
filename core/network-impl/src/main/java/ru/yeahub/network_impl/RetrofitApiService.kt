package ru.yeahub.network_impl

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.yeahub.network_api.ApiService
import ru.yeahub.network_api.models.AuthUserDto
import ru.yeahub.network_api.models.GetCollectionsResponse
import ru.yeahub.network_api.models.GetPublicQuestionResponse
import ru.yeahub.network_api.models.GetPublicQuestionsResponse
import ru.yeahub.network_api.models.GetSkillsResponse
import ru.yeahub.network_api.models.GetSpecializationResponse
import ru.yeahub.network_api.models.GetSpecializationsResponse
import ru.yeahub.network_api.models.LoginRequestDto
import ru.yeahub.network_api.models.LoginResponseDto
import ru.yeahub.network_api.models.RegistrationRequestDto

interface RetrofitApiService : ApiService {

    @POST("auth/signUp")
    override suspend fun register(
        @Body request: RegistrationRequestDto
    )

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

    // Пустые реализации для ApiService, если они не нужны в Retrofit-слое
    override suspend fun login(request: LoginRequestDto): LoginResponseDto = TODO("Not required for this feature")
    override suspend fun getProfile(): AuthUserDto = TODO("Not required for this feature")
}
