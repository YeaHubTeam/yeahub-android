package ru.yeahub.impl.presentation.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.component.HideQuestion

@Composable
fun LoadedQuestions(
    onClickMore: (itemId: String, title: String) -> Unit,
    onBackClick: () -> Unit
) {
    var isExtended by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.padding(
            top = FIGMA_PADDING_TOP.dp,
            bottom = FIGMA_PADDING_BOTTOM.dp,
            start = FIGMA_PADDING_START.dp,
            end = FIGMA_PADDING_END.dp
        )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            items(count = 12) { question ->
                HideQuestion(
                    questionText = "Что такое Virtual DOM, и как он работает?",
                    ratingValue = "10",
                    difficultyValue = "5",
                    answerText = "Никак не работает",
                    imageUrl = "",
                    isExtended = isExtended,
                    onClickMore = {},
                    onToggle = { isExtended = !isExtended },
                    modifier = Modifier
                )
            }
        }
    }
}

private const val FIGMA_PADDING_BOTTOM = 20
private const val FIGMA_PADDING_TOP = 20
private const val FIGMA_PADDING_START = 10
private const val FIGMA_PADDING_END = 10