package ru.yeahub.selection_specializations.impl.domain

import ru.yeahub.selection_specializations.api.domain.GetOnbordingUseCase

class GetOnbordingUseCaseImpl : GetOnbordingUseCase {
    override fun getNextRoute(specializationId: Int): String =
        "home/collections/by-specialization/$specializationId"
}