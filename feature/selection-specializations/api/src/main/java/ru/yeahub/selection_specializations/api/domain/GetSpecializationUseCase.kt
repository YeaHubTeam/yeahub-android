package ru.yeahub.selection_specializations.api.domain

interface GetSpecializationUseCase {
    fun getNextRoute(specializationId: Int): String
}