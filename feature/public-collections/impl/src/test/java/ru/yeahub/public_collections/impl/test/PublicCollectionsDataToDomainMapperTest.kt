package ru.yeahub.public_collections.impl.test

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.network_api.models.GetCollectionResponse
import ru.yeahub.network_api.models.GetCollectionsResponse
import ru.yeahub.network_api.models.GetCompanyResponse
import ru.yeahub.network_api.models.NestedSpecializationResponse
import ru.yeahub.network_api.models.NestedUserReferenceDto
import ru.yeahub.public_collections.impl.data.PublicCollectionDataToDomainMapper
import ru.yeahub.public_collections.impl.domain.GetCollectionResponseEntity
import ru.yeahub.public_collections.impl.domain.GetCollectionsResponseEntity
import java.util.stream.Stream

class PublicCollectionsDataToDomainMapperTest {

    private val mapper = PublicCollectionDataToDomainMapper()

    @ParameterizedTest
    @ArgumentsSource(PublicCollectionMapperArgumentsProvider::class)
    fun `should map GetCollectionsResponse to GetCollectionsResponseEntity`(testCase: PublicCollectionsMapperTestCase){
        val result = mapper.dataListToDomainList(testCase.dto)
        Assertions.assertEquals(testCase.expectedResult, result)
    }

    data class PublicCollectionsMapperTestCase(
        val dto: GetCollectionsResponse,
        val expectedResult: GetCollectionsResponseEntity
    )

    class PublicCollectionMapperArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments?>? {
            return Stream.of(
                Arguments.of(
                    PublicCollectionsMapperTestCase(
                        dto = GetCollectionsResponse(
                            page = 1, limit = 10, data = listOf(
                                GetCollectionResponse(
                                    id = 1,
                                    title = "Title 1",
                                    description = "Description 1",
                                    imageSrc = "123",
                                    isFree = true,
                                    keywords = listOf("keywords1"),
                                    questionsCount = 1,
                                    specializations = listOf(
                                        NestedSpecializationResponse(
                                            id = 1,
                                            title = "sp1",
                                            description = "des1",
                                            imageSrc = "img1",
                                            createdAt = "2024-01-01",
                                            updatedAt = "2024-01-01",
                                        )
                                    ),
                                    company = GetCompanyResponse(
                                        id = "1",
                                        title = "comp1",
                                        legalName = "leg1",
                                        description = "des1",
                                        imageSrc = "123",
                                        inn = "111",
                                        kpp = "123",
                                        createdAt = "2024-01-01",
                                        updatedAt = "2024-01-01"
                                    ),
                                    createdBy = NestedUserReferenceDto(
                                        id = "user_1", username = "User 1"
                                    ),
                                    createdAt = "2024-01-01",
                                    updatedAt = "2024-01-01"
                                )
                            ), total = 1
                        ), expectedResult = GetCollectionsResponseEntity(
                            page = 1, limit = 10, data = listOf(
                                GetCollectionResponseEntity(
                                    id = 1,
                                    title = "Title 1",
                                    description = "Description 1",
                                    imageSrc = "123",
                                    questionsCount = 1
                                )
                            ), total = 1
                        )
                    )
                )
            )
        }
    }
}