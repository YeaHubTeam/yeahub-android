package test

import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.network_api.models.GetUserProfileResponse
import ru.yeahub.network_api.models.PermissionDto
import ru.yeahub.network_api.models.ProfileDto
import ru.yeahub.network_api.models.ProfileSkillDto
import ru.yeahub.network_api.models.SocialNetworkDto
import ru.yeahub.network_api.models.SpecializationDto
import ru.yeahub.network_api.models.UserRoleDto
import ru.yeahub.profile.impl.data.UserProfileDataToDomainMapper
import ru.yeahub.profile.impl.domain.DomainUserProfile
import ru.yeahub.profile.impl.domain.SocialNetwork
import ru.yeahub.test.TestArgumentsProvider

class UserProfileMapperTest {
    private val mapper = UserProfileDataToDomainMapper()

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProvider::class)
    fun `profile data to domain mapper test cases`(
        testCase: ProfileDataToDomainMapperTestCase,
    ) {
        val result = mapper.mapDataToDomain(testCase.dataToTest)
        Assertions.assertEquals(testCase.expectedResult, result)
    }

    class ArgumentsProvider :
        TestArgumentsProvider<ProfileDataToDomainMapperTestCase>() {
        override fun testCases(): List<ProfileDataToDomainMapperTestCase> = listOf(
            ProfileDataToDomainMapperTestCase(
                dataToTest = ProfileExampleDataClasses.fullUserResponse,
                expectedResult = ProfileExampleDataClasses.fullDomainProfile
            ),
            ProfileDataToDomainMapperTestCase(
                dataToTest = ProfileExampleDataClasses.userResponseWithoutActiveProfile,
                expectedResult = ProfileExampleDataClasses.domainProfileWithoutActive
            ),
            ProfileDataToDomainMapperTestCase(
                dataToTest = ProfileExampleDataClasses.userResponseWithEmptyLists,
                expectedResult = ProfileExampleDataClasses.domainProfileWithEmptyLists
            ),
            ProfileDataToDomainMapperTestCase(
                dataToTest = ProfileExampleDataClasses.userResponseWithNulls,
                expectedResult = ProfileExampleDataClasses.domainProfileWithNulls
            ),
            ProfileDataToDomainMapperTestCase(
                dataToTest = ProfileExampleDataClasses.userResponseWithMultipleActive,
                expectedResult = ProfileExampleDataClasses.domainProfileFromFirstActive
            ),
            ProfileDataToDomainMapperTestCase(
                dataToTest = ProfileExampleDataClasses.userResponseWithDifferentCityFormats,
                expectedResult = ProfileExampleDataClasses.domainProfileWithParsedCity
            ),
            ProfileDataToDomainMapperTestCase(
                dataToTest = ProfileExampleDataClasses.userResponseWithHtmlDescription,
                expectedResult = ProfileExampleDataClasses.domainProfileWithCleanDescription
            )
        )
    }

    object ProfileExampleDataClasses {
        val defaultPermission = PermissionDto(
            id = 1,
            name = "view"
        )

        val defaultUserRole = UserRoleDto(
            id = 6,
            name = "candidate-free",
            permissions = listOf(defaultPermission)
        )

        val defaultSpecialization = SpecializationDto(
            id = 20,
            title = "Java Backend Developer",
            description = "Java разработчик",
            imageSrc = null,
            createdAt = "2024-12-07T18:53:51.891Z",
            updatedAt = "2024-12-07T18:53:51.891Z"
        )

        val defaultProfileSkill = ProfileSkillDto(
            id = 39,
            title = "Spring",
            description = "Spring Framework",
            imageSrc = "https://example.com/spring.jpg",
            createdAt = "2024-12-14T23:14:06.710Z",
            updatedAt = "2025-03-24T12:40:19.334Z",
            specializations = listOf(defaultSpecialization)
        )

        val defaultSocialNetwork = SocialNetworkDto(
            code = "instagram",
            title = "https://instagram.com/john_doe"
        )

        val defaultLinkedInNetwork = SocialNetworkDto(
            code = "linkedin",
            title = "https://linkedin.com/in/john-doe"
        )

        val defaultGithubNetwork = SocialNetworkDto(
            code = "github",
            title = "https://github.com/johndoe"
        )

        val defaultActiveProfile = ProfileDto(
            id = "de5ae341-0615-4be9-ac2b-e6ff2cc446d2",
            profileType = 1,
            specializationId = 19,
            markingWeight = 1,
            description = "<p>первая строка</p><p>вторая строка</p>",
            socialNetwork = listOf(defaultSocialNetwork, defaultLinkedInNetwork),
            imageSrc = null,
            isActive = true,
            profileSkills = listOf(defaultProfileSkill)
        )

        val defaultInactiveProfile = ProfileDto(
            id = "75a768a3-e465-4086-b929-11136e615750",
            profileType = 1,
            specializationId = 0,
            markingWeight = 1,
            description = null,
            socialNetwork = null,
            imageSrc = null,
            isActive = false,
            profileSkills = emptyList()
        )

        val fullUserResponse = GetUserProfileResponse(
            id = "baa89faa-2f76-4d7b-a8ff-254c09ce5f9e",
            username = "john_doe",
            telegramUsername = "john_telegram",
            phone = "+1234567890",
            country = null,
            city = "Россия, Москва",
            email = "john@example.com",
            birthday = null,
            address = "ул. Ленина, д.1",
            avatarUrl = "https://example.com/avatar.jpg",
            createdAt = "2025-12-16T16:50:25.926Z",
            updatedAt = "2025-12-24T10:37:08.645Z",
            isVerified = true,
            userRoles = listOf(defaultUserRole),
            profiles = listOf(defaultActiveProfile, defaultInactiveProfile),
            subscriptions = emptyList()
        )

        val fullDomainProfile = DomainUserProfile(
            id = "baa89faa-2f76-4d7b-a8ff-254c09ce5f9e",
            username = "john_doe",
            avatarUrl = "https://example.com/avatar.jpg",
            city = "Москва",
            country = "Россия",
            telegramUsername = "john_telegram",
            aboutMe = "первая строкавторая строка",
            roles = persistentListOf("candidate-free"),
            skills = persistentListOf("Spring"),
            specialization = "Java Backend Developer",
            socialNetworks = persistentListOf(
                SocialNetwork("instagram", url = "https://instagram.com/john_doe"),
                SocialNetwork("linkedin", url = "https://linkedin.com/in/john-doe")
            )
        )

        val userResponseWithoutActiveProfile = GetUserProfileResponse(
            id = "baa89faa-2f76-4d7b-a8ff-254c09ce5f9e",
            username = "john_doe",
            telegramUsername = null,
            phone = null,
            country = null,
            city = "Россия, Москва",
            email = "john@example.com",
            birthday = null,
            address = "",
            avatarUrl = null,
            createdAt = "2025-12-16T16:50:25.926Z",
            updatedAt = "2025-12-24T10:37:08.645Z",
            isVerified = true,
            userRoles = listOf(defaultUserRole),
            profiles = listOf(defaultInactiveProfile),
            subscriptions = emptyList()
        )

        val domainProfileWithoutActive = DomainUserProfile(
            id = "baa89faa-2f76-4d7b-a8ff-254c09ce5f9e",
            username = "john_doe",
            avatarUrl = null,
            city = "Москва",
            country = "Россия",
            telegramUsername = null,
            aboutMe = null,
            roles = persistentListOf("candidate-free"),
            skills = persistentListOf(),
            specialization = null,
            socialNetworks = persistentListOf()
        )

        val userResponseWithEmptyLists = GetUserProfileResponse(
            id = "baa89faa-2f76-4d7b-a8ff-254c09ce5f9e",
            username = "john_doe",
            telegramUsername = null,
            phone = null,
            country = null,
            city = null,
            email = null,
            birthday = null,
            address = null,
            avatarUrl = null,
            createdAt = "2025-12-16T16:50:25.926Z",
            updatedAt = "2025-12-24T10:37:08.645Z",
            isVerified = true,
            userRoles = emptyList(),
            profiles = listOf(
                defaultActiveProfile.copy(
                    socialNetwork = emptyList(),
                    profileSkills = emptyList()
                )
            ),
            subscriptions = emptyList()
        )

        val domainProfileWithEmptyLists = DomainUserProfile(
            id = "baa89faa-2f76-4d7b-a8ff-254c09ce5f9e",
            username = "john_doe",
            avatarUrl = null,
            city = null,
            country = null,
            telegramUsername = null,
            aboutMe = "первая строкавторая строка",
            roles = persistentListOf(),
            skills = persistentListOf(),
            specialization = null,
            socialNetworks = persistentListOf()
        )

        val userResponseWithNulls = GetUserProfileResponse(
            id = "baa89faa-2f76-4d7b-a8ff-254c09ce5f9e",
            username = "john_doe",
            telegramUsername = null,
            phone = null,
            country = null,
            city = null,
            email = null,
            birthday = null,
            address = null,
            avatarUrl = null,
            createdAt = "2025-12-16T16:50:25.926Z",
            updatedAt = "2025-12-24T10:37:08.645Z",
            isVerified = true,
            userRoles = emptyList(),
            profiles = listOf(
                defaultActiveProfile.copy(
                    description = null,
                    socialNetwork = null
                )
            ),
            subscriptions = emptyList()
        )

        val domainProfileWithNulls = DomainUserProfile(
            id = "baa89faa-2f76-4d7b-a8ff-254c09ce5f9e",
            username = "john_doe",
            avatarUrl = null,
            city = null,
            country = null,
            telegramUsername = null,
            aboutMe = null,
            roles = persistentListOf(),
            skills = persistentListOf("Spring"),
            specialization = "Java Backend Developer",
            socialNetworks = persistentListOf()
        )

        val userResponseWithMultipleActive = GetUserProfileResponse(
            id = "baa89faa-2f76-4d7b-a8ff-254c09ce5f9e",
            username = "john_doe",
            telegramUsername = null,
            phone = null,
            country = null,
            city = "Россия, Москва",
            email = null,
            birthday = null,
            address = null,
            avatarUrl = null,
            createdAt = "2025-12-16T16:50:25.926Z",
            updatedAt = "2025-12-24T10:37:08.645Z",
            isVerified = true,
            userRoles = listOf(defaultUserRole),
            profiles = listOf(
                defaultActiveProfile,
                defaultActiveProfile.copy(
                    id = "second-active",
                    socialNetwork = listOf(
                        defaultGithubNetwork
                    ),
                    profileSkills = listOf(
                        defaultProfileSkill.copy(
                            title = "Kotlin",
                            specializations = listOf(
                                defaultSpecialization.copy(
                                    title = "Kotlin Developer"
                                )
                            )
                        )
                    )
                )
            ),
            subscriptions = emptyList()
        )

        val domainProfileFromFirstActive = DomainUserProfile(
            id = "baa89faa-2f76-4d7b-a8ff-254c09ce5f9e",
            username = "john_doe",
            avatarUrl = null,
            city = "Москва",
            country = "Россия",
            telegramUsername = null,
            aboutMe = "первая строкавторая строка",
            roles = persistentListOf("candidate-free"),
            skills = persistentListOf("Spring"),
            specialization = "Java Backend Developer",
            socialNetworks = persistentListOf(
                SocialNetwork(code = "instagram", url = "https://instagram.com/john_doe"),
                SocialNetwork(code = "linkedin", url = "https://linkedin.com/in/john-doe")
            )
        )

        val userResponseWithDifferentCityFormats = GetUserProfileResponse(
            id = "baa89faa-2f76-4d7b-a8ff-254c09ce5f9e",
            username = "john_doe",
            telegramUsername = null,
            phone = null,
            country = null,
            city = cityForTest,
            email = null,
            birthday = null,
            address = null,
            avatarUrl = null,
            createdAt = "2025-12-16T16:50:25.926Z",
            updatedAt = "2025-12-24T10:37:08.645Z",
            isVerified = true,
            userRoles = listOf(defaultUserRole),
            profiles = listOf(
                defaultActiveProfile.copy(
                    description = null,
                    socialNetwork = null,
                    profileSkills = emptyList()
                )
            ),
            subscriptions = emptyList()
        )

        val domainProfileWithParsedCity = DomainUserProfile(
            id = "baa89faa-2f76-4d7b-a8ff-254c09ce5f9e",
            username = "john_doe",
            avatarUrl = null,
            city = expectedCity,
            country = expectedCountry,
            telegramUsername = null,
            aboutMe = null,
            roles = persistentListOf("candidate-free"),
            skills = persistentListOf(),
            specialization = null,
            socialNetworks = persistentListOf()
        )

        private const val cityForTest = "Россия, Москва"
        private const val expectedCountry = "Россия"
        private const val expectedCity = "Москва"

        val userResponseWithHtmlDescription = GetUserProfileResponse(
            id = "baa89faa-2f76-4d7b-a8ff-254c09ce5f9e",
            username = "john_doe",
            telegramUsername = null,
            phone = null,
            country = null,
            city = "Россия, Москва",
            email = null,
            birthday = null,
            address = null,
            avatarUrl = null,
            createdAt = "2025-12-16T16:50:25.926Z",
            updatedAt = "2025-12-24T10:37:08.645Z",
            isVerified = true,
            userRoles = listOf(defaultUserRole),
            profiles = listOf(
                defaultActiveProfile.copy(
                    description = "<p>строка 1</p><p>строка 2</p><p>строка 3</p>"
                )
            ),
            subscriptions = emptyList()
        )

        val domainProfileWithCleanDescription = DomainUserProfile(
            id = "baa89faa-2f76-4d7b-a8ff-254c09ce5f9e",
            username = "john_doe",
            avatarUrl = null,
            city = "Москва",
            country = "Россия",
            telegramUsername = null,
            aboutMe = "строка 1строка 2строка 3",
            roles = persistentListOf("candidate-free"),
            skills = persistentListOf("Spring"),
            specialization = "Java Backend Developer",
            socialNetworks = persistentListOf(
                SocialNetwork(code = "instagram", url = "https://instagram.com/john_doe"),
                SocialNetwork(code = "linkedin", url = "https://linkedin.com/in/john-doe")
            )
        )
    }

    data class ProfileDataToDomainMapperTestCase(
        val dataToTest: GetUserProfileResponse,
        val expectedResult: DomainUserProfile,
    )
}