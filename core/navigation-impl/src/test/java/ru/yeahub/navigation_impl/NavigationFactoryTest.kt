package ru.yeahub.navigation_impl

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.navigation_impl.model.BottomNavigationItem
import java.util.stream.Stream

class NavigationFactoryTest {

    @ParameterizedTest
    @ArgumentsSource(GetSelectedRouteArgumentsProvider::class)
    fun `getSelectedRoute resolves selected tab for current route`(
        testCase: GetSelectedRouteTestCase
    ) {
        val result = getSelectedRoute(testCase.currentRoute, testCase.navItems)
        Assertions.assertEquals(testCase.expectedResult, result)
    }

    data class GetSelectedRouteTestCase(
        val currentRoute: String?,
        val navItems: List<BottomNavigationItem>,
        val expectedResult: String?
    )

    class GetSelectedRouteArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            val navItems = getBottomNavItems()
            return Stream.of(
                Arguments.of(
                    GetSelectedRouteTestCase(
                        currentRoute = "home",
                        navItems = emptyList(),
                        expectedResult = null
                    )
                ),
                Arguments.of(
                    GetSelectedRouteTestCase(
                        currentRoute = null,
                        navItems = navItems,
                        expectedResult = navItems.first().route
                    )
                ),
                Arguments.of(
                    GetSelectedRouteTestCase(
                        currentRoute = "home",
                        navItems = navItems,
                        expectedResult = "home"
                    )
                ),
                Arguments.of(
                    GetSelectedRouteTestCase(
                        currentRoute = "home/details/1/Title",
                        navItems = navItems,
                        expectedResult = "home"
                    )
                )
            )
        }
    }
}
