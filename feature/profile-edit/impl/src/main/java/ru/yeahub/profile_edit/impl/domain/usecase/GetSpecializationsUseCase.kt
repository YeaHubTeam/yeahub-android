package ru.yeahub.profile_edit.impl.domain.usecase

internal interface GetSpecializationsUseCase {
    suspend operator fun invoke(): List<String>
}