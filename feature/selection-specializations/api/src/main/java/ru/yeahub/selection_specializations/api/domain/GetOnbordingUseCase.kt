package ru.yeahub.selection_specializations.api.domain

interface GetOnbordingUseCase {
    fun getNextRoute(specializationId: Int): String
}