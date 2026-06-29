package ru.yeahub.navigation_impl

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class NavigationGatingTest {

    @ParameterizedTest
    @ArgumentsSource(IsRouteEnabledArgumentsProvider::class)
    fun `isRouteEnabled returns expected for route and disabled features`(
        testCase: IsRouteEnabledTestCase
    ) {
        val result = isRouteEnabled(testCase.route, testCase.disabledFeatureNames)
        Assertions.assertEquals(testCase.expectedResult, result)
    }

    @ParameterizedTest
    @ArgumentsSource(FirstAvailableRouteArgumentsProvider::class)
    fun `firstAvailableRoute returns first not disabled candidate`(
        testCase: FirstAvailableRouteTestCase
    ) {
        val result = firstAvailableRoute(testCase.candidates, testCase.disabledFeatureNames)
        Assertions.assertEquals(testCase.expectedResult, result)
    }

    @ParameterizedTest
    @ArgumentsSource(ResolveDeepLinkRouteArgumentsProvider::class)
    fun `resolveDeepLinkRoute returns target route or fallback`(
        testCase: ResolveDeepLinkRouteTestCase
    ) {
        val result = resolveDeepLinkRoute(
            directPath = testCase.directPath,
            disabledFeatureNames = testCase.disabledFeatureNames,
            fallbackCandidates = testCase.fallbackCandidates
        )
        Assertions.assertEquals(testCase.expectedResult, result)
    }

    data class IsRouteEnabledTestCase(
        val route: String?,
        val disabledFeatureNames: Set<String>,
        val expectedResult: Boolean
    )

    data class FirstAvailableRouteTestCase(
        val candidates: List<String>,
        val disabledFeatureNames: Set<String>,
        val expectedResult: String?
    )

    data class ResolveDeepLinkRouteTestCase(
        val directPath: String,
        val disabledFeatureNames: Set<String>,
        val fallbackCandidates: List<String>,
        val expectedResult: String?
    )

    class IsRouteEnabledArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(
                    IsRouteEnabledTestCase(
                        route = null,
                        disabledFeatureNames = emptySet(),
                        expectedResult = true
                    )
                ),
                Arguments.of(
                    IsRouteEnabledTestCase(
                        route = "",
                        disabledFeatureNames = emptySet(),
                        expectedResult = true
                    )
                ),
                Arguments.of(
                    IsRouteEnabledTestCase(
                        route = "home",
                        disabledFeatureNames = emptySet(),
                        expectedResult = true
                    )
                ),
                Arguments.of(
                    IsRouteEnabledTestCase(
                        route = "collections",
                        disabledFeatureNames = setOf("collections"),
                        expectedResult = false
                    )
                ),
                Arguments.of(
                    IsRouteEnabledTestCase(
                        route = "home/details/{itemId}/{title}",
                        disabledFeatureNames = setOf("details"),
                        expectedResult = false
                    )
                ),
                Arguments.of(
                    IsRouteEnabledTestCase(
                        route = "home/details/{itemId}/{title}",
                        disabledFeatureNames = emptySet(),
                        expectedResult = true
                    )
                ),
                Arguments.of(
                    IsRouteEnabledTestCase(
                        route = "questions?category=tech",
                        disabledFeatureNames = setOf("questions"),
                        expectedResult = false
                    )
                ),
                Arguments.of(
                    IsRouteEnabledTestCase(
                        route = "questions?category=tech",
                        disabledFeatureNames = emptySet(),
                        expectedResult = true
                    )
                )
            )
        }
    }

    class FirstAvailableRouteArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(
                    FirstAvailableRouteTestCase(
                        candidates = listOf("collections", "home", "questions"),
                        disabledFeatureNames = setOf("collections"),
                        expectedResult = "home"
                    )
                ),
                Arguments.of(
                    FirstAvailableRouteTestCase(
                        candidates = listOf("collections", "questions", "home"),
                        disabledFeatureNames = setOf("collections"),
                        expectedResult = "questions"
                    )
                ),
                Arguments.of(
                    FirstAvailableRouteTestCase(
                        candidates = listOf("collections", "questions"),
                        disabledFeatureNames = setOf("collections", "questions"),
                        expectedResult = null
                    )
                )
            )
        }
    }

    class ResolveDeepLinkRouteArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(
                    ResolveDeepLinkRouteTestCase(
                        directPath = "home/details/1/Title",
                        disabledFeatureNames = emptySet(),
                        fallbackCandidates = listOf("collections", "home", "questions"),
                        expectedResult = "home/details/1/Title"
                    )
                ),
                Arguments.of(
                    ResolveDeepLinkRouteTestCase(
                        directPath = "collections/public_collections/1/Title",
                        disabledFeatureNames = setOf("collections"),
                        fallbackCandidates = listOf("collections", "home", "questions"),
                        expectedResult = "home"
                    )
                ),
                Arguments.of(
                    ResolveDeepLinkRouteTestCase(
                        directPath = "collections/public_collections/1/Title",
                        disabledFeatureNames = setOf("collections", "home", "questions"),
                        fallbackCandidates = listOf("collections", "home", "questions"),
                        expectedResult = null
                    )
                )
            )
        }
    }
}
