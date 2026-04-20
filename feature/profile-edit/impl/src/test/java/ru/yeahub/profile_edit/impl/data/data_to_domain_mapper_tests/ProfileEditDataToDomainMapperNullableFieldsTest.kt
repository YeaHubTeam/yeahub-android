package ru.yeahub.profile_edit.impl.data.data_to_domain_mapper_tests

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.network_api.models.GetSkillResponse
import ru.yeahub.profile_edit.impl.data.ProfileEditDataToDomainMapper
import ru.yeahub.profile_edit.impl.data.minimalProfile
import ru.yeahub.profile_edit.impl.data.minimalUser
import ru.yeahub.test.TestArgumentsProvider

class ProfileEditDataToDomainMapperNullableFieldsTest {

    @ParameterizedTest
    @ArgumentsSource(ProfileEditDataToDomainMapperNullableFieldsArgumentsProvider::class)
    fun `should map nullable profile response fields to empty editable values`(
        testCase: ProfileEditDataToDomainMapperNullableFieldsTestCase,
    ) {
        val result = ProfileEditDataToDomainMapper().mapProfileToDomain(
            user = minimalUser(
                username = testCase.username,
                email = testCase.email,
                avatarUrl = testCase.avatarUrl,
                city = testCase.city,
            ),
            activeProfile = minimalProfile(
                description = testCase.description,
                socialNetwork = null,
                profileSkills = testCase.profileSkills,
            ),
            allSkills = emptyList(),
            specializations = emptyList(),
        )

        assertEquals(testCase.expectedNickname, result.nickname)
        assertEquals(testCase.expectedEmail, result.email)
        assertEquals(testCase.expectedAvatarUrl, result.avatarUrl)
        assertEquals(testCase.expectedLocation, result.location)
        assertEquals(testCase.expectedAboutMe, result.aboutMe)
        assertEquals(0, result.socialLinks.size)
        assertEquals(0, result.selectedSkills.size)
    }

    data class ProfileEditDataToDomainMapperNullableFieldsTestCase(
        val username: String?,
        val email: String?,
        val avatarUrl: String?,
        val city: String?,
        val description: String?,
        val profileSkills: List<GetSkillResponse>?,
        val expectedNickname: String,
        val expectedEmail: String?,
        val expectedAvatarUrl: String,
        val expectedLocation: String,
        val expectedAboutMe: String,
    )

    class ProfileEditDataToDomainMapperNullableFieldsArgumentsProvider :
        TestArgumentsProvider<ProfileEditDataToDomainMapperNullableFieldsTestCase>() {
        override fun testCases() = listOf(
            ProfileEditDataToDomainMapperNullableFieldsTestCase(
                username = null,
                email = null,
                avatarUrl = null,
                city = null,
                description = null,
                profileSkills = null,
                expectedNickname = "",
                expectedEmail = null,
                expectedAvatarUrl = "",
                expectedLocation = "",
                expectedAboutMe = "",
            ),
        )
    }
}
