package ru.yeahub.selection_specializations.impl.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun HandleSpecialCommand(
    commandFlow: SharedFlow<SpecializationSelectionScreenCommand>,
    parentRoute: String,
    onNavigate: (parentRoute: String, specId: String) -> Unit,
    onBackClick: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val commandState by commandFlow.collectAsStateWithLifecycle(
            initialValue = null,
            lifecycleOwner = lifecycleOwner,
        )

    commandState
        ?.let { command ->
            when (command) {
                is SpecializationSelectionScreenCommand.SpecializationSelectionClick -> {
                    onNavigate("$parentRoute as parent route", "${command.onClickedSpecId} as specId")
                }

                SpecializationSelectionScreenCommand.OnBackClick -> {
                    onBackClick
                }
            }
        }
}
