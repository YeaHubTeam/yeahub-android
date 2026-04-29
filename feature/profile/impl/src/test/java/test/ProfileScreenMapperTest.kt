package test

import kotlinx.collections.immutable.persistentListOf
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import retrofit2.Response
import ru.yeahub.profile.impl.domain.DomainUserProfile
import ru.yeahub.profile.impl.presentation.ProfileScreenMapper
import ru.yeahub.profile.impl.presentation.ProfileScreenState
import java.io.IOException

class ProfileScreenMapperTest {

    private val mapper = ProfileScreenMapper()

    @Test
    fun `map should return Loading state when result is null`() {
        val state = mapper.map(result = null)

        assertTrue(state is ProfileScreenState.Loading)
    }

    @Test
    fun `map should return Success state when result is success`() {
        val domainProfile = createTestDomainProfile()
        val result = Result.success(domainProfile)

        val state = mapper.map(result)

        assertTrue(state is ProfileScreenState.Success)
        val successState = state as ProfileScreenState.Success
        assertEquals(domainProfile.id, successState.userData.id)
        assertEquals(domainProfile.username, successState.userData.username)
        assertEquals(domainProfile.avatarUrl, successState.userData.avatarUrl)
        assertEquals(domainProfile.city, successState.userData.city)
        assertEquals(domainProfile.country, successState.userData.country)
        assertEquals(domainProfile.telegramUsername, successState.userData.telegramUsername)
        assertEquals(domainProfile.aboutMe, successState.userData.aboutMe)
        assertEquals(domainProfile.roles, successState.userData.roles)
        assertEquals(domainProfile.skills, successState.userData.skills)
        assertEquals(domainProfile.specialization, successState.userData.specialization)
        assertEquals(domainProfile.socialNetworks.size, successState.userData.socialNetworks.size)
        assertEquals(
            domainProfile.socialNetworks.first().code,
            successState.userData.socialNetworks.first().code
        )
        assertEquals(
            domainProfile.socialNetworks.first().url,
            successState.userData.socialNetworks.first().title
        )
    }

    @Test
    fun `map should return Error state when result contains IOException`() {
        val exception = IOException("Network error")
        val result = Result.failure<DomainUserProfile>(exception)

        val state = mapper.map(result)

        assertTrue(state is ProfileScreenState.Error)
        val errorState = state as ProfileScreenState.Error
        assertEquals("network_error", errorState.message)
    }

    @Test
    fun `map should return Unauthorized state when result contains HttpException with code 401`() {
        val exception = createHttpException(401)
        val result = Result.failure<DomainUserProfile>(exception)

        val state = mapper.map(result)

        assertTrue(state is ProfileScreenState.Unauthorized)
    }

    @Test
    fun `map should return Error state when result contains HttpException with code 403`() {
        val exception = createHttpException(403)
        val result = Result.failure<DomainUserProfile>(exception)

        val state = mapper.map(result)

        assertTrue(state is ProfileScreenState.Error)
        val errorState = state as ProfileScreenState.Error
        assertEquals("access_denied", errorState.message)
    }

    @Test
    fun `map should return UserDeleted state when result contains HttpException with code 404`() {
        val exception = createHttpException(404)
        val result = Result.failure<DomainUserProfile>(exception)

        val state = mapper.map(result)

        assertTrue(state is ProfileScreenState.UserDeleted)
    }

    @Test
    fun `map should return Error with server error message when result contains HttpException with code 500`() {
        val exception = createHttpException(500)
        val result = Result.failure<DomainUserProfile>(exception)

        val state = mapper.map(result)

        assertTrue(state is ProfileScreenState.Error)
        val errorState = state as ProfileScreenState.Error
        assertEquals("server_error:500", errorState.message)
    }

    @Test
    fun `map should return Error state when result contains unknown exception`() {
        val exception = RuntimeException("Unexpected error")
        val result = Result.failure<DomainUserProfile>(exception)

        val state = mapper.map(result)

        assertTrue(state is ProfileScreenState.Error)
        val errorState = state as ProfileScreenState.Error
        assertEquals("unknown_error", errorState.message)
    }

    @Test
    fun `map should correctly map social networks with null url to empty string`() {
        val domainProfile = createTestDomainProfile().copy(
            socialNetworks = persistentListOf(
                ru.yeahub.profile.impl.domain.SocialNetwork("instagram", null),
                ru.yeahub.profile.impl.domain.SocialNetwork("linkedin", null)
            )
        )
        val result = Result.success(domainProfile)

        val state = mapper.map(result)

        assertTrue(state is ProfileScreenState.Success)
        val successState = state as ProfileScreenState.Success
        assertEquals("", successState.userData.socialNetworks[0].title)
        assertEquals("", successState.userData.socialNetworks[1].title)
    }

    @Test
    fun `map should correctly map empty social networks list`() {
        val domainProfile = createTestDomainProfile().copy(socialNetworks = persistentListOf())
        val result = Result.success(domainProfile)

        val state = mapper.map(result)

        assertTrue(state is ProfileScreenState.Success)
        val successState = state as ProfileScreenState.Success
        assertTrue(successState.userData.socialNetworks.isEmpty())
    }

    private fun createTestDomainProfile(): DomainUserProfile {
        return DomainUserProfile(
            id = "123",
            username = "john_doe",
            avatarUrl = "https://example.com/avatar.jpg",
            city = "Москва",
            country = "Россия",
            telegramUsername = "john_telegram",
            aboutMe = "About John Doe",
            roles = persistentListOf("Кандидат", "Ментор"),
            skills = persistentListOf("Kotlin", "Compose", "Coroutines"),
            specialization = "Android Developer",
            socialNetworks = persistentListOf(
                ru.yeahub.profile.impl.domain.SocialNetwork(
                    "instagram",
                    "https://instagram.com/john_doe"
                ),
                ru.yeahub.profile.impl.domain.SocialNetwork(
                    "linkedin",
                    "https://linkedin.com/in/john-doe"
                ),
                ru.yeahub.profile.impl.domain.SocialNetwork("github", "https://github.com/johndoe")
            )
        )
    }

    private fun createHttpException(code: Int): HttpException {
        val response = Response.error<Any>(
            code,
            "".toResponseBody(null)
        )
        return HttpException(response)
    }
}