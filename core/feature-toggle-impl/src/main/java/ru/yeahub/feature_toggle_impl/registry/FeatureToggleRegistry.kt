package ru.yeahub.feature_toggle_impl.registry

import ru.yeahub.feature_toggle_api.FeatureToggle

internal class FeatureToggleRegistry(
    featureToggles: List<FeatureToggle>
) {

    private val featureTogglesByKey = featureToggles.associateBy(FeatureToggle::key)

    init {
        check(featureTogglesByKey.size == featureToggles.size)
    }

    fun getFeatureToggle(key: String): FeatureToggle? {
        return featureTogglesByKey[key]
    }

    fun getFeatureToggles(): Collection<FeatureToggle> {
        return featureTogglesByKey.values
    }
}
