package ru.yeahub.feature_toggle_impl.resolver

import ru.yeahub.feature_toggle_api.FeatureKey
import ru.yeahub.feature_toggle_api.FeatureKeys

internal class FeatureKeyBackendMapper {

    private val backendValuesByFeatureKey = mapOf(
        FeatureKeys.InterviewTrainer to INTERVIEW_TRAINER_BACKEND_VALUE
    )

    private val featureKeysByBackendValue = backendValuesByFeatureKey.entries.associate { entry ->
        entry.value to entry.key
    }

    init {
        check(backendValuesByFeatureKey.keys == FeatureKeys.all)
    }

    fun fromBackendValue(backendValue: String): FeatureKey? {
        return featureKeysByBackendValue[backendValue]
    }

    fun getBackendValue(featureKey: FeatureKey): String {
        return backendValuesByFeatureKey.getValue(featureKey)
    }
}

private const val INTERVIEW_TRAINER_BACKEND_VALUE = "enabled_interview_trainer"
