package ru.yeahub.authentication.impl.navigation

import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import org.koin.androidx.compose.koinViewModel
import ru.yeahub.authentication.impl.login.presentation.ui.LoginScreen
import ru.yeahub.authentication.impl.login.presentation.viewmodel.LoginViewModel
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_api.NavigationPathManager

//граф авторизации
class AuthenticationFeatureImpl : FeatureApi {

    override fun getFeatureName(): String = FeatureRoute.AuthenticationFeature.AUTH_ROUTE

    override fun isRootFeature(): Boolean = true

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier,
    ) {
        registerLoginRoute(
            navGraphBuilder = navGraphBuilder,
            navController = navController,
            modifier = modifier,
        )
    }

    private fun registerLoginRoute(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier,
    ) {
        navGraphBuilder.composable(
            route = FeatureRoute.AuthenticationFeature.LOGIN_ROUTE,
        ) {
            val viewModel: LoginViewModel = koinViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            LoginScreen(
                state = state,
                commands = viewModel.commands,
                onAction = viewModel::onAction,
                onNavigateToMain = {
                    navController.navigate(FeatureRoute.HomeFeature.FEATURE_NAME) {
                        popUpTo(FeatureRoute.AuthenticationFeature.LOGIN_ROUTE) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(FeatureRoute.RegistrationFeature.FEATURE_NAME) {
                        launchSingleTop = true
                    }
                },
                onNavigateToForgotPassword = {
                    // TODO: добавить переход после подключения экрана восстановления пароля.
                },
                modifier = modifier,
            )
        }
    }
}
