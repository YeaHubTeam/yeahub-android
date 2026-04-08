package ru.yeahub.feature_toggle_impl.resolver

import ru.yeahub.feature_toggle_api.FeatureAvailability
import ru.yeahub.feature_toggle_api.FeatureFlagsSnapshot
import ru.yeahub.feature_toggle_api.FeatureKey
import ru.yeahub.feature_toggle_api.FeatureKeys

internal class FeatureValueResolver {

    private val bootstrapFallbackOverrides = createBootstrapFallbackOverrides()

    //Выбираем значение фичи (бэкенд или fallback)
    fun resolve(
        featureKey: FeatureKey,
        featureFlagsSnapshot: FeatureFlagsSnapshot
    ): FeatureAvailability {
        return featureFlagsSnapshot.getFeatureAvailability(featureKey = featureKey)
            ?: bootstrapFallbackOverrides[featureKey]
            ?: FeatureAvailability.fromBoolean(isAvailable = false)
    }

    private fun createBootstrapFallbackOverrides(): Map<FeatureKey, FeatureAvailability> {
        /**
         * Можно добавить кастомные значения флагов
         * в случае, если не получаем их с бэкенда
         *
         * Пример:
         * FeatureKeys.InterviewTrainer to FeatureAvailability.fromBoolean(
         *     isAvailable = true
         * )
         */
        val featureAvailabilityByKey: Map<FeatureKey, FeatureAvailability> = emptyMap()

        check(FeatureKeys.all.containsAll(featureAvailabilityByKey.keys))

        return featureAvailabilityByKey
    }
}
