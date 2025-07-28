package ru.yeahub.public_questions.impl.presentation.command

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow

@Composable
fun HandleQuestionsCommand(
    commandFlow: Flow<QuestionsScreenCommand>,
    onNavigateToDetail: (id: String, title: String) -> Unit,
    onBackClick: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        commandFlow
            .flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect { command ->
                when (command) {
                    is QuestionsScreenCommand.OnMoreClick -> {
                        onNavigateToDetail(command.id, command.title)
                    }

                    QuestionsScreenCommand.OnBackClick -> {
                        onBackClick()
                    }
                }
            }
    }
}