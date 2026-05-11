package ru.yeahub.feature_toggle_impl.data

import ru.yeahub.feature_toggle_api.FeatureFlagsSnapshot

internal interface FeatureFlagsRepository {
    suspend fun getFeatureFlagsSnapshot(): FeatureFlagsSnapshot
}
