package ru.yeahub.profile_edit.impl.data.data_to_domain_mapper_tests

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.network_api.models.GetSkillResponse
import ru.yeahub.profile_edit.impl.data.ProfileEditDataToDomainMapper
import ru.yeahub.profile_edit.impl.data.minimalProfile
import ru.yeahub.profile_edit.impl.data.minimalUser
import ru.yeahub.profile_edit.impl.data.skillResponse
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.test.TestArgumentsProvider

class ProfileEditDataToDomainMapperSkillsTest {

    @ParameterizedTest
    @ArgumentsSource(ProfileEditDataToDomainMapperSkillsArgumentsProvider::class)
    fun `should map skill responses to domain skills`(
        testCase:
        ProfileEditDataToDomainMapperSkillsTestCase,
    ) {
        val result = ProfileEditDataToDomainMapper().mapProfileToDomain(
            user = minimalUser(),
            activeProfile = minimalProfile(),
            allSkills = testCase.inputSkills,
            specializations = emptyList(),
        )
        assertEquals(testCase.expectedSkills, result.allSkills)
    }

    data class ProfileEditDataToDomainMapperSkillsTestCase(
        val inputSkills: List<GetSkillResponse>,
        val expectedSkills: List<DomainProfileEditSkill>,
    )

    class ProfileEditDataToDomainMapperSkillsArgumentsProvider :
        TestArgumentsProvider<ProfileEditDataToDomainMapperSkillsTestCase>() {
        override fun testCases() = listOf(
            ProfileEditDataToDomainMapperSkillsTestCase(
                inputSkills = emptyList(),
                expectedSkills = emptyList(),
            ),
            ProfileEditDataToDomainMapperSkillsTestCase(
                inputSkills = listOf(
                    skillResponse(
                        id = 1L,
                        title = "Kotlin",
                        imageSrc = "kotlin.png",
                    ),
                ),
                expectedSkills = listOf(
                    DomainProfileEditSkill(
                        imageUrl = "kotlin.png",
                        name = "Kotlin",
                    ),
                ),
            ),
            ProfileEditDataToDomainMapperSkillsTestCase(
                inputSkills = listOf(skillResponse(id = 2L, title = "Java", imageSrc = null)),
                expectedSkills = listOf(DomainProfileEditSkill(imageUrl = null, name = "Java")),
            ),
            ProfileEditDataToDomainMapperSkillsTestCase(
                inputSkills = listOf(
                    skillResponse(id = 1L, title = "Kotlin", imageSrc = "kotlin.png"),
                    skillResponse(id = 2L, title = "Java", imageSrc = null),
                    skillResponse(id = 3L, title = "Python", imageSrc = "python.png"),
                ),
                expectedSkills = listOf(
                    DomainProfileEditSkill(imageUrl = "kotlin.png", name = "Kotlin"),
                    DomainProfileEditSkill(imageUrl = null, name = "Java"),
                    DomainProfileEditSkill(imageUrl = "python.png", name = "Python"),
                ),
            ),
        )
    }
}
