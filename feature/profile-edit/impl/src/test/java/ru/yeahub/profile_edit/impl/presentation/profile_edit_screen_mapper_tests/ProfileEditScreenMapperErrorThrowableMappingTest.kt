package ru.yeahub.profile_edit.impl.presentation.profile_edit_screen_mapper_tests

import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import retrofit2.HttpException
import retrofit2.Response
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.profile_edit.impl.presentation.ProfileEditMapperInput
import ru.yeahub.profile_edit.impl.presentation.ProfileEditScreenMapper
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState
import ru.yeahub.test.TestArgumentsProvider
import ru.yeahub.ui.R
import java.io.IOException
import ru.yeahub.profile_edit.impl.R as ProfileEditR

private fun createHttpException(code: Int): HttpException {
    val response = Response.error<Any>(code, "".toResponseBody(null))
    return HttpException(response)
}

/**
 * Проверяет маппинг всех типов Throwable на TextOrResource в mapThrowableToMessage():
 * IOException, все именованные HTTP-коды (401/403/404/413/400/422),
 * диапазон 500..599 (граничные значения 500/599 и середина 503),
 * else-ветка HttpException (402, 418) и else-ветка общая (RuntimeException, IllegalStateException).
 */
class ProfileEditScreenMapperErrorThrowableMappingTest {

    private val mapper = ProfileEditScreenMapper()

    @ParameterizedTest
    @ArgumentsSource(ProfileEditScreenMapperErrorThrowableMappingArgumentsProvider::class)
    fun `should map throwable to correct error message`(
        testCase: ProfileEditScreenMapperErrorThrowableMappingTestCase,
    ) {
        val input = ProfileEditMapperInput.Error(testCase.throwable)
        val result = mapper.getScreenState(input)
        val error = result as ProfileEditState.Error
        assertEquals(testCase.expectedMessage, error.message)
    }

    data class ProfileEditScreenMapperErrorThrowableMappingTestCase(
        val throwable: Throwable,
        val expectedMessage: TextOrResource,
    )

    class ProfileEditScreenMapperErrorThrowableMappingArgumentsProvider :
        TestArgumentsProvider<ProfileEditScreenMapperErrorThrowableMappingTestCase>() {
        override fun testCases() = listOf(
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = IOException("no network"),
                expectedMessage = TextOrResource.Resource(ProfileEditR.string.error_no_internet),
            ),
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = createHttpException(401),
                expectedMessage = TextOrResource.Resource(
                    ProfileEditR.string.error_session_expired,
                ),
            ),
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = createHttpException(403),
                expectedMessage = TextOrResource.Resource(ProfileEditR.string.error_forbidden),
            ),
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = createHttpException(404),
                expectedMessage = TextOrResource.Resource(
                    ProfileEditR.string.error_profile_not_found,
                ),
            ),
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = createHttpException(413),
                expectedMessage = TextOrResource.Resource(
                    ProfileEditR.string.error_file_too_large,
                ),
            ),
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = createHttpException(400),
                expectedMessage = TextOrResource.Resource(ProfileEditR.string.error_invalid_data),
            ),
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = createHttpException(422),
                expectedMessage = TextOrResource.Resource(ProfileEditR.string.error_invalid_data),
            ),
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = createHttpException(500),
                expectedMessage = TextOrResource.Resource(ProfileEditR.string.error_server),
            ),
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = createHttpException(503),
                expectedMessage = TextOrResource.Resource(ProfileEditR.string.error_server),
            ),
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = createHttpException(599),
                expectedMessage = TextOrResource.Resource(ProfileEditR.string.error_server),
            ),
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = createHttpException(402),
                expectedMessage = TextOrResource.Resource(R.string.error_screen_text),
            ),
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = createHttpException(418),
                expectedMessage = TextOrResource.Resource(R.string.error_screen_text),
            ),
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = RuntimeException("unknown"),
                expectedMessage = TextOrResource.Resource(R.string.error_screen_text),
            ),
            ProfileEditScreenMapperErrorThrowableMappingTestCase(
                throwable = IllegalStateException("bug"),
                expectedMessage = TextOrResource.Resource(R.string.error_screen_text),
            ),
        )
    }
}
