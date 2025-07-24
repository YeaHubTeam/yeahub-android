package com.example.impl.presentation.view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.impl.R
import com.example.impl.domain.models.GuruEntity
import com.example.impl.domain.models.NestedSkillEntity
import com.example.impl.domain.models.NestedSpecializationEntity
import com.example.impl.domain.models.NestedUserReferenceEntity
import com.example.impl.domain.models.PublicQuestionEntity
import com.example.impl.domain.usecase.GetQuestionByIdUseCase
import com.example.impl.presentation.intents.DetailQuestionCommand
import com.example.impl.presentation.intents.DetailQuestionEvent
import com.example.impl.presentation.intents.DetailQuestionResult
import com.example.impl.presentation.mapper.DetailQuestionScreenMapper
import com.example.impl.presentation.state.DetailQuestionState
import com.example.impl.presentation.state.GuruVO
import com.example.impl.presentation.state.NestedSkillVO
import com.example.impl.presentation.state.NestedSpecializationVO
import com.example.impl.presentation.state.NestedUserReferenceVO
import com.example.impl.presentation.viewmodel.DetailQuestionViewModel
import com.example.impl.presentation.viewmodel.viewModelCreator
import kotlinx.coroutines.flow.SharedFlow
import org.koin.androidx.compose.koinViewModel
import ru.yeahub.core_ui.example.dynamicPreview.StandardScreenSizePreview
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme

@Composable
fun DetailQuestionScreen(
    onResult: (DetailQuestionResult) -> Unit,
    questionId: Long
) {
    val viewModel: DetailQuestionViewModel = koinViewModel()

    LaunchedEffect(Unit) {
        viewModel.handleEvents(DetailQuestionEvent.LoadQuestion(questionId))
    }

    DetailQuestionScreenView(
        onBackClick = { viewModel.handleEvents(DetailQuestionEvent.OnBackClick) },
        onPrevClick = { viewModel.handleEvents(DetailQuestionEvent.OnPrevClick) },
        onNextClick = { viewModel.handleEvents(DetailQuestionEvent.OnNextClick) },
        viewModel = viewModel
    )

    HandleCommands(viewModel.commands, onResult)
}

