package ru.yeahub.detail_question.impl.presentation

import android.content.Intent
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.yeahub.detail_question.impl.presentation.intents.DetailQuestionResult
import ru.yeahub.detail_question.impl.presentation.view.DetailQuestionScreen
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_api.NavigationPathManager
import timber.log.Timber

class DetailQuestionFeatureImpl : FeatureApi {

    override fun getFeatureName(): String = FeatureRoute.DetailQuestionFeature.FEATURE_NAME

    override fun isRootFeature(): Boolean = false

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier
    ) {
        val detailQuestionRoute = pathManager.createParametrizedPath(
            featureName = getFeatureName(),
            "questionIds",
            "currentIndex"
        )

        navGraphBuilder.composable(
            route = detailQuestionRoute,
            arguments = listOf(
                navArgument("questionIds") { type = NavType.StringType },
                navArgument("currentIndex") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val questionIdsString = backStackEntry.arguments?.getString("questionIds") ?: ""
            val questionIds = questionIdsString.split(",").map { it.toLongOrNull() ?: 0L }
            val currentIndex = backStackEntry.arguments?.getInt("currentIndex") ?: 0
            val currentQuestionId = questionIds.getOrNull(currentIndex) ?: 0L

            // Извлекаем родительский путь из текущего маршрута, а не из pathManager
            val currentRoute = backStackEntry.destination.route ?: ""
            val parentPathFromRoute = extractParentPath(currentRoute)

            Timber.tag("DetailQuestionFeature")
                .d("Current route: $currentRoute, Parent path: $parentPathFromRoute")

            DetailQuestionScreen(
                onResult = { result ->
                    when (result) {
                        is DetailQuestionResult.BackClick -> handleBackNavigation(
                            pathManager,
                            navController
                        )

                        is DetailQuestionResult.UrlClick -> {
                            val intent = Intent(Intent.ACTION_VIEW, result.url.toUri())
                            navController.context.startActivity(intent)
                        }

                        is DetailQuestionResult.NavigateToQuestion -> handleQuestionNavigation(
                            navController,
                            questionIds,
                            result.newIndex,
                            parentPathFromRoute
                        )
                    }
                },
                questionId = currentQuestionId,
                questionIds = questionIds,
                currentIndex = currentIndex
            )
        }
    }
}

private fun handleBackNavigation(
    pathManager: NavigationPathManager,
    navController: NavHostController
) {
    val parentPath = pathManager.getParentPath()
    pathManager.setCurrentPath(parentPath)

    if (parentPath.isEmpty()) {
        navController.navigateUp()
    } else {
        navController.navigate(parentPath) {
            popUpTo(parentPath) {
                inclusive = true
            }
        }
    }
}

private fun handleQuestionNavigation(
    navController: NavHostController,
    questionIds: List<Long>,
    newIndex: Int,
    parentPath: String
) {
    val questionIdsParam = questionIds.joinToString(",")
    val concretePath = if (parentPath.isEmpty()) {
        "${FeatureRoute.DetailQuestionFeature.FEATURE_NAME}/$questionIdsParam/$newIndex"
    } else {
        "$parentPath/${FeatureRoute.DetailQuestionFeature.FEATURE_NAME}/$questionIdsParam/$newIndex"
    }

    Timber.tag("DetailQuestionFeature")
        .d("Navigating to: $concretePath (parent: $parentPath)")

    navController.navigate(concretePath)
}

/**
 * Извлекает родительский путь из текущего маршрута.
 * Например, из "questions/detail_question/{questionIds}/{currentIndex}"
 * извлечет "questions"
 */
private fun extractParentPath(route: String): String {
    if (route.isEmpty()) return ""

    // Удаляем параметры маршрута (все что после /)
    val featureName = FeatureRoute.DetailQuestionFeature.FEATURE_NAME
    val featureIndex = route.indexOf(featureName)

    return if (featureIndex > 0) {
        // Возвращаем все до имени фичи detail_question (без последнего /)
        route.substring(0, featureIndex - 1)
    } else {
        ""
    }
}