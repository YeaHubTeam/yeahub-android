package com.example.impl.domain.usecase

import com.example.impl.domain.models.PublicQuestionEntity

interface GetQuestionByIdUseCase {
    suspend operator fun invoke(id: Long) : PublicQuestionEntity
}