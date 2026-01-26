package test

import ru.yeahub.interview_trainer.impl.createQuiz.domain.DomainSpecialization
import ru.yeahub.interview_trainer.impl.createQuiz.domain.DomainSpecializationListResponse
import ru.yeahub.interview_trainer.impl.createQuiz.presentation.CreateQuizState
import ru.yeahub.network_api.models.GetSpecializationResponse
import ru.yeahub.network_api.models.GetSpecializationsResponse

object SpecializationExampleDataClasses {
    val defaultSpecialResponse = GetSpecializationResponse(
        id = 0L,
        title = "default title num 0",
        description = "default description",
        imageSrc = null,
        createdAt = "01.01.1970",
        updatedAt = "01.01.1970"
    )

    val defaultSpecialResponseWithImage = GetSpecializationResponse(
        id = 1L,
        title = "default title num 1",
        description = "default description",
        imageSrc = "some image url",
        createdAt = "01.01.1970",
        updatedAt = "01.01.1970"
    )

    val defaultDomainSpecial = DomainSpecialization(
        id = 0L,
        title = "default title num 0"
    )

    val defaultDomainSpecialWithImage = DomainSpecialization(
        id = 1L,
        title = "default title num 1"
    )

    val defaultSpecialListResponse = GetSpecializationsResponse(
        page = 1L,
        limit = 10L,
        data = listOf(defaultSpecialResponse, defaultSpecialResponseWithImage),
        total = 2L
    )

    val defaultDomainSpecialListResponse = DomainSpecializationListResponse(
        page = 1L,
        limit = 10L,
        data = listOf(defaultDomainSpecial, defaultDomainSpecialWithImage),
        total = 2L
    )

    val defaultVoSpecialization = CreateQuizState.Loaded.VoSpecialization(
        id = 0,
        title = "default title num 0"
    )

    val defaultVoSpecializationWithImage = CreateQuizState.Loaded.VoSpecialization(
        id = 1,
        title = "default title num 1"
    )
}
