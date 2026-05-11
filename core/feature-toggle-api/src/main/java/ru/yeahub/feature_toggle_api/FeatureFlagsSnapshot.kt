package ru.yeahub.feature_toggle_api

fun emptyFeatureFlagsSnapshot(): FeatureFlagsSnapshot {
    return FeatureFlagsSnapshot(featureValueByKey = emptyMap())
}

data class FeatureFlagsSnapshot(
    private val featureValueByKey: Map<String, Boolean>
) {
    fun getFeatureValue(key: String): Boolean? {
        return featureValueByKey[key]
    }
}
