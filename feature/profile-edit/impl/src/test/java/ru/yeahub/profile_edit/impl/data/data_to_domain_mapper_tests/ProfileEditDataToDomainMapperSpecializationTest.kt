package ru.yeahub.profile_edit.impl.data.data_to_domain_mapper_tests

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.profile_edit.impl.data.ProfileEditDataToDomainMapper
import ru.yeahub.profile_edit.impl.data.minimalProfile
import ru.yeahub.profile_edit.impl.data.minimalUser
import ru.yeahub.profile_edit.impl.data.specializationResponse
import ru.yeahub.test.TestArgumentsProvider

class ProfileEditDataToDomainMapperSpecializationTest {

    @ParameterizedTest
    @ArgumentsSource(ProfileEditDataToDomainMapperSpecializationArgumentsProvider::class)
    fun `should resolve specialization name by id`(
        testCase: ProfileEditDataToDomainMapperSpecializationTestCase,
    ) {
        val result = ProfileEditDataToDomainMapper().mapProfileToDomain(
            user = minimalUser(),
            activeProfile = minimalProfile(specializationId = testCase.specializationId),
            allSkills = emptyList(),
            specializations = listOf(
                specializationResponse(id = 1L, title = "Android"),
                specializationResponse(id = 2L, title = "iOS"),
            ),
        )
        assertEquals(testCase.expectedSpecialization, result.specialization)
    }

    data class ProfileEditDataToDomainMapperSpecializationTestCase(
        val specializationId: Long?,
        val expectedSpecialization: String?,
    )

    class ProfileEditDataToDomainMapperSpecializationArgumentsProvider :
        TestArgumentsProvider<ProfileEditDataToDomainMapperSpecializationTestCase>() {
        override fun testCases() = listOf(
            ProfileEditDataToDomainMapperSpecializationTestCase(
                specializationId = null,
                expectedSpecialization = null,
            ),
            ProfileEditDataToDomainMapperSpecializationTestCase(
                specializationId = 0L,
                expectedSpecialization = null,
            ),
            ProfileEditDataToDomainMapperSpecializationTestCase(
                specializationId = 1L,
                expectedSpecialization = "Android",
            ),
            ProfileEditDataToDomainMapperSpecializationTestCase(
                specializationId = 2L,
                expectedSpecialization = "iOS",
            ),
        )
    }
}
