package ru.yeahub.feature_toggle_api

object FeatureKeys {
    val InterviewTrainer = FeatureKey(value = "interview_trainer")

    val all: Set<FeatureKey> = setOf(
        InterviewTrainer
    )
}
