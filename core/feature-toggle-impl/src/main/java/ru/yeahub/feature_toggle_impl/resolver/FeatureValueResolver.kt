package ru.yeahub.feature_toggle_impl.resolver

import ru.yeahub.feature_toggle_api.FeatureFlagsSnapshot
import ru.yeahub.feature_toggle_api.FeatureToggle

internal class FeatureValueResolver {

    // Выбираем значение фичи (бэкенд или fallback)
    fun resolve(
        featureToggle: FeatureToggle,
        featureFlagsSnapshot: FeatureFlagsSnapshot
    ): Boolean {
        return featureFlagsSnapshot.getFeatureValue(key = featureToggle.key)
            ?: featureToggle.defaultValue
    }
}
