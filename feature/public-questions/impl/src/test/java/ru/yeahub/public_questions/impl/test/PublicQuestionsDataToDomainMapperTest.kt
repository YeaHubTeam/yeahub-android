package ru.yeahub.public_questions.impl.test

import java.util.stream.Stream
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.junit.jupiter.params.ParameterizedTest
import ru.yeahub.public_questions.impl.data.mapper.PublicQuestionsDataToDomainMapper
import ru.yeahub.public_questions.impl.domain.entity.PublicQuestionModel
import ru.yeahub.public_questions.impl.domain.entity.PublicQuestionsModel
import ru.yeahub.network_api.models.GetPublicQuestionsResponse
import ru.yeahub.network_api.models.GetQuestionResponse
import ru.yeahub.network_api.models.NestedSkillResponse
import ru.yeahub.network_api.models.NestedSpecializationResponse
import ru.yeahub.network_api.models.NestedUserReferenceDto

class PublicQuestionsDataToDomainMapperTest {
    private val mapper = PublicQuestionsDataToDomainMapper()

    @ParameterizedTest
    @ArgumentsSource(PublicQuestionMapperArgumentsProvider::class)
    fun `should map GetPublicQuestionsResponse to QuestionsModel`(testCase: PublicQuestionMapperTestCase) {
        val result = mapper.mapGetPublicQuestionsResponseToDomain(testCase.dto)
        Assertions.assertEquals(testCase.expectedResult, result)
    }

    data class PublicQuestionMapperTestCase(
        val dto: GetPublicQuestionsResponse,
        val expectedResult: PublicQuestionsModel
    )

