package ru.yeahub.selection_specializations.impl.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.component.ErrorScreen
import ru.yeahub.core_ui.theme.LocalAppTypography
import ru.yeahub.core_ui.theme.colors
import ru.yeahub.selection_specializations.impl.ui.SpecilizationsResult.Error
import ru.yeahub.selection_specializations.impl.ui.SpecilizationsResult.Success

val FIGMA_HORIZONTAL_PADDING = 16.dp
val FIGMA_VERTICAL_TITLE_PADDING = 22.dp
val FIGMA_VERTICAL_CARD_PADDING = 16.dp //need edge_to_edge = 16

@Composable
fun SpecializationsScreen(
    backedRoute: String,
    onResult: SpecilizationsResult,
    onBackClick: () -> Unit
) {
    when (onResult) {
        is Success -> {
            SuccessSpecializationsScreen(list = onResult.specList)
        }

        is Error -> {
            ErrorScreen(
                error = onResult.errorMessage,
                titleText = "Crash",
                backText = "Back",
                unknownErrorText = "Something went wrong...",
                onBack = onBackClick
            )
        }
    }
}

@Composable
fun SuccessSpecializationsScreen(
    list: List<Specilialization>
) {
    val modifier = Modifier
    val lazyListState = rememberLazyListState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.black10)
            .padding(horizontal = FIGMA_HORIZONTAL_PADDING),
    ) {
        val titleTextStyle = LocalAppTypography.current.body5Strong
        Text(
            modifier = Modifier.padding(
                vertical = FIGMA_VERTICAL_TITLE_PADDING,
                horizontal = FIGMA_HORIZONTAL_PADDING
            ),
            fontSize = titleTextStyle.fontSize,
            fontStyle = titleTextStyle.fontStyle,
            fontWeight = titleTextStyle.fontWeight,
            fontFamily = titleTextStyle.fontFamily,
            text = "IT Specilializations",
        )

        LazyColumn(
            modifier = modifier,
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(FIGMA_VERTICAL_CARD_PADDING)
        ) {
            items(
                items = list,
                key = { spec -> spec.id }
            ) { specialization ->
                SpecializationButton(
                    title = specialization.description,
                    onClick = { }
                )
            }
        }
    }
}

@Preview
@Composable
fun SpecializationsScreenReview() {
    val exampleList =
        mutableListOf(Specilialization(description = "tooooo looooong speeeeeeeeeeeeeeeeeeeee"))
    val names = listOf("React", "Frontend", "QA", "Java")
    for (i in 0 until 15) {
        exampleList.add(Specilialization(id = i, description = names[i % names.size]))
    }

    SuccessSpecializationsScreen(list = exampleList)
}

sealed class SpecilizationsResult {
    data class Error(val errorMessage: String = "no message") : SpecilizationsResult()
    data class Success(val specList: List<Specilialization>) : SpecilizationsResult()
}

//model from backend
data class Specilialization(
    val description: String,
    val id: Int = description.hashCode(),
    val title: String = "",
    val imageSrc: String = "",
    val createdAt: String = "",
    val updatedAt: String = ""
)