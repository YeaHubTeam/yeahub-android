package test

import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.profile.impl.domain.DomainUserProfile
import ru.yeahub.profile.impl.presentation.ProfileScreenMapper
import ru.yeahub.profile.impl.presentation.ProfileScreenState
import ru.yeahub.profile.impl.presentation.UserData
import ru.yeahub.profile.impl.presentation.VOSocialNetwork
import ru.yeahub.profile.impl.domain.SocialNetwork as DomainSocialNetwork
import ru.yeahub.test.TestArgumentsProvider

class ProfileScreenMapperTest {

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProvider::class)
    fun `getScreenState should map DomainUserProfile to ProfileScreenState Success`(
        testCase: ProfileScreenMapperTestCase,
    ) {
        val result = ProfileScreenMapper.getScreenState(
            userData = testCase.domainProfile
        )
        Assertions.assertEquals(testCase.expectedResult, result)
    }

    object MapperExampleDataClasses {
        val defaultDomainProfile = DomainUserProfile(
            id = "1",
            username = "john_doe",
            specialization = "Senior Android Developer",
            aboutMe = "Опытный Android разработчик",
            skills = persistentListOf("Kotlin", "Compose", "Coroutines"),
            avatarUrl = "https://example.com/avatar.jpg",
            roles = persistentListOf("Кандидат", "Ментор"),
            country = "Россия",
            city = "Москва",
            telegramUsername = "john_telegram",
            socialNetworks = persistentListOf(
                DomainSocialNetwork("instagram", "https://instagram.com/john_doe"),
                DomainSocialNetwork("linkedin", "https://linkedin.com/in/john-doe"),
                DomainSocialNetwork("github", "https://github.com/johndoe")
            )
        )

        val domainProfileWithNulls = DomainUserProfile(
            id = "2",
            username = "jane_doe",
            specialization = null,
            aboutMe = null,
            skills = persistentListOf(),
            avatarUrl = null,
            roles = persistentListOf(),
            country = null,
            city = null,
            telegramUsername = null,
            socialNetworks = persistentListOf()
        )

        val domainProfileWithEmptySocialNetworks = DomainUserProfile(
            id = "3",
            username = "alice",
            specialization = "iOS Developer",
            aboutMe = "iOS разработчик",
            skills = persistentListOf("Swift", "SwiftUI"),
            avatarUrl = null,
            roles = persistentListOf("Кандидат"),
            country = "Россия",
            city = "СПб",
            telegramUsername = null,
            socialNetworks = persistentListOf()
        )

        val domainProfileWithNullUrls = DomainUserProfile(
            id = "4",
            username = "bob",
            specialization = "Backend Developer",
            aboutMe = "Java разработчик",
            skills = persistentListOf("Java", "Spring"),
            avatarUrl = null,
            roles = persistentListOf("Кандидат"),
            country = "Россия",
            city = "Казань",
            telegramUsername = null,
            socialNetworks = persistentListOf(
                DomainSocialNetwork("instagram", null),
                DomainSocialNetwork("linkedin", null)
            )
        )

        val expectedFullProfile = ProfileScreenState.Success(
            userData = UserData(
                id = "1",
                username = "john_doe",
                specialization = "Senior Android Developer",
                aboutMe = "Опытный Android разработчик",
                skills = persistentListOf("Kotlin", "Compose", "Coroutines"),
                avatarUrl = "https://example.com/avatar.jpg",
                roles = persistentListOf("Кандидат", "Ментор"),
                country = "Россия",
                city = "Москва",
                telegramUsername = "john_telegram",
                socialNetworks = persistentListOf(
                    VOSocialNetwork("instagram", "https://instagram.com/john_doe"),
                    VOSocialNetwork("linkedin", "https://linkedin.com/in/john-doe"),
                    VOSocialNetwork("github", "https://github.com/johndoe")
                )
            )
        )

        val expectedProfileWithNulls = ProfileScreenState.Success(
            userData = UserData(
                id = "2",
                username = "jane_doe",
                specialization = null,
                aboutMe = null,
                skills = persistentListOf(),
                avatarUrl = null,
                roles = persistentListOf(),
                country = null,
                city = null,
                telegramUsername = null,
                socialNetworks = persistentListOf()
            )
        )

        val expectedProfileWithEmptySocialNetworks = ProfileScreenState.Success(
            userData = UserData(
                id = "3",
                username = "alice",
                specialization = "iOS Developer",
                aboutMe = "iOS разработчик",
                skills = persistentListOf("Swift", "SwiftUI"),
                avatarUrl = null,
                roles = persistentListOf("Кандидат"),
                country = "Россия",
                city = "СПб",
                telegramUsername = null,
                socialNetworks = persistentListOf()
            )
        )

        val expectedProfileWithNullUrls = ProfileScreenState.Success(
            userData = UserData(
                id = "4",
                username = "bob",
                specialization = "Backend Developer",
                aboutMe = "Java разработчик",
                skills = persistentListOf("Java", "Spring"),
                avatarUrl = null,
                roles = persistentListOf("Кандидат"),
                country = "Россия",
                city = "Казань",
                telegramUsername = null,
                socialNetworks = persistentListOf(
                    VOSocialNetwork("instagram", ""),
                    VOSocialNetwork("linkedin", "")
                )
            )
        )
    }

    data class ProfileScreenMapperTestCase(
        val domainProfile: DomainUserProfile,
        val expectedResult: ProfileScreenState.Success,
    )

    class ArgumentsProvider :
        TestArgumentsProvider<ProfileScreenMapperTestCase>() {
        override fun testCases(): List<ProfileScreenMapperTestCase> = listOf(
            ProfileScreenMapperTestCase(
                domainProfile = MapperExampleDataClasses.defaultDomainProfile,
                expectedResult = MapperExampleDataClasses.expectedFullProfile
            ),
            ProfileScreenMapperTestCase(
                domainProfile = MapperExampleDataClasses.domainProfileWithNulls,
                expectedResult = MapperExampleDataClasses.expectedProfileWithNulls
            ),
            ProfileScreenMapperTestCase(
                domainProfile = MapperExampleDataClasses.domainProfileWithEmptySocialNetworks,
                expectedResult = MapperExampleDataClasses.expectedProfileWithEmptySocialNetworks
            ),
            ProfileScreenMapperTestCase(
                domainProfile = MapperExampleDataClasses.domainProfileWithNullUrls,
                expectedResult = MapperExampleDataClasses.expectedProfileWithNullUrls
            )
        )
    }
}