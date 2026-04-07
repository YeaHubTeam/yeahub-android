package ru.yeahub.feature_toggle_api

@JvmInline
value class FeatureAvailability(
    val isAvailable: Boolean
) {
    companion object {
        fun fromBoolean(isAvailable: Boolean): FeatureAvailability {
            return FeatureAvailability(isAvailable = isAvailable)
        }
    }
}
