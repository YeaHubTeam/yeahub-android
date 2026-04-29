package ru.yeahub.profile.impl

import android.content.Intent
import android.net.Uri
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_api.NavigationPathManager
import ru.yeahub.profile.impl.presentation.ProfileResult
import ru.yeahub.profile.impl.presentation.ProfileScreen
import timber.log.Timber

class ProfileFeatureImpl : FeatureApi {
    override fun getFeatureName(): String = FeatureRoute.ProfileFeature.PROFILE_SCREEN_NAME

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier
    ) {
        android.util.Log.d("ProfileFeature", "✅ registerGraph CALLED!")

        pathManager.registerFeaturePath(featureName = getFeatureName(), basePath = getFeatureName())

        val profileRoute = "${FeatureRoute.ProfileFeature.PROFILE_SCREEN_NAME}"

        Timber.d("ProfileFeatureImpl registerGraph: currentPath: $profileRoute")

        navGraphBuilder.composable(
            route = profileRoute
        ) {
            val context = LocalContext.current

            val onResult: (ProfileResult) -> Unit = { result ->
                when (result) {
                    is ProfileResult.OpenSocialNetwork -> {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(result.url))
                        context.startActivity(intent)
                    }
                    is ProfileResult.NavigateBack -> {
                        navController.popBackStack()
                    }
                }
            }

            ProfileScreen(onResult = onResult)
        }
    }
}