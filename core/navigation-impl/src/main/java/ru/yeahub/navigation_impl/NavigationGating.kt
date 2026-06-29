package ru.yeahub.navigation_impl

import ru.yeahub.feature_toggle_api.FeatureAvailabilityService
import ru.yeahub.navigation_api.FeatureApi

fun collectDisabledFeatureNames(
    features: Collection<FeatureApi>,
    featureAvailabilityService: FeatureAvailabilityService
): Set<String> = features.mapNotNull { feature ->
    feature.featureToggle()
        ?.takeUnless(featureAvailabilityService::isFeatureEnabled)
        ?.let { feature.getFeatureName() }
}.toSet()

fun isRouteEnabled(route: String?, disabledFeatureNames: Set<String>): Boolean {
    if (route.isNullOrEmpty()) return true
    return route.substringBefore('?')
        .split('/')
        .none { segment -> segment in disabledFeatureNames }
}

fun firstAvailableRoute(candidates: List<String>, disabledFeatureNames: Set<String>): String? =
    candidates.firstOrNull { route -> isRouteEnabled(route, disabledFeatureNames) }

fun resolveDeepLinkRoute(
    directPath: String,
    disabledFeatureNames: Set<String>,
    fallbackCandidates: List<String>
): String? = if (isRouteEnabled(directPath, disabledFeatureNames)) {
    directPath
} else {
    firstAvailableRoute(fallbackCandidates, disabledFeatureNames)
}
