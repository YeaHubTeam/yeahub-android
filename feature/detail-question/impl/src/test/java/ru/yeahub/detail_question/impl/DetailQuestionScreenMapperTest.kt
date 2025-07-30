package ru.yeahub.detail_question.impl


import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.detail_question.impl.domain.models.GuruEntity
import ru.yeahub.detail_question.impl.domain.models.NestedSkillEntity
import ru.yeahub.detail_question.impl.domain.models.NestedSpecializationEntity
import ru.yeahub.detail_question.impl.domain.models.NestedUserReferenceEntity
import ru.yeahub.detail_question.impl.domain.models.PublicQuestionEntity
import ru.yeahub.detail_question.impl.presentation.mapper.DetailQuestionScreenMapper
import ru.yeahub.detail_question.impl.presentation.state.DetailQuestionState
import ru.yeahub.detail_question.impl.presentation.state.GuruVO
import ru.yeahub.detail_question.impl.presentation.state.NestedSkillVO
import ru.yeahub.detail_question.impl.presentation.state.NestedSpecializationVO
import ru.yeahub.detail_question.impl.presentation.state.NestedUserReferenceVO

class DetailQuestionScreenMapperTest {

    @ParameterizedTest
    @ArgumentsSource(DetailQuestionScreenMapperArgumentsProvider::class)
    fun `map PublicQuestionEntity to DetailQuestionState correctly`(tesCase: DetailQuestionScreenMapperTestCase) {
        val mapper = DetailQuestionScreenMapper()

        val result = mapper.getScreenState(tesCase.dataToTest)

        assert(result == tesCase.expectedResult)
    }

    data class DetailQuestionScreenMapperTestCase(
        val dataToTest: PublicQuestionEntity,
        val expectedResult: DetailQuestionState
    )

    class DetailQuestionScreenMapperArgumentsProvider :
        TestDetailQuestionArgumentsProvider<DetailQuestionScreenMapperTestCase>() {
        override fun testCases(): List<DetailQuestionScreenMapperTestCase> {
            return listOf(
                DetailQuestionScreenMapperTestCase(
                    dataToTest = PublicQuestionEntity(
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
                            photoUrl = "https://e5e684b1-4a6a-4be5-b7ee-b2b678239d61.selstorage.ru/gurus" +
                                    "/%D0%90%D0%BD%D1%82%D0%BE%D0%BD%D0%BC%D0%B5%D0%BD%D1%82%D0%BE%D1%80.png",
                            youtubeUrl = "https://youtube.com/@gulyaev_it",
                            telegramUrl = "https://t.me/gulyaev_it"
                        )
                    ),
                    expectedResult = DetailQuestionState.Success(
                        DetailQuestionState.Success.PublicQuestionVO(
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
                            NestedSpecializationVO(
                                id = 27,
                                title = "Android",
                                description = "Android development",
                                imageSrc = "android.jpg",
                                createdAt = "2023-01-01",
                                updatedAt = "2023-01-02"
                            )
                        ),
                        questionSkills = listOf(
                            NestedSkillVO(
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
                        createdBy = NestedUserReferenceVO(
                            id = "user1",
                            username = "john_doe"
                        ),
                        updatedBy = NestedUserReferenceVO(
                            id = "user1",
                            username = "john_doe"
                        ),
                        guru = GuruVO(
                            name = "Anton Gulyaev",
                            title = "Android Guru",
                            specializationId = 27,
                            photoUrl = "https://e5e684b1-4a6a-4be5-b7ee-b2b678239d61.selstorage.ru/gurus" +
                                    "/%D0%90%D0%BD%D1%82%D0%BE%D0%BD%D0%BC%D0%B5%D0%BD%D1%82%D0%BE%D1%80.png",
                            youtubeUrl = "https://youtube.com/@gulyaev_it",
                            telegramUrl = "https://t.me/gulyaev_it"
                        )
                    )
                )
                ),
                DetailQuestionScreenMapperTestCase(
                    dataToTest = PublicQuestionEntity(
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
                            photoUrl = "https://e5e684b1-4a6a-4be5-b7ee-b2b678239d61.selstorage.ru/gurus" +
                                    "/%D0%90%D0%BD%D1%82%D0%BE%D0%BD%D0%BC%D0%B5%D0%BD%D1%82%D0%BE%D1%80.png",
                            youtubeUrl = "https://youtube.com/@gulyaev_it",
                            telegramUrl = "https://t.me/gulyaev_it"
                        )
                    ),
                    expectedResult = DetailQuestionState.Success(
                        DetailQuestionState.Success.PublicQuestionVO(
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
                            NestedSpecializationVO(
                                id = 27,
                                title = "Android",
                                description = "Android development",
                                imageSrc = "android.jpg",
                                createdAt = "2023-01-01",
                                updatedAt = "2023-01-02"
                            )
                        ),
                        questionSkills = listOf(
                            NestedSkillVO(
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
                        createdBy = NestedUserReferenceVO(
                            id = "user1",
                            username = "john_doe"
                        ),
                        updatedBy = null,
                        guru = GuruVO(
                            name = "Anton Gulyaev",
                            title = "Android Guru",
                            specializationId = 27,
                            photoUrl = "https://e5e684b1-4a6a-4be5-b7ee-b2b678239d61.selstorage.ru/gurus" +
                                    "/%D0%90%D0%BD%D1%82%D0%BE%D0%BD%D0%BC%D0%B5%D0%BD%D1%82%D0%BE%D1%80.png",
                            youtubeUrl = "https://youtube.com/@gulyaev_it",
                            telegramUrl = "https://t.me/gulyaev_it"
                        )
                    )
                )
                )
            )
        }
    }
}