package ru.yeahub.feature_toggle_api

import kotlinx.coroutines.flow.StateFlow

interface FeatureAvailabilityService {
    val featureFlagsSnapshot: StateFlow<FeatureFlagsSnapshot>

    fun getFeatureAvailability(featureKey: FeatureKey): FeatureAvailability
}
