package ru.yeahub.feature_toggle_api

import org.koin.core.module.Module
import org.koin.core.qualifier.named

fun Module.registerFeatureToggle(featureToggle: FeatureToggle) {
    single<FeatureToggle>(qualifier = named(featureToggle.key)) {
        featureToggle
    }
}

fun Module.registerFeatureToggle(vararg featureToggles: FeatureToggle) {
    featureToggles.forEach { featureToggle ->
        registerFeatureToggle(featureToggle = featureToggle)
    }
}
