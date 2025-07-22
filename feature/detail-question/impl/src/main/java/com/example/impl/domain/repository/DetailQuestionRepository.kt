package com.example.impl.domain.repository

import com.example.impl.domain.models.PublicQuestionEntity

interface DetailQuestionRepository {
   suspend fun getQuestionById(id: Long) : PublicQuestionEntity
}