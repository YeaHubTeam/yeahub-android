package com.example.impl

import com.example.impl.data.mapper.DataToDomainMapper
import com.example.impl.domain.models.GuruEntity
import com.example.impl.domain.models.NestedSkillEntity
import com.example.impl.domain.models.NestedSpecializationEntity
import com.example.impl.domain.models.NestedUserReferenceEntity
import com.example.impl.domain.models.PublicQuestionEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.network_api.models.GetPublicQuestionResponse
import ru.yeahub.network_api.models.NestedSkillResponse
import ru.yeahub.network_api.models.NestedSpecializationResponse
import ru.yeahub.network_api.models.NestedUserReferenceDto

class DTOToDomainMapperTest {

    @ParameterizedTest
    @ArgumentsSource(DTOToDomainMapperDetailQuestionArgumentsProvider::class)
    fun `map GetPublicQuestionResponse to PublicQuestionEntity correctly`(testCase: DetailQuestionMapperTestCase) {
        val mapper = DataToDomainMapper()
        val result = mapper.getPublicQuestionEntity(testCase.dataToTest)

        assertEquals(testCase.expectedResult, result)
    }

    data class DetailQuestionMapperTestCase(
        val dataToTest: GetPublicQuestionResponse,
        val expectedResult: PublicQuestionEntity
    )

