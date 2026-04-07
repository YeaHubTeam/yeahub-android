package ru.yeahub.feature_toggle_api

fun emptyFeatureFlagsSnapshot(): FeatureFlagsSnapshot {
    return FeatureFlagsSnapshot(featureAvailabilityByKey = emptyMap())
}

data class FeatureFlagsSnapshot(
    private val featureAvailabilityByKey: Map<FeatureKey, FeatureAvailability>
) {
    fun getFeatureAvailability(featureKey: FeatureKey): FeatureAvailability? {
        return featureAvailabilityByKey.get(featureKey)
    }
}
