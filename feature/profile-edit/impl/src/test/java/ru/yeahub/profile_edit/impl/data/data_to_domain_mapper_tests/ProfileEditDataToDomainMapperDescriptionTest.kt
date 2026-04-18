package ru.yeahub.profile_edit.impl.data.data_to_domain_mapper_tests

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.profile_edit.impl.data.ProfileEditDataToDomainMapper
import ru.yeahub.profile_edit.impl.data.minimalProfile
import ru.yeahub.profile_edit.impl.data.minimalUser
import ru.yeahub.test.TestArgumentsProvider

class ProfileEditDataToDomainMapperDescriptionTest {

    @ParameterizedTest
    @ArgumentsSource(ProfileEditDataToDomainMapperDescriptionArgumentsProvider::class)
    fun `should map html description to editable plain text`(
        testCase: ProfileEditDataToDomainMapperDescriptionTestCase,
    ) {
        val result = ProfileEditDataToDomainMapper().mapProfileToDomain(
            user = minimalUser(),
            activeProfile = minimalProfile(description = testCase.description),
            allSkills = emptyList(),
            specializations = emptyList(),
        )

        assertEquals(testCase.expectedDescription, result.aboutMe)
    }

    data class ProfileEditDataToDomainMapperDescriptionTestCase(
        val description: String,
        val expectedDescription: String,
    )

    class ProfileEditDataToDomainMapperDescriptionArgumentsProvider :
        TestArgumentsProvider<ProfileEditDataToDomainMapperDescriptionTestCase>() {
        override fun testCases() = listOf(
            ProfileEditDataToDomainMapperDescriptionTestCase(
                description = "<p>text<br>newline</p><p>newline after double enter<br>new line</p>",
                expectedDescription = "text\nnewline\n\nnewline after double enter\nnew line",
            ),
            ProfileEditDataToDomainMapperDescriptionTestCase(
                description = "<p>Hello <strong>world</strong></p><p>Second line</p>",
                expectedDescription = "Hello world\n\nSecond line",
            ),
            ProfileEditDataToDomainMapperDescriptionTestCase(
                description = "<div>First<br>Second</div>",
                expectedDescription = "First\nSecond",
            ),
            ProfileEditDataToDomainMapperDescriptionTestCase(
                description = "<p><strong>Bold</strong></p>" +
                        "<blockquote><p>Quote</p></blockquote><ul>" +
                        "<li><p>One</p></li><li><p>Two</p></li></ul>",
                expectedDescription = "Bold\n\nQuote\n\nOne\n\nTwo",
            ),
            ProfileEditDataToDomainMapperDescriptionTestCase(
                description = "<p></p>",
                expectedDescription = "",
            ),
        )
    }
}
