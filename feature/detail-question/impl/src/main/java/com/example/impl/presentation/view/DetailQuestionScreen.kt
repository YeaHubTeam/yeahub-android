package com.example.impl.presentation.view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.impl.R
import com.example.impl.presentation.viewmodel.DetailQuestionViewModel
import com.example.impl.presentation.viewmodel.PublicQuestion
import com.valentinilk.shimmer.shimmer
import org.koin.androidx.compose.koinViewModel
import ru.yeahub.core_ui.component.DetailHeaderQuestion
import ru.yeahub.core_ui.component.DetailedQuestionAnswer
import ru.yeahub.core_ui.component.DetailedQuestionAnswerBlock
import ru.yeahub.core_ui.component.FavoriteState
import ru.yeahub.core_ui.component.GuruCard
import ru.yeahub.core_ui.component.GuruData
import ru.yeahub.core_ui.component.PopoverQuestion
import ru.yeahub.core_ui.component.ShortQuestionAnswer
import ru.yeahub.core_ui.example.dynamicPreview.StandardScreenSizePreview
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme

@Composable
fun DetailQuestionScreen(
    onBackClick: () -> Unit,
) {
    val viewModel: DetailQuestionViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DetailQuestionScreenView(
        onBackClick = onBackClick,
        uiState = uiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailQuestionScreenView(
    onBackClick: () -> Unit,
    uiState: DetailQuestionState
) {
    Scaffold(
        topBar = {
            Box(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    modifier = Modifier,
                    title = { },
                    navigationIcon = { },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Theme.colors.white900
                    ),
                    actions = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            Row(
                                modifier = Modifier
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = ripple(bounded = false),
                                    ) { onBackClick() }
                                    .animateContentSize()
                                    .width(IntrinsicSize.Min),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.back_button_icon),
                                    contentDescription = "icon_back",
                                    tint = Theme.colors.purple700,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = stringResource(R.string.back_button_text),
                                    style = Theme.typography.body3Accent,
                                    color = Theme.colors.purple700
                                )
                            }
                        }
                    }
                )
                HorizontalDivider(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth(),
                    color = Theme.colors.black50,
                    thickness = 1.dp
                )
            }
        }
    ) { padding ->
        when (val state = uiState) {
            is DetailQuestionState.Initial -> Unit
            is DetailQuestionState.LoadedState -> QuestionContent(state.data, padding)
            is DetailQuestionState.LoadingState -> LoadingScreen(padding)
            is DetailQuestionState.ErrorState -> ErrorScreen(
                state.message,
                onRetry = { }
            )
        }
    }
}

@Composable
fun LoadingScreen(padding: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .shimmer()
    ) {
        item {
            Column(
                modifier = Modifier.padding(10.dp, 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(332.dp)
                        .background(Theme.colors.black100)
                        .clip(shape = RoundedCornerShape(12.dp))
                )

                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(225.dp)
                            .background(Theme.colors.black100)
                            .clip(shape = RoundedCornerShape(12.dp))
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp)
                            .background(Theme.colors.black100)
                            .clip(shape = RoundedCornerShape(12.dp))
                    )
                }
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
                    .height(100.dp)
                    .background(Theme.colors.black100)
                    .clip(shape = RoundedCornerShape(12.dp))
            )
        }
        item {
            Column(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(458.dp)
                        .background(Theme.colors.black100)
                        .clip(shape = RoundedCornerShape(12.dp))
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(458.dp)
                        .background(Theme.colors.black100)
                        .clip(shape = RoundedCornerShape(12.dp))
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(175.dp)
                    .padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
                    .background(Theme.colors.black100)
            )
        }
    }
}

@Composable
fun QuestionContent(question: PublicQuestion, padding: PaddingValues) {
    val blocks = listOf(
        DetailedQuestionAnswerBlock.TextBlock(question.longAnswer),
        DetailedQuestionAnswerBlock.CodeBlock(question.code),
        DetailedQuestionAnswerBlock.ImageBlock("")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        item {
            Box(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(10.dp, 16.dp)
            ) {
                DetailHeaderQuestion(
                    questionTitle = question.title,
                    additionalText = question.description,
                    imageUrl = question.imageSrc,
                    onFilterClick = {},
                )
            }
        }
        item {
            PopoverQuestion(
                favoriteState = FavoriteState.DISABLED,
                onPreviousClick = { },
                onNextClick = {},
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
            )
        }
        item {
            Box(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
                    .height(IntrinsicSize.Min)
            ) {
                ShortQuestionAnswer(
                    answerText = question.shortAnswer
                )
            }
        }
        item {
            DetailedQuestionAnswer(
                blocks = blocks,
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
            )
        }
        item {
            GuruCard(
                data = GuruData(
                    name = "Руслан Куянец",
                    position = "Python Guru",
                    description = "Guru – это эксперты YeaHub, которые помогают развивать комьюнити.",
                    photoUrl = "",
                    profileUrl = "",
                    youtubeUrl = "https://www.youtube.com/watch?v=Cqu2HMJl44Q",
                    telegramUrl = "",
                ),
                onTelegramClick = {},
                onYoutubeClick = {},
            ) { }
        }
    }
}

