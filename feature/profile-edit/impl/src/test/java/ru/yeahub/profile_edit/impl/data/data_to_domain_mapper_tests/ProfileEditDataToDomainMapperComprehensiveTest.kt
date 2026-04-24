package ru.yeahub.profile_edit.impl.data.data_to_domain_mapper_tests

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.network_api.models.GetProfileForUserResponse
import ru.yeahub.network_api.models.GetSkillResponse
import ru.yeahub.network_api.models.GetSpecializationResponse
import ru.yeahub.network_api.models.GetUserProfileResponse
import ru.yeahub.network_api.models.SocialNetworkDto
import ru.yeahub.profile_edit.impl.data.ProfileEditDataToDomainMapper
import ru.yeahub.profile_edit.impl.data.skillResponse
import ru.yeahub.profile_edit.impl.data.specializationResponse
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditData
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSocialPlatform
import ru.yeahub.test.TestArgumentsProvider

class ProfileEditDataToDomainMapperComprehensiveTest {

    private val mapper = ProfileEditDataToDomainMapper()

    @ParameterizedTest
    @ArgumentsSource(ProfileEditDataToDomainMapperComprehensiveArgumentsProvider::class)
    fun `should map profile edit data to domain with all fields`(
        testCase: ProfileEditDataToDomainMapperComprehensiveTestCase,
    ) {
        val result = mapper.mapProfileToDomain(
            user = testCase.user,
            activeProfile = testCase.activeProfile,
            allSkills = testCase.allSkills,
            specializations = testCase.specializations,
        )

        assertEquals(testCase.expected, result)
    }

    data class ProfileEditDataToDomainMapperComprehensiveTestCase(
        val user: GetUserProfileResponse,
        val activeProfile: GetProfileForUserResponse,
        val allSkills: List<GetSkillResponse>,
        val specializations: List<GetSpecializationResponse>,
        val expected: DomainProfileEditData,
    )

    class ProfileEditDataToDomainMapperComprehensiveArgumentsProvider :
        TestArgumentsProvider<ProfileEditDataToDomainMapperComprehensiveTestCase>() {
        override fun testCases() = listOf(
            ProfileEditDataToDomainMapperComprehensiveTestCase(
                user = GetUserProfileResponse(
                    id = "user-1",
                    username = "john_doe",
                    email = "john@yeahub.ru",
                    telegramUsername = null,
                    phone = null,
                    country = "Russia",
                    city = "Saint Petersburg",
                    birthday = "1990-01-01",
                    address = "Nevsky, 1",
                    avatarUrl = "https://example.com/avatar.png",
                    createdAt = null,
                    updatedAt = null,
                    isVerified = null,
                    userRoles = null,
                    profiles = emptyList(),
                    subscriptions = null,
                ),
                activeProfile = GetProfileForUserResponse(
                    id = "profile-1",
                    profileType = 2,
                    specializationId = 5L,
                    markingWeight = 10,
                    description = "<p>Hello <strong>world</strong><br>second line</p><p>Next paragraph</p>",
                    socialNetwork = listOf(
                        SocialNetworkDto(
                            code = "github",
                            title = "https://github.com/john",
                        ),
                        SocialNetworkDto(
                            code = "telegram",
                            title = "https://t.me/john",
                        ),
                        SocialNetworkDto(
                            code = "discord",
                            title = "https://discord.gg/john",
                        ),
                    ),
                    imageSrc = "https://example.com/profile.png",
                    isActive = true,
                    profileSkills = listOf(
                        skillResponse(id = 1L, title = "Kotlin", imageSrc = "kotlin.png"),
                        skillResponse(id = 2L, title = "Compose", imageSrc = null),
                    ),
                ),
                allSkills = listOf(
                    skillResponse(id = 1L, title = "Kotlin", imageSrc = "kotlin.png"),
                    skillResponse(id = 2L, title = "Compose", imageSrc = null),
                    skillResponse(id = 3L, title = "Coroutines", imageSrc = "coroutines.png"),
                ),
                specializations = listOf(
                    specializationResponse(id = 5L, title = "Android"),
                    specializationResponse(id = 7L, title = "iOS"),
                ),
                expected = DomainProfileEditData(
                    email = "john@yeahub.ru",
                    avatarUrl = "https://example.com/avatar.png",
                    nickname = "john_doe",
                    specialization = "Android",
                    specializationList = listOf("Android", "iOS"),
                    location = "Saint Petersburg",
                    socialLinks = mapOf(
                        DomainProfileEditSocialPlatform.GitHub to "https://github.com/john",
                        DomainProfileEditSocialPlatform.Telegram to "https://t.me/john",
                    ),
                    aboutMe = "Hello world\nsecond line\n\nNext paragraph",
                    selectedSkills = listOf(
                        DomainProfileEditSkill(imageUrl = "kotlin.png", name = "Kotlin"),
                        DomainProfileEditSkill(imageUrl = null, name = "Compose"),
                    ),
                    allSkills = listOf(
                        DomainProfileEditSkill(imageUrl = "kotlin.png", name = "Kotlin"),
                        DomainProfileEditSkill(imageUrl = null, name = "Compose"),
                        DomainProfileEditSkill(imageUrl = "coroutines.png", name = "Coroutines"),
                    ),
                ),
            ),
        )
    }
}
