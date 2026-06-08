package ru.yeahub.profile_edit.impl.data.repository_impl_tests

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.network_api.ApiService
import ru.yeahub.network_api.models.GetSkillsResponse
import ru.yeahub.network_api.models.GetSpecializationsResponse
import ru.yeahub.network_api.models.SocialNetworkDto
import ru.yeahub.network_api.models.UpdatePublicUserResponse
import ru.yeahub.profile_edit.impl.data.ProfileEditDataToDomainMapper
import ru.yeahub.profile_edit.impl.data.ProfileEditDomainToDataMapper
import ru.yeahub.profile_edit.impl.data.ProfileEditRepositoryImpl
import ru.yeahub.profile_edit.impl.data.minimalProfile
import ru.yeahub.profile_edit.impl.data.minimalUser
import ru.yeahub.profile_edit.impl.data.skillResponse
import ru.yeahub.profile_edit.impl.data.specializationResponse
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditData
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSocialPlatform
import ru.yeahub.test.TestArgumentsProvider

/**
 * Проверяет saveProfile() в ProfileEditRepositoryImpl.
 *
 * Сначала тест вызывает getProfileData(), чтобы repository заполнил cache теми же данными,
 * которые экран получает при открытии. Потом меняет форму по сценарию и проверяет,
 * какие update-методы ApiService были вызваны: только user, только profile, оба или ни один.
 */
class ProfileEditRepositorySaveProfileTest {

    @ParameterizedTest
    @ArgumentsSource(ProfileEditRepositorySaveProfileArgumentsProvider::class)
    internal fun `should send only changed profile edit requests`(
        testCase: ProfileEditRepositorySaveProfileTestCase,
    ) = runBlocking {
        val apiService = mockk<ApiService>()
        stubProfileLoad(apiService)
        stubProfileSave(apiService)
        val repository = ProfileEditRepositoryImpl(
            apiService = apiService,
            mapperDataToDomain = ProfileEditDataToDomainMapper(),
            mapperDomainToData = ProfileEditDomainToDataMapper(),
        )

        val loadedProfile = repository.getProfileData()
        if (testCase.shouldDeleteAvatar) {
            repository.markAvatarDeleted()
        }
        repository.saveProfile(testCase.transform(loadedProfile))

        coVerify(exactly = testCase.expectedProfileUpdates) {
            apiService.updateProfile("profile-1", any())
        }
        coVerify(exactly = testCase.expectedUserUpdates) {
            apiService.updateUser("user-1", any())
        }
    }

    private fun stubProfileLoad(apiService: ApiService) {
        coEvery {
            apiService.getSkills(page = 1, limit = 200)
        } returns GetSkillsResponse(
            page = 1,
            limit = 200,
            data = cachedSkills(),
            total = cachedSkills().size.toLong(),
        )
        coEvery {
            apiService.getSpecializations(page = 1, limit = 200)
        } returns GetSpecializationsResponse(
            page = 1,
            limit = 200,
            data = cachedSpecializations(),
            total = cachedSpecializations().size.toLong(),
        )
        coEvery { apiService.getProfile() } returns cachedUser()
    }

    private fun stubProfileSave(apiService: ApiService) {
        coEvery { apiService.updateProfile(any(), any()) } returns Unit
        coEvery { apiService.updateUser(any(), any()) } returns UpdatePublicUserResponse(
            id = "user-1",
            username = "john",
            email = "john@yeahub.ru",
            avatarUrl = "https://example.com/avatar.png",
            city = "Baku",
        )
    }

    internal data class ProfileEditRepositorySaveProfileTestCase(
        val name: String,
        val shouldDeleteAvatar: Boolean,
        val expectedProfileUpdates: Int,
        val expectedUserUpdates: Int,
        val transform: (DomainProfileEditData) -> DomainProfileEditData,
    ) {
        override fun toString(): String = name
    }

    internal class ProfileEditRepositorySaveProfileArgumentsProvider :
        TestArgumentsProvider<ProfileEditRepositorySaveProfileTestCase>() {
        override fun testCases() = listOf(
            ProfileEditRepositorySaveProfileTestCase(
                name = "no changes",
                shouldDeleteAvatar = false,
                expectedProfileUpdates = 0,
                expectedUserUpdates = 0,
                transform = { profile -> profile },
            ),
            ProfileEditRepositorySaveProfileTestCase(
                name = "user fields changed",
                shouldDeleteAvatar = false,
                expectedProfileUpdates = 0,
                expectedUserUpdates = 1,
                transform = { profile ->
                    profile.copy(
                        nickname = "john_updated",
                        location = "Tbilisi",
                    )
                },
            ),
            ProfileEditRepositorySaveProfileTestCase(
                name = "profile fields changed",
                shouldDeleteAvatar = false,
                expectedProfileUpdates = 1,
                expectedUserUpdates = 0,
                transform = { profile ->
                    profile.copy(
                        aboutMe = "Updated about me",
                        socialLinks = profile.socialLinks + mapOf(
                            DomainProfileEditSocialPlatform.LinkedIn to "https://linkedin.com/in/john",
                        ),
                    )
                },
            ),
            ProfileEditRepositorySaveProfileTestCase(
                name = "user and profile fields changed",
                shouldDeleteAvatar = false,
                expectedProfileUpdates = 1,
                expectedUserUpdates = 1,
                transform = { profile ->
                    profile.copy(
                        nickname = "john_updated",
                        aboutMe = "Updated about me",
                    )
                },
            ),
            ProfileEditRepositorySaveProfileTestCase(
                name = "avatar deleted",
                shouldDeleteAvatar = true,
                expectedProfileUpdates = 0,
                expectedUserUpdates = 1,
                transform = { profile -> profile },
            ),
            ProfileEditRepositorySaveProfileTestCase(
                name = "only list order changed",
                shouldDeleteAvatar = false,
                expectedProfileUpdates = 0,
                expectedUserUpdates = 0,
                transform = { profile ->
                    profile.copy(
                        socialLinks = linkedMapOf(
                            DomainProfileEditSocialPlatform.Telegram to "https://t.me/john",
                            DomainProfileEditSocialPlatform.GitHub to "https://github.com/john",
                        ),
                        selectedSkills = profile.selectedSkills.reversed(),
                    )
                },
            ),
        )
    }

    private fun cachedUser() = minimalUser(
        username = "john",
        email = "john@yeahub.ru",
        avatarUrl = "https://example.com/avatar.png",
        city = "Baku",
        country = "Azerbaijan",
        birthday = "1990-01-01",
        address = "Nizami, 1",
    ).copy(profiles = listOf(cachedProfile()))

    private fun cachedProfile() =
        minimalProfile(
            specializationId = 5,
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
            description = "<p>Old about<br>line</p>",
            profileSkills = cachedSkills(),
        ).copy(
            profileType = 2,
            markingWeight = 10,
            imageSrc = "https://example.com/profile.png",
            isActive = true,
        )

    private fun cachedSkills() = listOf(
        skillResponse(
            id = 1,
            title = "Kotlin",
            imageSrc = "kotlin.png",
        ),
        skillResponse(
            id = 2,
            title = "Compose",
            imageSrc = null,
        ),
    )

    private fun cachedSpecializations() = listOf(
        specializationResponse(
            id = 5,
            title = "Android",
        ),
        specializationResponse(
            id = 7,
            title = "iOS",
        ),
    )
}
