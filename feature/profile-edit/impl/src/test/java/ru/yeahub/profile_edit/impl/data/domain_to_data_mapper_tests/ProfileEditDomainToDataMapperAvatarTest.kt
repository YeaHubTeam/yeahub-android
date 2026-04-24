package ru.yeahub.profile_edit.impl.data.domain_to_data_mapper_tests

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.profile_edit.impl.data.PendingAvatarChange
import ru.yeahub.profile_edit.impl.data.ProfileEditDomainToDataMapper
import ru.yeahub.profile_edit.impl.data.minimalDomainData
import ru.yeahub.profile_edit.impl.data.minimalUser
import ru.yeahub.test.TestArgumentsProvider

class ProfileEditDomainToDataMapperAvatarTest {

    @ParameterizedTest
    @ArgumentsSource(ProfileEditDomainToDataMapperAvatarArgumentsProvider::class)
    internal fun `should handle pending avatar change in user request`(
        testCase: ProfileEditDomainToDataMapperAvatarTestCase,
    ) {
        val result = ProfileEditDomainToDataMapper().mapToUpdateUserRequest(
            profile = minimalDomainData(),
            cachedUser = minimalUser(avatarUrl = testCase.cachedAvatarUrl),
            pendingAvatarChange = testCase.pendingAvatarChange,
        )
        assertEquals(testCase.expectedAvatarUrl, result.avatarUrl)
        assertEquals(testCase.expectedAvatarImage, result.avatarImage)
    }

    internal data class ProfileEditDomainToDataMapperAvatarTestCase(
        val pendingAvatarChange: PendingAvatarChange,
        val cachedAvatarUrl: String,
        val expectedAvatarUrl: String?,
        val expectedAvatarImage: String?,
    )

    internal class ProfileEditDomainToDataMapperAvatarArgumentsProvider :
        TestArgumentsProvider<ProfileEditDomainToDataMapperAvatarTestCase>() {
        override fun testCases() = listOf(
            ProfileEditDomainToDataMapperAvatarTestCase(
                pendingAvatarChange = PendingAvatarChange.Delete,
                cachedAvatarUrl = "https://example.com/avatar.png",
                expectedAvatarUrl = "",
                expectedAvatarImage = null,
            ),
            ProfileEditDomainToDataMapperAvatarTestCase(
                pendingAvatarChange = PendingAvatarChange.None,
                cachedAvatarUrl = "https://example.com/avatar.png",
                expectedAvatarUrl = "https://example.com/avatar.png",
                expectedAvatarImage = null,
            ),
            ProfileEditDomainToDataMapperAvatarTestCase(
                pendingAvatarChange = PendingAvatarChange.Upload(avatarBase64 = "abc123base64"),
                cachedAvatarUrl = "https://example.com/avatar.png",
                expectedAvatarUrl = "https://example.com/avatar.png",
                expectedAvatarImage = "abc123base64",
            ),
        )
    }
}
