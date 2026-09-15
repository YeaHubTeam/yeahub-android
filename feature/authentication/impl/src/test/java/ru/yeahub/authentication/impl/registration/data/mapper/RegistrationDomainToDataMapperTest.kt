package ru.yeahub.authentication.impl.registration.data.mapper

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationModel
import ru.yeahub.network_api.models.RegistrationRequestDto

class RegistrationDomainToDataMapperTest {

    private val mapper = RegistrationDomainToDataMapper()

    @Test
    fun `map RegistrationModel to RegistrationRequestDto correctly`() {
        val model = RegistrationModel(
            nickname = "testuser",
            email = "test@mail.ru",
            password = "Password123!",
            isMailingAccepted = true
        )

        val expected = RegistrationRequestDto(
            username = "testuser",
            email = "test@mail.ru",
            password = "Password123!"
        )

        val result = mapper.map(model)

        assertEquals(expected, result)
    }
}
