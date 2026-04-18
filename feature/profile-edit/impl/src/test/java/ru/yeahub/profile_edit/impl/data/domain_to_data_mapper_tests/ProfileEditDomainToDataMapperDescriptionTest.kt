package ru.yeahub.profile_edit.impl.data.domain_to_data_mapper_tests

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.profile_edit.impl.data.ProfileEditDomainToDataMapper
import ru.yeahub.profile_edit.impl.data.minimalDomainData
import ru.yeahub.profile_edit.impl.data.minimalProfile
import ru.yeahub.profile_edit.impl.data.minimalUser
import ru.yeahub.test.TestArgumentsProvider

class ProfileEditDomainToDataMapperDescriptionTest {

    private val mapper = ProfileEditDomainToDataMapper()

    @ParameterizedTest
    @ArgumentsSource(ProfileEditDomainToDataMapperDescriptionArgumentsProvider::class)
    fun `should map plain text description to minimal html`(
        testCase: ProfileEditDomainToDataMapperDescriptionTestCase,
    ) {
        val description = mapper.mapAboutMeToHtml(testCase.description)
        val result = mapper.mapToUpdateProfileRequest(
            profile = minimalDomainData(aboutMe = testCase.description),
            description = description,
            cachedProfile = minimalProfile(),
            cachedUser = minimalUser(),
            cachedAllSkills = emptyList(),
            allSpecializations = emptyList(),
        )

        assertEquals(testCase.expectedDescription, result.description)
    }

    data class ProfileEditDomainToDataMapperDescriptionTestCase(
        val description: String,
        val expectedDescription: String,
    )

    class ProfileEditDomainToDataMapperDescriptionArgumentsProvider :
        TestArgumentsProvider<ProfileEditDomainToDataMapperDescriptionTestCase>() {
        override fun testCases() = listOf(
            ProfileEditDomainToDataMapperDescriptionTestCase(
                description = "Updated first line\nUpdated second line",
                expectedDescription = "<p>Updated first line<br>Updated second line</p>",
            ),
            ProfileEditDomainToDataMapperDescriptionTestCase(
                description = "First paragraph\n\nSecond paragraph",
                expectedDescription = "<p>First paragraph</p><p>Second paragraph</p>",
            ),
            ProfileEditDomainToDataMapperDescriptionTestCase(
                description = "text\nnewline\n\nnewline after double enter\nnew line",
                expectedDescription = "<p>text<br>newline</p><p>newline after double enter<br>new line</p>",
            ),
        )
    }
}
