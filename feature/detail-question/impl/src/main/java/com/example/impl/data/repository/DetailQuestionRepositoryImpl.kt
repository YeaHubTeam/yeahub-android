package com.example.impl.data.repository

import com.example.impl.data.mapper.DataToDomainMapper
import com.example.impl.domain.models.PublicQuestionEntity
import com.example.impl.domain.repository.DetailQuestionRepository
import ru.yeahub.network_api.ApiService
import timber.log.Timber

class DetailQuestionRepositoryImpl(
    private val apiService: ApiService,
    private val dataToDomainMapper: DataToDomainMapper
) : DetailQuestionRepository {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun getQuestionById(id: Long): PublicQuestionEntity {
        return try {
            dataToDomainMapper.getPublicQuestionEntity(apiService.getQuestionById(id))
        } catch (e: Exception) {
            Timber.e("Ошибка при загрузке вопроса")
            throw e
        }
    }
}