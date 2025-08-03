package ru.yeahub.public_questions.impl.presentation.command

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow

@Composable
fun HandlePublicQuestionsCommand(
    commandFlow: Flow<PublicQuestionsScreenCommand>,
    onNavigateToDetail: (id: String) -> Unit,
    onBackClick: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(key1 = commandFlow, key2 = lifecycleOwner) {
        commandFlow
            .flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect { command ->
                when (command) {
                    is PublicQuestionsScreenCommand.OnMoreClick -> {
                        onNavigateToDetail(command.id)
                    }

                    PublicQuestionsScreenCommand.OnBackClick -> {
                        onBackClick()
                    }
                }
            }
    }
}