class ListOfDetailQuestionStatesProvider : PreviewParameterProvider<DetailQuestionState> {
    private val mockQuestion = PublicQuestion(
        id = 1L,
        title = "Что такое Virtual DOM, и как он работает?",
        description = "Вопрос проверяет знание React под капотом",
        code = "\"<code>val x = 5</code>\"",
        imageSrc = "",
        longAnswer = "Virtual DOM (виртуальное DOM) - это концепция, " +
                "используемая в библиотеках и фреймворках, таких как React, " +
                "для оптимизации обновлений реального DOM (Document Object Model) " +
                "и повышения производительности веб-приложений. Реальный DOM — " +
                "это представление структуры веб-страницы в браузере в виде дерева объектов. " +
                "Когда состояние приложения меняется и требуется обновление интерфейса, " +
                "браузер выполняет изменения непосредственно в реальном DOM. " +
                "Однако многократные и частые обновления реального DOM могут быть затратными " +
                "с точки зрения производительности, особенно для больших и сложных интерфейсов. " +
                "Виртуальное DOM решает эту проблему следующим образом: Создание виртуального DOM: " +
                "При изменении состояния приложения React создаёт виртуальное представление " +
                "DOM-структуры, которая является легковесной копией реального DOM. " +
                "Сравнение виртуального DOM: " +
                "React сравнивает предыдущее состояние виртуального DOM с новым состоянием, " +
                "выявляя, какие части интерфейса были изменены. " +
                "Генерация разницы (патч): " +
                "На основе сравнения React создаёт минимальный набор изменений, " +
                "необходимых для обновления виртуального DOM согласно новому состоянию. " +
                "Применение изменений: Созданные изменения применяются к реальному DOM только " +
                "одним обновлением, что позволяет избежать множественных манипуляций с реальным DOM. " +
                "Использование виртуального DOM позволяет значительно улучшить производительность, " +
                "так как обновления реального DOM происходят только в необходимых местах. " +
                "Это также делает разработку более удобной и предсказуемой, поскольку разработчику " +
                "не нужно ручным образом управлять множеством изменений на реальном DOM.",
        shortAnswer = "Virtual DOM (виртуальный DOM) — это программная концепция, " +
                "используемая в разработке веб-приложений для повышения эффективности " +
                "обновлений интерфейса. Это представление реального DOM (структуры документа, " +
                "отображаемого в браузере) в памяти, которое позволяет оптимизировать изменения, " +
                "минимизируя взаимодействие с реальным DOM, " +
                "что ускоряет рендеринг и обновление страниц. " +
                "При изменении данных приложения Virtual DOM сравнивает новое состояние" +
                "с предыдущим и обновляет только те части реального DOM, которые изменились," +
                " вместо перерисовки всего документа.",
    )

    override val values = sequenceOf(
        DetailQuestionState.Initial,
        DetailQuestionState.LoadingState,
        DetailQuestionState.LoadedState(mockQuestion),
        DetailQuestionState.ErrorState("Ошибка загрузки"),
    )
}

@StaticPreview
@Composable
fun ShowDetailQuestionPreview(
    @PreviewParameter(ListOfDetailQuestionStatesProvider::class) state: DetailQuestionState
) {
    StatesDetailQuestionPreview(state)
}

@Composable
fun StatesDetailQuestionPreview(state: DetailQuestionState) {
    DetailQuestionScreenView(
        uiState = state,
        onBackClick = {}
    )
}

@StandardScreenSizePreview
@Composable
fun DetailQuestionScreenDynamicPreview() {
    val mockViewModel = DetailQuestionViewModel()
    val uiState by mockViewModel.uiState.collectAsStateWithLifecycle()
    DetailQuestionScreenView(
        onBackClick = {},
        uiState = uiState
    )
}