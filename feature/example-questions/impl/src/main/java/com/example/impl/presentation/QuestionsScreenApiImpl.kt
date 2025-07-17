package com.example.impl.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.api.QuestionsScreenApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.koinViewModel
import ru.yeahub.core_utils.pagerImpl.YeaHubPagerState

class QuestionsScreenApiImpl : QuestionsScreenApi {

    @Composable
    override fun QuestionsScreen(
        onBackClick: () -> Unit,
        onDetailsClick: (itemId: String, title: String) -> Unit
    ) {
        val viewModel: QuestionsViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val lazyListState = rememberLazyListState()

        LaunchedEffect(lazyListState) {
            snapshotFlow { lazyListState.layoutInfo }.map { layout ->
                val lastItem = layout.visibleItemsInfo.lastOrNull()
                lastItem?.index == layout.totalItemsCount - 1
            }.distinctUntilChanged().collect { reachedEnd ->
                if (reachedEnd && state !is YeaHubPagerState.Error) {
                    viewModel.loadNext()
                }
            }
        }

        LaunchedEffect(Unit) {
            if (state is YeaHubPagerState.Initial) {
                viewModel.loadNext()
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            // Кнопка для перехода к деталям
            Button(
                onClick = {
                    onDetailsClick("question_item_1", "Детали с экрана вопросов")
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Перейти к деталям")
            }
            
            // Основной контент
            Box(modifier = Modifier.weight(1f)) {
                when (val currentState = state) {
                is YeaHubPagerState.Initial -> {
                    LazyColumn(state = lazyListState) {
                        items(10) {
                            PlaceholderItem()
                        }
                    }
                }

                is YeaHubPagerState.Loading -> {
                    if (currentState.items.isEmpty()) {
                        LazyColumn(state = lazyListState) {
                            items(10) {
                                PlaceholderItem()
                            }
                        }
                    } else {
                        LazyColumn(state = lazyListState) {
                            items(currentState.items) { question ->
                                QuestionItem(question = question)
                            }
                            items(10) {
                                PlaceholderItem()
                            }
                        }
                    }
                }

                is YeaHubPagerState.Loaded -> {
                    LazyColumn(state = lazyListState) {
                        items(currentState.items) { question ->
                            QuestionItem(question = question)
                        }
                    }
                }

                is YeaHubPagerState.Error -> if (currentState.items.isEmpty()) {
                    ErrorScreen(
                        error = currentState.throwable,
                        onRetry = {
                            viewModel.refresh()
                        }
                    )
                } else {
                    LazyColumn(state = lazyListState) {
                        items(currentState.items) { question ->
                            QuestionItem(question = question)
                        }
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                ErrorItem(
                                    error = currentState.throwable,
                                    onRetry = {
                                        viewModel.loadNext()
                                    }
                                )
                            }
                        }
                    }
                }
                }
            }
        }
    }
}