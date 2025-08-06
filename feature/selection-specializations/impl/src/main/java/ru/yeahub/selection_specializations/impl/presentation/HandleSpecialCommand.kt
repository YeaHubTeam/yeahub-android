package ru.yeahub.selection_specializations.impl.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.yeahub.selection_specializations.api.domain.GetOnbordingUseCase
import ru.yeahub.selection_specializations.api.domain.GetSpecializationUseCase

@Composable
fun HandleSpecialCommand(
    commandFlow: Flow<OnSpecialScreenCommand>,
    onNavigate: (nextRoute: String) -> Unit,
    onBoardUseCase: GetOnbordingUseCase,
    specialUseCase: GetSpecializationUseCase,
    onBackClick: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(commandFlow, lifecycleOwner) {
        commandFlow
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { command ->
                when (command) {
                    is OnSpecialScreenCommand.ByOnBoardOnClick -> {
                        onNavigate(onBoardUseCase.getNextRoute(command.onClickedSpecId))
                    }

                    is OnSpecialScreenCommand.BySpecialOnClick -> {
                        onNavigate(specialUseCase.getNextRoute(command.onClickedSpecId))
                    }

                    OnSpecialScreenCommand.OnBackClick -> {
                        onBackClick
                    }
                }
            }
    }
}