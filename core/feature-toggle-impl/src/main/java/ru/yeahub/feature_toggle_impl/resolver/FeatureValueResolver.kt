package ru.yeahub.feature_toggle_impl.resolver

import ru.yeahub.feature_toggle_api.FeatureAvailability
import ru.yeahub.feature_toggle_api.FeatureFlagsSnapshot
import ru.yeahub.feature_toggle_api.FeatureKey
import ru.yeahub.feature_toggle_api.FeatureKeys

internal class FeatureValueResolver {

    private val bootstrapFallbackAvailabilityByKey =
        createBootstrapFallbackAvailabilityByKey()

    fun resolve(
        featureKey: FeatureKey,
        featureFlagsSnapshot: FeatureFlagsSnapshot
    ): FeatureAvailability {
        return featureFlagsSnapshot.getFeatureAvailability(featureKey = featureKey)
            ?: bootstrapFallbackAvailabilityByKey.getValue(featureKey)
    }

    private fun createBootstrapFallbackAvailabilityByKey(): Map<FeatureKey, FeatureAvailability> {
        val featureAvailabilityByKey = mapOf(
            FeatureKeys.InterviewTrainer to FeatureAvailability.fromBoolean(
                isAvailable = false
            )
        )

        check(featureAvailabilityByKey.keys == FeatureKeys.all)

        return featureAvailabilityByKey
    }
}
