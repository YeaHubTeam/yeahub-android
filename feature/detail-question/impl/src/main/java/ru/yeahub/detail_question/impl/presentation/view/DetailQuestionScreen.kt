package ru.yeahub.detail_question.impl.presentation.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.SharedFlow
import org.koin.androidx.compose.koinViewModel
import ru.yeahub.core_ui.component.ErrorScreen
import ru.yeahub.core_ui.component.TopAppBarWithBottomBorder
import ru.yeahub.core_ui.example.dynamicPreview.StandardScreenSizePreview
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.detail_question.impl.R
import ru.yeahub.detail_question.impl.domain.models.GuruEntity
import ru.yeahub.detail_question.impl.domain.models.NestedSkillEntity
import ru.yeahub.detail_question.impl.domain.models.NestedSpecializationEntity
import ru.yeahub.detail_question.impl.domain.models.NestedUserReferenceEntity
import ru.yeahub.detail_question.impl.domain.models.PublicQuestionEntity
import ru.yeahub.detail_question.impl.domain.usecase.GetQuestionByIdUseCase
import ru.yeahub.detail_question.impl.presentation.intents.DetailQuestionCommand
import ru.yeahub.detail_question.impl.presentation.intents.DetailQuestionEvent
import ru.yeahub.detail_question.impl.presentation.intents.DetailQuestionResult
import ru.yeahub.detail_question.impl.presentation.mapper.DetailQuestionScreenMapper
import ru.yeahub.detail_question.impl.presentation.state.DetailQuestionState
import ru.yeahub.detail_question.impl.presentation.state.GuruVO
import ru.yeahub.detail_question.impl.presentation.state.NestedSkillVO
import ru.yeahub.detail_question.impl.presentation.state.NestedSpecializationVO
import ru.yeahub.detail_question.impl.presentation.state.NestedUserReferenceVO
import ru.yeahub.detail_question.impl.presentation.viewmodel.DetailQuestionViewModel
import ru.yeahub.detail_question.impl.presentation.viewmodel.viewModelCreator

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
                is DetailQuestionCommand.OpenUrl -> onResult(DetailQuestionResult.UrlClick(command.url))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailQuestionScreenView(
    onBackClick: () -> Unit,
    viewModel: DetailQuestionViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
 Scaffold(
        containerColor = Theme.colors.black25,
        topBar = {
            TopAppBarWithBottomBorder(
                title = TextOrResource.Text(""),
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        DetailQuestionScreenState(
            uiState = uiState,
            onBackClick = onBackClick,
            onTelegramClick = {
                if (uiState is DetailQuestionState.Success) {
                    viewModel.handleEvents(
                        DetailQuestionEvent.OnTelegramClick(
                            (uiState as DetailQuestionState.Success).data.guru.telegramUrl
                        )
                    )
                }
            },
            onYoutubeClick = {
                if (uiState is DetailQuestionState.Success) {
                    viewModel.handleEvents(
                        DetailQuestionEvent.OnYoutubeClick(
                            (uiState as DetailQuestionState.Success).data.guru.youtubeUrl
                        )
                    )
                }
            },
            padding = padding
        )
    }
}

@Composable
fun DetailQuestionScreenState(
    uiState: DetailQuestionState,
    onBackClick: () -> Unit,
    onTelegramClick: () -> Unit,
    onYoutubeClick: () -> Unit,
    padding: PaddingValues
) {
    when (uiState) {
        is DetailQuestionState.Initial -> Unit
        is DetailQuestionState.Success -> QuestionContent(
            uiState.data,
            onTelegramClick,
            onYoutubeClick,
            padding,
            TextOrResource.Resource(R.string.guru_description_text)
        )

        is DetailQuestionState.LoadingState -> LoadingScreen(padding)
        is DetailQuestionState.ErrorState -> ErrorScreen(
            error = uiState.message,
            onBack = { onBackClick() },
            titleText = TextOrResource.Resource(R.string.error_screen_title_text),
            backText = TextOrResource.Resource(R.string.on_back_button_text),
            unknownErrorText = TextOrResource.Resource(R.string.unknown_error_screen_text),
            errorText = TextOrResource.Resource(R.string.error_screen_text),
        )
    }
}

data class DetailQuestionScreenStateParams(
    val state: DetailQuestionState,
    val padding: PaddingValues = PaddingValues()
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
        onTelegramClick = {},
        onYoutubeClick = {},
        onBackClick = {}
    )
}

@StandardScreenSizePreview
@Composable
fun DetailQuestionScreenDynamicPreview() {
    val questionId by remember { mutableLongStateOf(1L) }
    val mockMapper = remember {
        DetailQuestionScreenMapper()
    }
    val mockGetQuestionByIUseCase = object : GetQuestionByIdUseCase {
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
        DetailQuestionViewModel(
            mockMapper,
            mockGetQuestionByIUseCase
        )
    }

    LaunchedEffect(Unit) { mockViewModel.handleEvents(DetailQuestionEvent.LoadQuestion(questionId)) }

    DetailQuestionScreenView(
        onBackClick = {},
        viewModel = mockViewModel,
    )
}