    class DTOToDomainMapperDetailQuestionArgumentsProvider :
        TestDetailQuestionArgumentsProvider<DetailQuestionMapperTestCase>() {
        override fun testCases(): List<DetailQuestionMapperTestCase> {
            return listOf(
                DetailQuestionMapperTestCase(
                    dataToTest = GetPublicQuestionResponse(
                        id = 1,
                        title = "Question 1",
                        description = "Description 1",
                        code = "println(\"Hello\")",
                        imageSrc = "question1.jpg",
                        longAnswer = "Long answer",
                        shortAnswer = "Short answer",
                        keywords = listOf("kotlin", "android"),
                        status = "public",
                        rate = 4,
                        complexity = 5,
                        createdById = "user1",
                        updatedById = "user2",
                        questionSpecializations = listOf(
                            NestedSpecializationResponse(
                                id = 27,
                                title = "Android",
                                description = "Android development",
                                imageSrc = "android.jpg",
                                createdAt = "2023-01-01",
                                updatedAt = "2023-01-02"
                            )
                        ),
                        questionSkills = listOf(
                            NestedSkillResponse(
                                id = 27,
                                title = "Kotlin",
                                description = "Kotlin programming",
                                imageSrc = "kotlin.jpg",
                                createdAt = "2023-01-01",
                                updatedAt = "2023-01-02"
                            )
                        ),
                        createdAt = "2023-01-01",
                        updatedAt = "2023-01-02",
                        createdBy = NestedUserReferenceDto(
                            id = "user1",
                            username = "john_doe"
                        ),
                        updatedBy = NestedUserReferenceDto(
                            id = "user1",
                            username = "john_doe"
                        )
                    ),
                    expectedResult = PublicQuestionEntity(
                        id = 1,
                        title = "Question 1",
                        description = "Description 1",
                        code = "println(\"Hello\")",
                        imageSrc = "question1.jpg",
                        longAnswer = "Long answer",
                        shortAnswer = "Short answer",
                        keywords = listOf("kotlin", "android"),
                        status = "public",
                        rate = 4,
                        complexity = 5,
                        createdById = "user1",
                        updatedById = "user2",
                        questionSpecializations = listOf(
                            NestedSpecializationEntity(
                                id = 27,
                                title = "Android",
                                description = "Android development",
                                imageSrc = "android.jpg",
                                createdAt = "2023-01-01",
                                updatedAt = "2023-01-02"
                            )
                        ),
                        questionSkills = listOf(
                            NestedSkillEntity(
                                id = 27,
                                title = "Kotlin",
                                description = "Kotlin programming",
                                imageSrc = "kotlin.jpg",
                                createdAt = "2023-01-01",
                                updatedAt = "2023-01-02"
                            )
                        ),
                        createdAt = "2023-01-01",
                        updatedAt = "2023-01-02",
                        createdBy = NestedUserReferenceEntity(
                            id = "user1",
                            username = "john_doe"
                        ),
                        updatedBy = NestedUserReferenceEntity(
                            id = "user1",
                            username = "john_doe"
                        ),
                        guru = GuruEntity(
                            name = "Anton Gulyaev",
                            title = "Android Guru",
                            specializationId = 27,
                            photoUrl = "https://e5e684b1-4a6a-4be5-b7ee-b2b678239d61.selstorage.ru/gurus/%D0%90%D0%BD%D1%82%D0%BE%D0%BD%D0%BC%D0%B5%D0%BD%D1%82%D0%BE%D1%80.png",
                            youtubeUrl = "https://youtube.com/@gulyaev_it",
                            telegramUrl = "https://t.me/gulyaev_it"
                        )
                    )
                ),
                DetailQuestionMapperTestCase(
                    dataToTest = GetPublicQuestionResponse(
                        id = 1,
                        title = "Question 1",
                        description = "Description 1",
                        code = null,
                        imageSrc = null,
                        longAnswer = null,
                        shortAnswer = null,
                        keywords = null,
                        status = null,
                        rate = null,
                        complexity = null,
                        createdById = "user1",
                        updatedById = null,
                        questionSpecializations = listOf(
                            NestedSpecializationResponse(
                                id = 27,
                                title = "Android",
                                description = "Android development",
                                imageSrc = "android.jpg",
                                createdAt = "2023-01-01",
                                updatedAt = "2023-01-02"
                            )
                        ),
                        questionSkills = listOf(
                            NestedSkillResponse(
                                id = 27,
                                title = "Kotlin",
                                description = "Kotlin programming",
                                imageSrc = "kotlin.jpg",
                                createdAt = "2023-01-01",
                                updatedAt = "2023-01-02"
                            )
                        ),
                        createdAt = "2023-01-01",
                        updatedAt = "2023-01-02",
                        createdBy = NestedUserReferenceDto(
                            id = "user1",
                            username = "john_doe"
                        ),
                        updatedBy = null
                    ),
                    expectedResult = PublicQuestionEntity(
                        id = 1,
                        title = "Question 1",
                        description = "Description 1",
                        code = null,
                        imageSrc = null,
                        longAnswer = null,
                        shortAnswer = null,
                        keywords = null,
                        status = null,
                        rate = null,
                        complexity = null,
                        createdById = "user1",
                        updatedById = null,
                        questionSpecializations = listOf(
                            NestedSpecializationEntity(
                                id = 27,
                                title = "Android",
                                description = "Android development",
                                imageSrc = "android.jpg",
                                createdAt = "2023-01-01",
                                updatedAt = "2023-01-02"
                            )
                        ),
                        questionSkills = listOf(
                            NestedSkillEntity(
                                id = 27,
                                title = "Kotlin",
                                description = "Kotlin programming",
                                imageSrc = "kotlin.jpg",
                                createdAt = "2023-01-01",
                                updatedAt = "2023-01-02"
                            )
                        ),
                        createdAt = "2023-01-01",
                        updatedAt = "2023-01-02",
                        createdBy = NestedUserReferenceEntity(
                            id = "user1",
                            username = "john_doe"
                        ),
                        updatedBy = null,
                        guru = GuruEntity(
                            name = "Anton Gulyaev",
                            title = "Android Guru",
                            specializationId = 27,
                            photoUrl = "https://e5e684b1-4a6a-4be5-b7ee-b2b678239d61.selstorage.ru/gurus/%D0%90%D0%BD%D1%82%D0%BE%D0%BD%D0%BC%D0%B5%D0%BD%D1%82%D0%BE%D1%80.png",
                            youtubeUrl = "https://youtube.com/@gulyaev_it",
                            telegramUrl = "https://t.me/gulyaev_it"
                        )
                    )
                )
            )
        }
    }
}