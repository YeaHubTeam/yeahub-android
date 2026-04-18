package ru.yeahub.profile_edit.impl.data.domain_to_data_mapper_tests

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.profile_edit.impl.data.ProfileEditDomainToDataMapper
import ru.yeahub.profile_edit.impl.data.minimalDomainData
import ru.yeahub.profile_edit.impl.data.minimalUser
import ru.yeahub.test.TestArgumentsProvider

class ProfileEditDomainToDataMapperAvatarTest {

    @ParameterizedTest
    @ArgumentsSource(ProfileEditDomainToDataMapperAvatarArgumentsProvider::class)
    fun `should handle avatar deletion and base64 in user request`(
        testCase: ProfileEditDomainToDataMapperAvatarTestCase,
    ) {
        val result = ProfileEditDomainToDataMapper().mapToUpdateUserRequest(
            profile = minimalDomainData(),
            cachedUser = minimalUser(avatarUrl = testCase.cachedAvatarUrl),
            avatarBase64 = testCase.avatarBase64,
            avatarDeleted = testCase.avatarDeleted,
        )
        assertEquals(testCase.expectedAvatarUrl, result.avatarUrl)
        assertEquals(testCase.expectedAvatarImage, result.avatarImage)
    }

    data class ProfileEditDomainToDataMapperAvatarTestCase(
        val avatarDeleted: Boolean,
        val avatarBase64: String?,
        val cachedAvatarUrl: String,
        val expectedAvatarUrl: String?,
        val expectedAvatarImage: String?,
    )

    class ProfileEditDomainToDataMapperAvatarArgumentsProvider :
        TestArgumentsProvider<ProfileEditDomainToDataMapperAvatarTestCase>() {
        override fun testCases() = listOf(
            ProfileEditDomainToDataMapperAvatarTestCase(
                avatarDeleted = true,
                avatarBase64 = null,
                cachedAvatarUrl = "https://example.com/avatar.png",
                expectedAvatarUrl = "",
                expectedAvatarImage = null,
            ),
            ProfileEditDomainToDataMapperAvatarTestCase(
                avatarDeleted = false,
                avatarBase64 = null,
                cachedAvatarUrl = "https://example.com/avatar.png",
                expectedAvatarUrl = "https://example.com/avatar.png",
                expectedAvatarImage = null,
            ),
            ProfileEditDomainToDataMapperAvatarTestCase(
                avatarDeleted = false,
                avatarBase64 = "abc123base64",
                cachedAvatarUrl = "https://example.com/avatar.png",
                expectedAvatarUrl = "https://example.com/avatar.png",
                expectedAvatarImage = "abc123base64",
            ),
        )
    }
}
