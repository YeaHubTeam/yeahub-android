package ru.yeahub.selection_specializations.impl.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.yeahub.selection_specializations.impl.domain.GetOnbordingUseCaseImpl
import ru.yeahub.selection_specializations.impl.domain.GetSpecializationUseCaseImpl

@Composable
fun HandleSpecialCommand(
    commandFlow: Flow<OnSpecialScreenCommand>,
    currentUseCase: OnSpecialFeatureUseCase,
    onNavigate: (nextRoute: String) -> Unit,
    onBackClick: () -> Unit
) {
    val onBoardUseCase = GetOnbordingUseCaseImpl()
    val specialUseCase = GetSpecializationUseCaseImpl()

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(commandFlow, lifecycleOwner) {
        commandFlow
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { command ->
                when (command) {
                    is OnSpecialScreenCommand.OnSpecialClick -> {
                        when (currentUseCase) {
                            OnSpecialFeatureUseCase.OnBoard -> {
                                onNavigate(onBoardUseCase.getNextRoute(command.onClickedSpecId))
                            }
                            OnSpecialFeatureUseCase.Specilialization -> {
                                onNavigate(specialUseCase.getNextRoute(command.onClickedSpecId))
                            }
                        }
                    }

                    OnSpecialScreenCommand.OnBackClick -> {
                        onBackClick
                    }
                }
            }
    }
}