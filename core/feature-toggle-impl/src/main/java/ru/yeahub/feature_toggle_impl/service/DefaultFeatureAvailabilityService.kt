package ru.yeahub.feature_toggle_impl.service

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.yeahub.feature_toggle_api.FeatureAvailability
import ru.yeahub.feature_toggle_api.FeatureAvailabilityService
import ru.yeahub.feature_toggle_api.FeatureFlagsSnapshot
import ru.yeahub.feature_toggle_api.FeatureKey
import ru.yeahub.feature_toggle_api.emptyFeatureFlagsSnapshot
import ru.yeahub.feature_toggle_impl.data.FeatureFlagsRepository
import ru.yeahub.feature_toggle_impl.resolver.FeatureValueResolver
import timber.log.Timber
import java.io.IOException

internal class DefaultFeatureAvailabilityService(
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val featureValueResolver: FeatureValueResolver
) : FeatureAvailabilityService {

    private val featureToggleScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val mutableFeatureFlagsSnapshot = MutableStateFlow(emptyFeatureFlagsSnapshot())

    override val featureFlagsSnapshot: StateFlow<FeatureFlagsSnapshot> =
        mutableFeatureFlagsSnapshot.asStateFlow()

    init {
        launchRefreshFeatureFlags()
    }

    override fun getFeatureAvailability(featureKey: FeatureKey): FeatureAvailability {
        return featureValueResolver.resolve(
            featureKey = featureKey,
            featureFlagsSnapshot = mutableFeatureFlagsSnapshot.value
        )
    }

    private fun launchRefreshFeatureFlags() {
        featureToggleScope.launch {
            val featureFlagsSnapshot = refreshFeatureFlagsSnapshot()

            if (featureFlagsSnapshot != null) {
                mutableFeatureFlagsSnapshot.value = featureFlagsSnapshot
            }
        }
    }

    //Загрузка флагов на старте приложения
    @Suppress("TooGenericExceptionCaught")
    private suspend fun refreshFeatureFlagsSnapshot(): FeatureFlagsSnapshot? {
        var hasLoggedRefreshFailure = false

        while (true) {
            try {
                return featureFlagsRepository.getFeatureFlagsSnapshot()
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: IOException) {
                if (hasLoggedRefreshFailure.not()) {
                    Timber.tag(FEATURE_TOGGLE_LOG_TAG)
                        .e(exception, UNABLE_TO_REFRESH_FEATURE_FLAGS_LOG_MESSAGE)
                    hasLoggedRefreshFailure = true
                }

                delay(REFRESH_RETRY_DELAY_MILLIS)
            } catch (exception: Exception) {
                Timber.tag(FEATURE_TOGGLE_LOG_TAG)
                    .w(exception, UNABLE_TO_REFRESH_FEATURE_FLAGS_LOG_MESSAGE)
                return null
            }
        }
    }
}

private const val FEATURE_TOGGLE_LOG_TAG = "FeatureToggle"
private const val UNABLE_TO_REFRESH_FEATURE_FLAGS_LOG_MESSAGE =
    "Unable to refresh feature flags"
private const val REFRESH_RETRY_DELAY_MILLIS = 5_000L