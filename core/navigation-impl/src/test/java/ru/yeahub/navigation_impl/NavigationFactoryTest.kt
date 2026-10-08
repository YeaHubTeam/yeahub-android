package ru.yeahub.navigation_impl

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_impl.model.BottomNavigationItem
import ru.yeahub.test.TestArgumentsProvider

class NavigationFactoryTest {

    @ParameterizedTest
    @ArgumentsSource(StartDestinationArgumentsProvider::class)
    fun `should return correct start destination`(
        testCase: StartDestinationTestCase,
    ) {
        val result = getStartDestination(testCase.isAuthorized)

        assertEquals(testCase.expectedRoute, result)
    }

    @ParameterizedTest
    @ArgumentsSource(SelectedRouteArgumentsProvider::class)
    fun `should return correct selected bottom navigation route`(
        testCase: SelectedRouteTestCase,
    ) {
        val result = getSelectedRoute(
            currentRoute = testCase.currentRoute,
            navItems = getBottomNavItems(),
        )

        assertEquals(testCase.expectedRoute, result)
    }

    data class StartDestinationTestCase(
        val isAuthorized: Boolean,
        val expectedRoute: String,
    )

    class StartDestinationArgumentsProvider : TestArgumentsProvider<StartDestinationTestCase>() {
        override fun testCases(): List<StartDestinationTestCase> = listOf(
            StartDestinationTestCase(
                isAuthorized = true,
                expectedRoute = FeatureRoute.HomeFeature.FEATURE_NAME,
            ),
            StartDestinationTestCase(
                isAuthorized = false,
                expectedRoute = FeatureRoute.AuthenticationFeature.LOGIN_ROUTE,
            ),
        )
    }

    data class SelectedRouteTestCase(
        val currentRoute: String?,
        val expectedRoute: String,
    )

    class SelectedRouteArgumentsProvider : TestArgumentsProvider<SelectedRouteTestCase>() {
        override fun testCases(): List<SelectedRouteTestCase> = listOf(
            SelectedRouteTestCase(
                currentRoute = null,
                expectedRoute = BottomNavigationItem.Home.route,
            ),
            SelectedRouteTestCase(
                currentRoute = FeatureRoute.AuthenticationFeature.LOGIN_ROUTE,
                expectedRoute = BottomNavigationItem.Profile.route,
            ),
            SelectedRouteTestCase(
                currentRoute = FeatureRoute.RegistrationFeature.FEATURE_NAME,
                expectedRoute = BottomNavigationItem.Profile.route,
            ),
            SelectedRouteTestCase(
                currentRoute = BottomNavigationItem.Home.route,
                expectedRoute = BottomNavigationItem.Home.route,
            ),
            SelectedRouteTestCase(
                currentRoute = BottomNavigationItem.Collections.route,
                expectedRoute = BottomNavigationItem.Collections.route,
            ),
            SelectedRouteTestCase(
                currentRoute = "${BottomNavigationItem.Collections.route}/specializations",
                expectedRoute = BottomNavigationItem.Collections.route,
            ),
            SelectedRouteTestCase(
                currentRoute = BottomNavigationItem.Questions.route,
                expectedRoute = BottomNavigationItem.Questions.route,
            ),
        )
    }
}
