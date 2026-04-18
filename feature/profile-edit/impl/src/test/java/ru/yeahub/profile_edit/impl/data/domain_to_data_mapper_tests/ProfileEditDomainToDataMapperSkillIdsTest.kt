package ru.yeahub.profile_edit.impl.data.domain_to_data_mapper_tests

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.profile_edit.impl.data.ProfileEditDomainToDataMapper
import ru.yeahub.profile_edit.impl.data.minimalDomainData
import ru.yeahub.profile_edit.impl.data.minimalProfile
import ru.yeahub.profile_edit.impl.data.minimalUser
import ru.yeahub.profile_edit.impl.data.skillResponse
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.test.TestArgumentsProvider

class ProfileEditDomainToDataMapperSkillIdsTest {

    private val cache = listOf(
        skillResponse(id = 1L, title = "Kotlin"),
        skillResponse(id = 2L, title = "Java"),
        skillResponse(id = 3L, title = "Python"),
    )

    @ParameterizedTest
    @ArgumentsSource(ProfileEditDomainToDataMapperSkillIdsArgumentsProvider::class)
    fun `should resolve skill ids from cache and drop skills not found`(
        testCase: ProfileEditDomainToDataMapperSkillIdsTestCase,
    ) {
        val result = ProfileEditDomainToDataMapper().mapToUpdateProfileRequest(
            profile = minimalDomainData(selectedSkills = testCase.selectedSkills),
            description = "",
            cachedProfile = minimalProfile(),
            cachedUser = minimalUser(),
            cachedAllSkills = cache,
            allSpecializations = emptyList(),
        )
        assertEquals(testCase.expectedSkillIds, result.profileSkills)
    }

    data class ProfileEditDomainToDataMapperSkillIdsTestCase(
        val selectedSkills: List<DomainProfileEditSkill>,
        val expectedSkillIds: List<Long>,
    )

    class ProfileEditDomainToDataMapperSkillIdsArgumentsProvider :
        TestArgumentsProvider<ProfileEditDomainToDataMapperSkillIdsTestCase>() {
        override fun testCases() = listOf(
            ProfileEditDomainToDataMapperSkillIdsTestCase(
                selectedSkills = emptyList(),
                expectedSkillIds = emptyList(),
            ),
            ProfileEditDomainToDataMapperSkillIdsTestCase(
                selectedSkills = listOf(
                    DomainProfileEditSkill(imageUrl = null, name = "Kotlin"),
                    DomainProfileEditSkill(imageUrl = null, name = "Java"),
                ),
                expectedSkillIds = listOf(1L, 2L),
            ),
            ProfileEditDomainToDataMapperSkillIdsTestCase(
                selectedSkills = listOf(
                    DomainProfileEditSkill(imageUrl = null, name = "Kotlin"),
                    DomainProfileEditSkill(imageUrl = null, name = "Unknown"),
                ),
                expectedSkillIds = listOf(1L),
            ),
            ProfileEditDomainToDataMapperSkillIdsTestCase(
                selectedSkills = listOf(
                    DomainProfileEditSkill(imageUrl = null, name = "Unknown"),
                ),
                expectedSkillIds = emptyList(),
            ),
        )
    }
}