    class PublicQuestionMapperArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(
                    PublicQuestionMapperTestCase(
                        dto = GetPublicQuestionsResponse(
                            page = null,
                            limit = 10,
                            data = listOf(
                                GetQuestionResponse(
                                    id = 1,
                                    title = "Title 1",
                                    description = "Description 1",
                                    shortAnswer = "Short Answer 1",
                                    status = "active",
                                    rate = 5,
                                    complexity = 2,
                                    imageSrc = "image1.jpg",
                                    createdById = "user_1",
                                    updatedById = "user_2",
                                    createdAt = "2024-01-01",
                                    updatedAt = "2024-01-02",
                                    questionSkills = listOf(
                                        NestedSkillResponse(
                                            1,
                                            "Skill 1",
                                            "Skill Description 1",
                                            "skill1.jpg",
                                            "2023-01-01",
                                            "2023-02-01"
                                        )
                                    ),
                                    questionSpecializations = listOf(
                                        NestedSpecializationResponse(
                                            1,
                                            "Specialization 1",
                                            "Specialization Description 1",
                                            "specialization1.jpg",
                                            "2023-01-01",
                                            "2023-02-01"
                                        )
                                    ),
                                    createdBy = NestedUserReferenceDto(
                                        id = "user_1",
                                        username = "User 1"
                                    ),
                                    updatedBy = NestedUserReferenceDto(
                                        id = "user_2",
                                        username = "User 2"
                                    ),
                                    keywords = listOf(
                                        "keyword1",
                                        "keyword2"
                                    ),
                                    code = "code1",
                                    longAnswer = "longAnswer1"
                                )
                            ),
                            total = 1
                        ),
                        expectedResult = PublicQuestionsModel(
                            page = 0,
                            limit = 10,
                            data = listOf(
                                PublicQuestionModel(
                                    id = 1,
                                    title = "Title 1",
                                )
                            ),
                            total = 1
                        )
                    )
                ),
                Arguments.of(
                    PublicQuestionMapperTestCase(
                        dto = GetPublicQuestionsResponse(
                            page = null,
                            limit = 10,
                            data = listOf(
                                GetQuestionResponse(
                                    id = 1,
                                    title = "Title 1",
                                    description = "Description 1",
                                    shortAnswer = "Short Answer 1",
                                    status = "active",
                                    rate = 5,
                                    complexity = 2,
                                    imageSrc = "image1.jpg",
                                    createdById = "user_1",
                                    updatedById = "user_2",
                                    createdAt = "2024-01-01",
                                    updatedAt = "2024-01-02",
                                    questionSkills = listOf(
                                        NestedSkillResponse(
                                            1,
                                            "Skill 1",
                                            "Skill Description 1",
                                            "skill1.jpg",
                                            "2023-01-01",
                                            "2023-02-01"
                                        )
                                    ),
                                    questionSpecializations = listOf(
                                        NestedSpecializationResponse(
                                            1,
                                            "Specialization 1",
                                            "Specialization Description 1",
                                            "specialization1.jpg",
                                            "2023-01-01",
                                            "2023-02-01"
                                        )
                                    ),
                                    createdBy = NestedUserReferenceDto(
                                        id = "user_1",
                                        username = "User 1"
                                    ),
                                    updatedBy = NestedUserReferenceDto(
                                        id = "user_2",
                                        username = "User 2"
                                    ),
                                    keywords = listOf("keyword1", "keyword2"),
                                    code = "code1",
                                    longAnswer = "longAnswer1"
                                )
                            ),
                            total = 1
                        ),
                        expectedResult = PublicQuestionsModel(
                            page = 0,
                            limit = 10,
                            data = listOf(
                                PublicQuestionModel(
                                    id = 1,
                                    title = "Title 1",
                                )
                            ),
                            total = 1
                        )
                    )
                ),
                Arguments.of(
                    PublicQuestionMapperTestCase(
                        dto = GetPublicQuestionsResponse(
                            page = 2,
                            limit = 10,
                            data = listOf(
                                GetQuestionResponse(
                                    id = 2,
                                    title = "Title 2",
                                    description = "Description 2",
                                    shortAnswer = "Short Answer 2",
                                    status = "inactive",
                                    rate = 3,
                                    complexity = 5,
                                    imageSrc = "image2.jpg",
                                    createdById = "user_3",
                                    updatedById = "user_4",
                                    createdAt = "2024-02-01",
                                    updatedAt = "2024-02-02",
                                    questionSkills = listOf(
                                        NestedSkillResponse(
                                            2,
                                            "Skill 2",
                                            "Skill Description 2",
                                            "skill2.jpg",
                                            "2023-02-01",
                                            "2023-03-01"
                                        )
                                    ),
                                    questionSpecializations = listOf(
                                        NestedSpecializationResponse(
                                            2,
                                            "Specialization 2",
                                            "Specialization Description 2",
                                            "specialization2.jpg",
                                            "2023-02-01",
                                            "2023-03-01"
                                        )
                                    ),
                                    createdBy = NestedUserReferenceDto(
                                        id = "user_3",
                                        username = "User 3"
                                    ),
                                    updatedBy = NestedUserReferenceDto(
                                        id = "user_4",
                                        username = "User 4"
                                    ),
                                    keywords = listOf("keyword3", "keyword4"),
                                    code = "code2",
                                    longAnswer = "longAnswer2"
                                )
                            ),
                            total = 1
                        ),
                        expectedResult = PublicQuestionsModel(
                            page = 2,
                            limit = 10,
                            data = listOf(
                                PublicQuestionModel(
                                    id = 2,
                                    title = "Title 2",
                                )
                            ),
                            total = 1
                        )
                    )
                ),
                Arguments.of(
                    PublicQuestionMapperTestCase(
                        dto = GetPublicQuestionsResponse(
                            page = 1,
                            limit = 5,
                            data = listOf(
                                GetQuestionResponse(
                                    id = 3,
                                    title = "Title 3",
                                    description = "Description 3",
                                    shortAnswer = "Short Answer 3",
                                    status = "active",
                                    rate = null,
                                    complexity = null,
                                    imageSrc = "image3.jpg",
                                    createdById = "user_5",
                                    updatedById = "user_6",
                                    createdAt = "2024-03-01",
                                    updatedAt = "2024-03-02",
                                    questionSkills = listOf(
                                        NestedSkillResponse(
                                            3,
                                            "Skill 3",
                                            "Skill Description 3",
                                            "skill3.jpg",
                                            "2023-03-01",
                                            "2023-04-01"
                                        )
                                    ),
                                    questionSpecializations = listOf(
                                        NestedSpecializationResponse(
                                            3,
                                            "Specialization 3",
                                            "Specialization Description 3",
                                            "specialization3.jpg",
                                            "2023-03-01",
                                            "2023-04-01"
                                        )
                                    ),
                                    createdBy = NestedUserReferenceDto(
                                        id = "user_5",
                                        username = "User 5"
                                    ),
                                    updatedBy = NestedUserReferenceDto(
                                        id = "user_6",
                                        username = "User 6"
                                    ),
                                    keywords = listOf("keyword5", "keyword6"),
                                    code = "code3",
                                    longAnswer = "longAnswer3"
                                )
                            ),
                            total = 1
                        ),
                        expectedResult = PublicQuestionsModel(
                            page = 1,
                            limit = 5,
                            data = listOf(
                                PublicQuestionModel(
                                    id = 3,
                                    title = "Title 3",
                                )
                            ),
                            total = 1
                        )
                    )
                ),
                Arguments.of(
                    PublicQuestionMapperTestCase(
                        dto = GetPublicQuestionsResponse(
                            page = 1,
                            limit = 5,
                            data = listOf(
                                GetQuestionResponse(
                                    id = 4,
                                    title = "Title 4",
                                    description = "Description 4",
                                    shortAnswer = "Short Answer 4",
                                    status = "inactive",
                                    rate = 8,
                                    complexity = 4,
                                    imageSrc = "image4.jpg",
                                    createdById = "user_7",
                                    updatedById = "user_8",
                                    createdAt = "2024-04-01",
                                    updatedAt = "2024-04-02",
                                    questionSkills = listOf(
                                        NestedSkillResponse(
                                            4,
                                            "Skill 4",
                                            "Skill Description 4",
                                            "skill4.jpg",
                                            "2023-04-01",
                                            "2023-05-01"
                                        )
                                    ),
                                    questionSpecializations = listOf(
                                        NestedSpecializationResponse(
                                            4,
                                            "Specialization 4",
                                            "Specialization Description 4",
                                            "specialization4.jpg",
                                            "2023-04-01",
                                            "2023-05-01"
                                        )
                                    ),
                                    createdBy = NestedUserReferenceDto(
                                        id = "user_7",
                                        username = "User 7"
                                    ),
                                    updatedBy = NestedUserReferenceDto(
                                        id = "user_8",
                                        username = "User 8"
                                    ),
                                    keywords = listOf("keyword7", "keyword8"),
                                    code = "code4",
                                    longAnswer = "longAnswer4"
                                ),
                                GetQuestionResponse(
                                    id = 5,
                                    title = "Title 5",
                                    description = "Description 5",
                                    shortAnswer = "Short Answer 5",
                                    status = "active",
                                    rate = 9,
                                    complexity = 6,
                                    imageSrc = "image5.jpg",
                                    createdById = "user_9",
                                    updatedById = "user_10",
                                    createdAt = "2024-05-01",
                                    updatedAt = "2024-05-02",
                                    questionSkills = listOf(
                                        NestedSkillResponse(
                                            5,
                                            "Skill 5",
                                            "Skill Description 5",
                                            "skill5.jpg",
                                            "2023-05-01",
                                            "2023-06-01"
                                        )
                                    ),
                                    questionSpecializations = listOf(
                                        NestedSpecializationResponse(
                                            5,
                                            "Specialization 5",
                                            "Specialization Description 5",
                                            "specialization5.jpg",
                                            "2023-05-01",
                                            "2023-06-01"
                                        )
                                    ),
                                    createdBy = NestedUserReferenceDto(
                                        id = "user_9",
                                        username = "User 9"
                                    ),
                                    updatedBy = NestedUserReferenceDto(
                                        id = "user_10",
                                        username = "User 10"
                                    ),
                                    keywords = listOf("keyword9", "keyword10"),
                                    code = "code5",
                                    longAnswer = "longAnswer5"
                                )
                            ),
                            total = 2
                        ),
                        expectedResult = PublicQuestionsModel(
                            page = 1,
                            limit = 5,
                            data = listOf(
                                PublicQuestionModel(
                                    id = 4,
                                    title = "Title 4",
                                ),
                                PublicQuestionModel(
                                    id = 5,
                                    title = "Title 5",
                                )
                            ),
                            total = 2
                        )
                    )
                )
            )
        }
    }
}
