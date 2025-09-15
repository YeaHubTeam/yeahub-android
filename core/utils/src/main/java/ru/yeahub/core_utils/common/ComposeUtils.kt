package ru.yeahub.core_utils.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
inline fun <reified T> Flow<T>.observe(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    key: Any = Unit,
    noinline action: suspend (T) -> Unit,
) {
    LaunchedEffect(key1 = key) {
        onEach(action)
            .flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState)
            .launchIn(this)
    }
}