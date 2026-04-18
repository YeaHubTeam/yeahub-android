package ru.yeahub.profile_edit.impl.data.domain_to_data_mapper_tests

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.profile_edit.impl.data.ProfileEditDomainToDataMapper
import ru.yeahub.profile_edit.impl.data.minimalDomainData
import ru.yeahub.profile_edit.impl.data.minimalProfile
import ru.yeahub.profile_edit.impl.data.minimalUser
import ru.yeahub.profile_edit.impl.data.specializationResponse
import ru.yeahub.test.TestArgumentsProvider

class ProfileEditDomainToDataMapperSpecializationIdTest {

    private val allSpecializations = listOf(
        specializationResponse(id = 5L, title = "Android"),
        specializationResponse(id = 7L, title = "iOS"),
    )

    @ParameterizedTest
    @ArgumentsSource(ProfileEditDomainToDataMapperSpecializationIdArgumentsProvider::class)
    fun `should resolve specialization id from list or fall back to cached`(
        testCase: ProfileEditDomainToDataMapperSpecializationIdTestCase,
    ) {
        val result = ProfileEditDomainToDataMapper().mapToUpdateProfileRequest(
            profile = minimalDomainData(specialization = testCase.profileSpecialization),
            description = "",
            cachedProfile = minimalProfile(specializationId = testCase.cachedSpecializationId),
            cachedUser = minimalUser(),
            cachedAllSkills = emptyList(),
            allSpecializations = allSpecializations,
        )
        assertEquals(testCase.expectedSpecializationId, result.specializationId)
    }

    data class ProfileEditDomainToDataMapperSpecializationIdTestCase(
        val profileSpecialization: String?,
        val cachedSpecializationId: Long?,
        val expectedSpecializationId: Long?,
    )

    class ProfileEditDomainToDataMapperSpecializationIdArgumentsProvider :
        TestArgumentsProvider<ProfileEditDomainToDataMapperSpecializationIdTestCase>() {
        override fun testCases() = listOf(
            ProfileEditDomainToDataMapperSpecializationIdTestCase(
                profileSpecialization = "Android",
                cachedSpecializationId = null,
                expectedSpecializationId = 5L,
            ),
            ProfileEditDomainToDataMapperSpecializationIdTestCase(
                profileSpecialization = "iOS",
                cachedSpecializationId = null,
                expectedSpecializationId = 7L,
            ),
            ProfileEditDomainToDataMapperSpecializationIdTestCase(
                profileSpecialization = null,
                cachedSpecializationId = null,
                expectedSpecializationId = null,
            ),
            ProfileEditDomainToDataMapperSpecializationIdTestCase(
                profileSpecialization = null,
                cachedSpecializationId = 99L,
                expectedSpecializationId = 99L,
            ),
        )
    }
}
