package ru.yeahub.selection_specializations.impl.domain

import ru.yeahub.selection_specializations.api.domain.GetSpecializationUseCase

class GetSpecializationUseCaseImpl : GetSpecializationUseCase {
    override fun getNextRoute(specializationId: Int): String =
        "home/questions/by-specialization/$specializationId"
}