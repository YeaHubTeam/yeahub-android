package ru.yeahub.feature_toggle_api

abstract class FeatureToggle(
    val key: String,
    val defaultValue: Boolean,
    val description: String
)
