package ru.yeahub.public_collections.impl.data

import retrofit2.http.GET
import retrofit2.http.Query
import ru.yeahub.network_api.models.GetPublicQuestionResponse

interface ApiService {

    @GET("collections/public")//что дописывать для специальности
   suspend fun getPublicCollections(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): GetCollectionsResponseDto
}