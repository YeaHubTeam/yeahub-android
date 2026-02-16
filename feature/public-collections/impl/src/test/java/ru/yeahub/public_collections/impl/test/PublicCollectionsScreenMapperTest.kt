package ru.yeahub.public_collections.impl.test

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.core_utils.pagerImpl.YeaHubPagerState
import ru.yeahub.public_collections.impl.domain.entity.GetCollectionResponseEntity
import ru.yeahub.public_collections.impl.presentation.PublicCollectionsScreenState
import ru.yeahub.public_collections.impl.presentation.mapper.PublicCollectionsScreenMapper
import java.util.stream.Stream

class PublicCollectionsScreenMapperTest {

    private val mapper = PublicCollectionsScreenMapper()

    @ParameterizedTest
    @ArgumentsSource(PublicCollectionsScreenMapperArgumentsProvider::class)
    fun `should map GetCollectionResponseEntity to PublicCollectionsScreenState`(
        testCase: PublicCollectionsScreenMapperTestCase
    ) {
        val result = mapper.getScreenState(
            testCase.dataToTest,
            header = testCase.header
        )
        Assertions.assertEquals(testCase.expectedResult, result)
    }

    data class PublicCollectionsScreenMapperTestCase(
        val dataToTest: YeaHubPagerState<GetCollectionResponseEntity>,
        val header: String,
        val expectedResult: PublicCollectionsScreenState
    )

    class PublicCollectionsScreenMapperArgumentsProvider : ArgumentsProvider {
        private val testError = RuntimeException("Test error")

        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(
                    PublicCollectionsScreenMapperTestCase(
                        dataToTest = YeaHubPagerState.Initial,
                        expectedResult = PublicCollectionsScreenState.Initial(
                            header = TextOrResource.Text("Public Collections")
                        ),
                        header = "Public Collections"
                    )
                ),
                Arguments.of(
                    PublicCollectionsScreenMapperTestCase(
                        dataToTest = YeaHubPagerState.Loading(
                            items = emptyList()
                        ),
                        expectedResult = PublicCollectionsScreenState.Loading(
                            header = TextOrResource.Text("Public Collections")
                        ),
                        header = "Public Collections"
                    )
                ),
                Arguments.of(
                    PublicCollectionsScreenMapperTestCase(
                        dataToTest = YeaHubPagerState.Loading(
                            items = listOf(
                                GetCollectionResponseEntity(
                                    id = 1,
                                    title = "Test Collection",
                                    description = "Test Description",
                                    imageSrc = "test.jpg",
                                    questionsCount = 5
                                )
                            )
                        ),
                        expectedResult = PublicCollectionsScreenState.Loaded(
                            header = TextOrResource.Text("Public Collections"),
                            collectionPublicCollectionVOList = listOf(
                                PublicCollectionsScreenState.Loaded.PublicCollectionVO(
                                    id = 1,
                                    collectionTitle = "Test Collection",
                                    descriptionText = "Test Description",
                                    imageUrl = "test.jpg",
                                    questionsCount = 5
                                )
                            ),
                            isEndReached = false,
                            isLoadingNextPage = true
                        ),
                        header = "Public Collections"
                    )
                ),
                Arguments.of(
                    PublicCollectionsScreenMapperTestCase(
                        dataToTest = YeaHubPagerState.Loaded(
                            items = listOf(
                                GetCollectionResponseEntity(
                                    id = 1,
                                    title = "Collection 1",
                                    description = "Description 1",
                                    imageSrc = "image1.jpg",
                                    questionsCount = 10
                                ),
                                GetCollectionResponseEntity(
                                    id = 2,
                                    title = "Collection 2",
                                    description = "Description 2",
                                    imageSrc = "image2.jpg",
                                    questionsCount = 20
                                )
                            ),
                            isEndReached = true
                        ),
                        expectedResult = PublicCollectionsScreenState.Loaded(
                            header = TextOrResource.Text("Public Collections"),
                            collectionPublicCollectionVOList = listOf(
                                PublicCollectionsScreenState.Loaded.PublicCollectionVO(
                                    id = 1,
                                    collectionTitle = "Collection 1",
                                    descriptionText = "Description 1",
                                    imageUrl = "image1.jpg",
                                    questionsCount = 10
                                ),
                                PublicCollectionsScreenState.Loaded.PublicCollectionVO(
                                    id = 2,
                                    collectionTitle = "Collection 2",
                                    descriptionText = "Description 2",
                                    imageUrl = "image2.jpg",
                                    questionsCount = 20
                                )
                            ),
                            isEndReached = true,
                            isLoadingNextPage = false
                        ),
                        header = "Public Collections"
                    )
                ),
                Arguments.of(
                    PublicCollectionsScreenMapperTestCase(
                        dataToTest = YeaHubPagerState.Error(
                            items = listOf(
                                GetCollectionResponseEntity(
                                    id = 1,
                                    title = "Existing Collection",
                                    description = "Existing Description",
                                    imageSrc = "existing.jpg",
                                    questionsCount = 3
                                )
                            ),
                            throwable = testError
                        ),
                        expectedResult = PublicCollectionsScreenState.Error(
                            header = TextOrResource.Text(""),
                            currentList = listOf(
                                PublicCollectionsScreenState.Loaded.PublicCollectionVO(
                                    id = 1,
                                    collectionTitle = "Existing Collection",
                                    descriptionText = "Existing Description",
                                    imageUrl = "existing.jpg",
                                    questionsCount = 3
                                )
                            ),
                            throwable = testError
                        ),
                        header = ""
                    )
                )
            )
        }
    }
}