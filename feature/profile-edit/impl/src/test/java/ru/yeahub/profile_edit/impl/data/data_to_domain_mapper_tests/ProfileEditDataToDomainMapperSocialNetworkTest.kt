package ru.yeahub.profile_edit.impl.data.data_to_domain_mapper_tests

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.network_api.models.SocialNetworkDto
import ru.yeahub.profile_edit.impl.data.ProfileEditDataToDomainMapper
import ru.yeahub.profile_edit.impl.data.minimalProfile
import ru.yeahub.profile_edit.impl.data.minimalUser
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSocialPlatform
import ru.yeahub.test.TestArgumentsProvider

class ProfileEditDataToDomainMapperSocialNetworkTest {

    @ParameterizedTest
    @ArgumentsSource(ProfileEditDataToDomainMapperSocialNetworkArgumentsProvider::class)
    fun `should map social network codes to domain platforms`(
        testCase: ProfileEditDataToDomainMapperSocialNetworkTestCase,
    ) {
        val result = ProfileEditDataToDomainMapper().mapProfileToDomain(
            user = minimalUser(),
            activeProfile = minimalProfile(socialNetwork = testCase.socialNetwork),
            allSkills = emptyList(),
            specializations = emptyList(),
        )
        assertEquals(testCase.expectedSocialLinks, result.socialLinks)
    }

    data class ProfileEditDataToDomainMapperSocialNetworkTestCase(
        val socialNetwork: List<SocialNetworkDto>?,
        val expectedSocialLinks: Map<DomainProfileEditSocialPlatform, String>,
    )

    class ProfileEditDataToDomainMapperSocialNetworkArgumentsProvider :
        TestArgumentsProvider<ProfileEditDataToDomainMapperSocialNetworkTestCase>() {
        override fun testCases() = listOf(
            ProfileEditDataToDomainMapperSocialNetworkTestCase(
                socialNetwork = null,
                expectedSocialLinks = emptyMap(),
            ),
            ProfileEditDataToDomainMapperSocialNetworkTestCase(
                socialNetwork = listOf(
                    SocialNetworkDto(
                        code = "discord",
                        title = "https://discord.gg/user",
                    ),
                ),
                expectedSocialLinks = emptyMap(),
            ),
            ProfileEditDataToDomainMapperSocialNetworkTestCase(
                socialNetwork = listOf(
                    SocialNetworkDto(
                        code = "instagram",
                        title = "https://instagram.com/user",
                    ),
                ),
                expectedSocialLinks = mapOf(DomainProfileEditSocialPlatform.Instagram to "https://instagram.com/user"),
            ),
            ProfileEditDataToDomainMapperSocialNetworkTestCase(
                socialNetwork = listOf(
                    SocialNetworkDto(
                        code = "linkedin",
                        title = "https://linkedin.com/in/user",
                    ),
                ),
                expectedSocialLinks = mapOf(DomainProfileEditSocialPlatform.LinkedIn to "https://linkedin.com/in/user"),
            ),
            ProfileEditDataToDomainMapperSocialNetworkTestCase(
                socialNetwork = listOf(
                    SocialNetworkDto(
                        code = "twitter",
                        title = "https://twitter.com/user",
                    ),
                ),
                expectedSocialLinks = mapOf(DomainProfileEditSocialPlatform.Twitter to "https://twitter.com/user"),
            ),
            ProfileEditDataToDomainMapperSocialNetworkTestCase(
                socialNetwork = listOf(
                    SocialNetworkDto(
                        code = "github",
                        title = "https://github.com/user",
                    ),
                ),
                expectedSocialLinks = mapOf(DomainProfileEditSocialPlatform.GitHub to "https://github.com/user"),
            ),
            ProfileEditDataToDomainMapperSocialNetworkTestCase(
                socialNetwork = listOf(
                    SocialNetworkDto(
                        code = "behance",
                        title = "https://behance.net/user",
                    ),
                ),
                expectedSocialLinks = mapOf(DomainProfileEditSocialPlatform.Behance to "https://behance.net/user"),
            ),
            ProfileEditDataToDomainMapperSocialNetworkTestCase(
                socialNetwork = listOf(
                    SocialNetworkDto(
                        code = "whatsapp",
                        title = "https://wa.me/user",
                    ),
                ),
                expectedSocialLinks = mapOf(DomainProfileEditSocialPlatform.WhatsApp to "https://wa.me/user"),
            ),
            ProfileEditDataToDomainMapperSocialNetworkTestCase(
                socialNetwork = listOf(
                    SocialNetworkDto(
                        code = "telegram",
                        title = "https://t.me/user",
                    ),
                ),
                expectedSocialLinks = mapOf(DomainProfileEditSocialPlatform.Telegram to "https://t.me/user"),
            ),
            ProfileEditDataToDomainMapperSocialNetworkTestCase(
                socialNetwork = listOf(
                    SocialNetworkDto(
                        code = "facebook",
                        title = "https://facebook.com/user",
                    ),
                ),
                expectedSocialLinks = mapOf(DomainProfileEditSocialPlatform.Facebook to "https://facebook.com/user"),
            ),
            ProfileEditDataToDomainMapperSocialNetworkTestCase(
                socialNetwork = listOf(
                    SocialNetworkDto(
                        code = "youtube",
                        title = "https://youtube.com/@user",
                    ),
                ),
                expectedSocialLinks = mapOf(DomainProfileEditSocialPlatform.YouTube to "https://youtube.com/@user"),
            ),
            ProfileEditDataToDomainMapperSocialNetworkTestCase(
                socialNetwork = listOf(
                    SocialNetworkDto(code = "github", title = "https://github.com/user"),
                    SocialNetworkDto(code = "discord", title = "https://discord.gg/user"),
                    SocialNetworkDto(code = "telegram", title = "https://t.me/user"),
                ),
                expectedSocialLinks = mapOf(
                    DomainProfileEditSocialPlatform.GitHub to "https://github.com/user",
                    DomainProfileEditSocialPlatform.Telegram to "https://t.me/user",
                ),
            ),
        )
    }
}