@Composable
internal fun HandleCommands(
    commands: SharedFlow<DetailQuestionCommand>,
    onResult: (DetailQuestionResult) -> Unit
) {
    LaunchedEffect(Unit) {
        commands.collect { command ->
            when (command) {
                DetailQuestionCommand.NavigateBack -> onResult(DetailQuestionResult.BackClick)
                DetailQuestionCommand.NavigateNextPage -> onResult(DetailQuestionResult.NextClick)
                DetailQuestionCommand.NavigatePrevPage -> onResult(DetailQuestionResult.PrevClick)
                is DetailQuestionCommand.OpenUrl -> onResult(DetailQuestionResult.UrlClick(command.url))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailQuestionScreenView(
    onBackClick: () -> Unit,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    viewModel: DetailQuestionViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        containerColor = Theme.colors.black25,
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
        DetailQuestionScreenState(uiState = uiState, onPrevClick = onPrevClick, onTelegramClick = {
            if (uiState is DetailQuestionState.Success) {
                viewModel.handleEvents(
                    DetailQuestionEvent.OnTelegramClick((uiState as DetailQuestionState.Success).data.guru.telegramUrl)
                )
            }
        }, onYoutubeClick = {
            if (uiState is DetailQuestionState.Success) {
                viewModel.handleEvents(
                    DetailQuestionEvent.OnYoutubeClick((uiState as DetailQuestionState.Success).data.guru.youtubeUrl)
                )
            }
        }, onNextClick = onNextClick, padding = padding)
    }
}

@Composable
fun DetailQuestionScreenState(
    uiState: DetailQuestionState,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    onTelegramClick: () -> Unit,
    onYoutubeClick: () -> Unit,
    padding: PaddingValues,
    errorStrings: DetailQuestionStrings = rememberDetailQuestionStrings()
) {
    when (uiState) {
        is DetailQuestionState.Initial -> Unit
        is DetailQuestionState.Success -> QuestionContent(
            uiState.data,
            onPrevClick,
            onNextClick,
            onTelegramClick,
            onYoutubeClick,
            padding,
            errorStrings
        )

        is DetailQuestionState.LoadingState -> LoadingScreen(padding)
        is DetailQuestionState.ErrorState -> ErrorScreen(
            uiState.message,
            onRetry = { },
            errorStrings
        )
    }
}

data class DetailQuestionScreenStateParams(
    val state: DetailQuestionState,
    val padding: PaddingValues = PaddingValues(),
    val errorStrings: DetailQuestionStrings = previewErrorScreenStrings()
)

class ListOfDetailQuestionScreenStateProvider :
    PreviewParameterProvider<DetailQuestionScreenStateParams> {

    private val publicQuestion = DetailQuestionState.Success.PublicQuestionVO(
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
        keywords = listOf(),
        status = null,
        rate = null,
        complexity = null,
        createdById = "",
        updatedById = "",
        questionSpecializations = listOf(
            NestedSpecializationVO(
                id = 11,
                title = "",
                description = "",
                imageSrc = "",
                createdAt = "",
                updatedAt = ""
            )
        ),
        questionSkills = listOf(
            NestedSkillVO(
                id = 11,
                title = "",
                description = "",
                imageSrc = "",
                createdAt = "",
                updatedAt = ""
            )
        ),
        createdAt = "",
        updatedAt = "",
        createdBy = NestedUserReferenceVO(
            id = "",
            username = ""
        ),
        updatedBy = null,
        guru = GuruVO(
            name = "Ruslan Kuyanets",
            title = "Frontend Guru",
            specializationId = 11,
            photoUrl = "https://e5e684b1-4a6a-4be5-b7ee-b2b678239d61.selstorage.ru/" +
                    "gurus/%D1%80%D1%83%D1%81%D0%BB%D0%B0%D0%BD%D0%BC%D0%B5%D0%BD%D1%82%D0%BE%D1%80%20(1).jpeg",
            youtubeUrl = "https://youtube.com/@reactify-it",
            telegramUrl = "https://t.me/reactify_IT"
        )
    )
    override val values = sequenceOf(
        DetailQuestionScreenStateParams(DetailQuestionState.LoadingState),
        DetailQuestionScreenStateParams(DetailQuestionState.Success(publicQuestion)),
        DetailQuestionScreenStateParams(DetailQuestionState.ErrorState("Не удалось загрузить вопрос")),
    )
}

@StaticPreview
@Composable
fun ShowDetailQuestionPreview(
    @PreviewParameter(ListOfDetailQuestionScreenStateProvider::class) params: DetailQuestionScreenStateParams
) {
    StatesDetailQuestionPreview(params)
}

@Composable
fun StatesDetailQuestionPreview(params: DetailQuestionScreenStateParams) {
    DetailQuestionScreenState(
        uiState = params.state,
        padding = params.padding,
        errorStrings = params.errorStrings,
        onPrevClick = {},
        onNextClick = {},
        onTelegramClick = {},
        onYoutubeClick = {}
    )
}

@StandardScreenSizePreview
@Composable
fun DetailQuestionScreenDynamicPreview() {
    val questionId by remember { mutableLongStateOf(1L) }
    val mockMapper = remember {
        DetailQuestionScreenMapper()
    }
    val mockUseCase = object : GetQuestionByIdUseCase {
        override suspend fun invoke(id: Long): PublicQuestionEntity {
            return PublicQuestionEntity(
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
                keywords = listOf(),
                status = null,
                rate = null,
                complexity = null,
                createdById = "",
                updatedById = "",
                questionSpecializations = listOf(
                    NestedSpecializationEntity(
                        id = 27,
                        title = "",
                        description = "",
                        imageSrc = "",
                        createdAt = "",
                        updatedAt = ""
                    )
                ),
                questionSkills = listOf(
                    NestedSkillEntity(
                        id = 11,
                        title = "",
                        description = "",
                        imageSrc = "",
                        createdAt = "",
                        updatedAt = ""
                    )
                ),
                createdAt = "",
                updatedAt = "",
                createdBy = NestedUserReferenceEntity(
                    id = "",
                    username = ""
                ),
                updatedBy = null,
                guru = GuruEntity(
                    name = "Ruslan Kuyanets",
                    title = "Frontend Guru",
                    specializationId = 11,
                    photoUrl = "https://e5e684b1-4a6a-4be5-b7ee-b2b678239d61.selstorage.ru/" +
                            "gurus/%D1%80%D1%83%D1%81%D0%BB%D0%B0%D0%BD%D0%BC%D0%B5%D0%BD%D1%82%D0%BE%D1%80%20(1).jpeg",
                    youtubeUrl = "https://youtube.com/@reactify-it",
                    telegramUrl = "https://t.me/reactify_IT"
                )
            )
        }
    }
    val mockViewModel: DetailQuestionViewModel = viewModelCreator {
        DetailQuestionViewModel(mockMapper, mockUseCase)
    }

    LaunchedEffect(Unit) { mockViewModel.handleEvents(DetailQuestionEvent.LoadQuestion(questionId)) }

    DetailQuestionScreenView(
        onBackClick = {}, viewModel = mockViewModel,
        onPrevClick = {},
        onNextClick = {}
    )
}