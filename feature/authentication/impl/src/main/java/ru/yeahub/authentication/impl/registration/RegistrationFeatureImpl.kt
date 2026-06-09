package ru.yeahub.authentication.impl.registration

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.yeahub.authentication.impl.R
import ru.yeahub.authentication.impl.registration.presentation.RegistrationScreen
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_api.NavigationPathManager

class RegistrationFeatureImpl : FeatureApi {

    override fun getFeatureName(): String = FeatureRoute.RegistrationFeature.FEATURE_NAME

    override fun isRootFeature(): Boolean = true

    override fun initialize(pathManager: NavigationPathManager) {
        super.initialize(pathManager)
        pathManager.registerFeaturePath(getFeatureName(), getFeatureName())
    }

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier
    ) {
        navGraphBuilder.composable(route = getFeatureName()) {
            val uriHandler = LocalUriHandler.current
            val pdUrl = stringResource(R.string.pd_offer_url)
            val offerUrl = stringResource(R.string.public_offer_uri)

            RegistrationScreen(
                onRegistrationSuccess = {
                    navController.navigate(FeatureRoute.HomeFeature.FEATURE_NAME) {
                        popUpTo(getFeatureName()) { inclusive = true }
                    }
                },
                onOpenPdPolicy = { uriHandler.openUri(pdUrl) },
                onOpenOffer = { uriHandler.openUri(offerUrl) }
            )
        }
    }
}
