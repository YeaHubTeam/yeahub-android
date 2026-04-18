package ru.yeahub.profile_edit.impl.data.domain_to_data_mapper_tests

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.network_api.models.GetProfileForUserResponse
import ru.yeahub.network_api.models.GetSkillResponse
import ru.yeahub.network_api.models.GetSpecializationResponse
import ru.yeahub.network_api.models.GetUserProfileResponse
import ru.yeahub.network_api.models.SocialNetworkDto
import ru.yeahub.network_api.models.UpdateProfileRequest
import ru.yeahub.network_api.models.UpdateUserRequest
import ru.yeahub.profile_edit.impl.data.ProfileEditDomainToDataMapper
import ru.yeahub.profile_edit.impl.data.minimalProfile
import ru.yeahub.profile_edit.impl.data.minimalUser
import ru.yeahub.profile_edit.impl.data.skillResponse
import ru.yeahub.profile_edit.impl.data.specializationResponse
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditData
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSocialPlatform
import ru.yeahub.test.TestArgumentsProvider

class ProfileEditDomainToDataMapperComprehensiveTest {

    private val mapper = ProfileEditDomainToDataMapper()

    @ParameterizedTest
    @ArgumentsSource(ProfileEditDomainToDataMapperComprehensiveArgumentsProvider::class)
    fun `should map profile edit domain data to update requests with all fields`(
        testCase: ProfileEditDomainToDataMapperComprehensiveTestCase,
    ) {
        val description = mapper.mapAboutMeToHtml(testCase.profile.aboutMe)
        val updateProfileRequest = mapper.mapToUpdateProfileRequest(
            profile = testCase.profile,
            description = description,
            cachedProfile = testCase.cachedProfile,
            cachedUser = testCase.cachedUser,
            cachedAllSkills = testCase.cachedAllSkills,
            allSpecializations = testCase.allSpecializations,
        )
        val updateUserRequest = mapper.mapToUpdateUserRequest(
            profile = testCase.profile,
            cachedUser = testCase.cachedUser,
            avatarBase64 = testCase.avatarBase64,
            avatarDeleted = testCase.avatarDeleted,
        )

        assertEquals(testCase.expectedProfileRequest, updateProfileRequest)
        assertEquals(testCase.expectedUserRequest, updateUserRequest)
    }

    data class ProfileEditDomainToDataMapperComprehensiveTestCase(
        val profile: DomainProfileEditData,
        val cachedProfile: GetProfileForUserResponse,
        val cachedUser: GetUserProfileResponse,
        val cachedAllSkills: List<GetSkillResponse>,
        val allSpecializations: List<GetSpecializationResponse>,
        val avatarBase64: String?,
        val avatarDeleted: Boolean,
        val expectedProfileRequest: UpdateProfileRequest,
        val expectedUserRequest: UpdateUserRequest,
    )

    class ProfileEditDomainToDataMapperComprehensiveArgumentsProvider :
        TestArgumentsProvider<ProfileEditDomainToDataMapperComprehensiveTestCase>() {
        override fun testCases() = listOf(
            ProfileEditDomainToDataMapperComprehensiveTestCase(
                profile = DomainProfileEditData(
                    email = "john@yeahub.ru",
                    avatarUrl = "content://avatar/new",
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
                cachedProfile = minimalProfile(
                    specializationId = 99L,
                    description = "<p>Old description</p>",
                ).copy(
                    profileType = 2,
                    markingWeight = 10,
                    imageSrc = "https://example.com/profile.png",
                    isActive = true,
                ),
                cachedUser = minimalUser(
                    username = "old_name",
                    email = "john@yeahub.ru",
                    avatarUrl = "https://example.com/avatar.png",
                    city = "Moscow",
                    country = "Russia",
                    birthday = "1990-01-01",
                    address = "Nevsky, 1",
                ),
                cachedAllSkills = listOf(
                    skillResponse(id = 1L, title = "Kotlin", imageSrc = "kotlin.png"),
                    skillResponse(id = 2L, title = "Compose", imageSrc = null),
                    skillResponse(id = 3L, title = "Coroutines", imageSrc = "coroutines.png"),
                ),
                allSpecializations = listOf(
                    specializationResponse(id = 5L, title = "Android"),
                    specializationResponse(id = 7L, title = "iOS"),
                ),
                avatarBase64 = "base64-avatar",
                avatarDeleted = false,
                expectedProfileRequest = UpdateProfileRequest(
                    profileType = 2,
                    specializationId = 5L,
                    markingWeight = 10,
                    description = "<p>Hello world<br>second line</p><p>Next paragraph</p>",
                    socialNetwork = listOf(
                        SocialNetworkDto(
                            code = "github",
                            title = "https://github.com/john",
                        ),
                        SocialNetworkDto(
                            code = "telegram",
                            title = "https://t.me/john",
                        ),
                    ),
                    imageSrc = "https://example.com/profile.png",
                    isActive = true,
                    profileSkills = listOf(1L, 2L),
                    user = minimalUser(
                        username = "old_name",
                        email = "john@yeahub.ru",
                        avatarUrl = "https://example.com/avatar.png",
                        city = "Moscow",
                        country = "Russia",
                        birthday = "1990-01-01",
                        address = "Nevsky, 1",
                    ),
                ),
                expectedUserRequest = UpdateUserRequest(
                    username = "john_doe",
                    country = "Russia",
                    city = "Saint Petersburg",
                    birthday = "1990-01-01",
                    address = "Nevsky, 1",
                    avatarUrl = "https://example.com/avatar.png",
                    avatarImage = "base64-avatar",
                ),
            ),
        )
    }
}
