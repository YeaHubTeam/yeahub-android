package ru.yeahub.interview_trainer.impl.createQuiz.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import ru.yeahub.core_ui.component.ErrorScreen
import ru.yeahub.core_ui.component.PrimaryButton
import ru.yeahub.core_ui.component.SkillButton
import ru.yeahub.core_ui.component.TopAppBarWithBottomBorder
import ru.yeahub.core_ui.example.dynamicPreview.ProvidePreviewCompositionLocals
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.LocalAppTypography
import ru.yeahub.core_ui.theme.colors
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.interview_trainer.impl.R
import ru.yeahub.interview_trainer.impl.createQuiz.domain.DomainSpecialization
import ru.yeahub.interview_trainer.impl.createQuiz.domain.DomainSpecializationListResponse
import ru.yeahub.interview_trainer.impl.createQuiz.domain.GetSpecializationListUseCase
import ru.yeahub.interview_trainer.impl.createQuiz.presentation.CreateQuizEvent
import ru.yeahub.interview_trainer.impl.createQuiz.presentation.CreateQuizState
import ru.yeahub.interview_trainer.impl.createQuiz.presentation.CreateQuizViewModel

private val FIGMA_HORIZONTAL_PADDING = 16.dp
private val FIGMA_VERTICAL_BLOCKS_PADDING = 16.dp
private val FIGMA_VERTICAL_FIRST_AND_LAST_ELEMENT_PADDING = 24.dp

@Composable
private fun ScreenUI(
    state: CreateQuizState,
    onEvent: (CreateQuizEvent) -> Unit,
    headerText: TextOrResource = TextOrResource.Resource(R.string.create_quiz_top_bar_header_text),
) {
    Scaffold(
        containerColor = colors.black10,
        topBar = {
            TopAppBarWithBottomBorder(
                title = headerText,
                onBackClick = { onEvent(CreateQuizEvent.OnBackClick) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            when (state) {
                CreateQuizState.Loading -> CreateQuizLoading()

                is CreateQuizState.Error -> {
                    ErrorScreen(
                        error = state.throwable.localizedMessage,
                        errorText = TextOrResource.Resource(R.string.error_screen_text),
                        titleText = TextOrResource.Resource(R.string.title_error_screen_text),
                        backText = TextOrResource.Resource(R.string.back_error_screen_text),
                        unknownErrorText = TextOrResource.Resource(R.string.unknown_error_screen_text),
                        onBack = { onEvent(CreateQuizEvent.OnBackClick) }
                    )
                }

                is CreateQuizState.Loaded -> BaseCreateQuizScreen(
                    specializations = state.specializations,
                    selectedSpecializationId = state.selectedSpecializationId,
                    questionsCount = state.questionsCount,
                    onSpecializationClick = { id ->
                        onEvent(CreateQuizEvent.OnSpecializationClick(specializationId = id))
                    },
                    onPlusQuestionCountClick = { count ->
                        onEvent(CreateQuizEvent.OnPlusQuestionClick(questionsCount = count))
                    },
                    onMinusQuestionCountClick = { count ->
                        onEvent(CreateQuizEvent.OnMinusQuestionClick(questionsCount = count))
                    },
                    onStartQuizClick = { specializationId: Long, questionsCount: Int ->
                        onEvent(
                            CreateQuizEvent.OnStartInterviewQuizClick(
                                specializationId = specializationId,
                                questionCount = questionsCount,
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun BaseCreateQuizScreen(
    specializations: List<CreateQuizState.Loaded.VoSpecialization>,
    selectedSpecializationId: Long,
    questionsCount: Int,
    onSpecializationClick: (id: Long) -> Unit,
    onPlusQuestionCountClick: (count: Int) -> Unit,
    onMinusQuestionCountClick: (count: Int) -> Unit,
    onStartQuizClick: (specializationId: Long, questionsCount: Int) -> Unit,
    modifier: Modifier = Modifier,
    titleText: TextOrResource = TextOrResource.Resource(R.string.create_quiz_screen_main_title),
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .padding(horizontal = FIGMA_HORIZONTAL_PADDING),
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = FIGMA_VERTICAL_FIRST_AND_LAST_ELEMENT_PADDING),
            text = titleText.getString(context),
            style = LocalAppTypography.current.head5,
        )

        ChooseSpecializationBlock(
            context = context,
            specializations = specializations,
            selectedSpecializationId = selectedSpecializationId,
            onSpecializationClick = onSpecializationClick
        )

        Spacer(modifier = Modifier.height(FIGMA_VERTICAL_BLOCKS_PADDING))

        ChooseQuestionsCountBlock(
            context = context,
            questionsCount = questionsCount,
            onPlusQuestionCountClick = onPlusQuestionCountClick,
            onMinusQuestionCountClick = onMinusQuestionCountClick,
        )

        Spacer(modifier = Modifier.weight(1f))

        StartQuizButton(
            specializationId = selectedSpecializationId,
            questionsCount = questionsCount,
            onStartQuizClick = onStartQuizClick,
        )
    }
}

@Composable
private fun ChooseSpecializationBlock(
    specializations: List<CreateQuizState.Loaded.VoSpecialization>,
    selectedSpecializationId: Long,
    context: Context,
    onSpecializationClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    titleText: TextOrResource = TextOrResource.Resource(R.string.create_quiz_specialization_param_header_text),
) {
    Column(modifier = modifier) {
        Text(
            style = LocalAppTypography.current.body3Accent,
            text = titleText.getString(context),
        )

        Spacer(modifier = Modifier.height(12.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                space = 12.dp,
                alignment = Alignment.Start
            ),
            verticalArrangement = Arrangement.spacedBy(
                space = 12.dp,
                alignment = Alignment.Top
            ),
        ) {
            specializations.forEach { specialization ->
                SkillButton(
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    enabled = true,
                    activeButton = specialization.id == selectedSpecializationId,
                    fillButton = true,
                    text = specialization.title,
                    onClick = { onSpecializationClick(specialization.id) },
                )
            }
        }
    }
}

@Composable
private fun ChooseQuestionsCountBlock(
    context: Context,
    questionsCount: Int,
    onPlusQuestionCountClick: (count: Int) -> Unit,
    onMinusQuestionCountClick: (count: Int) -> Unit,
    modifier: Modifier = Modifier,
    titleText: TextOrResource = TextOrResource.Resource(R.string.create_quiz_question_count_param_header_text),
) {
    Column(modifier = modifier) {
        Text(
            style = LocalAppTypography.current.body3Accent,
            text = titleText.getString(context),
        )

        Spacer(modifier = Modifier.height(12.dp))

        QuestionCounter(
            count = questionsCount,
            onPlusQuestionCountClick = onPlusQuestionCountClick,
            onMinusQuestionCountClick = onMinusQuestionCountClick
        )
    }
}

@Composable
private fun QuestionCounter(
    onPlusQuestionCountClick: (count: Int) -> Unit,
    onMinusQuestionCountClick: (count: Int) -> Unit,
    modifier: Modifier = Modifier,
    count: Int,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = colors.black50,
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 6.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = { onMinusQuestionCountClick(count) }
            ) {
                Icon(
                    painter = painterResource(R.drawable.minus_icon),
                    contentDescription = "Decrease questions count",
                    tint = colors.black600
                )
            }

            Text(
                text = count.toString(),
                style = LocalAppTypography.current.body5Accent,
                textAlign = TextAlign.Center,
                color = colors.black600
            )

            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = { onPlusQuestionCountClick(count) }
            ) {
                Icon(
                    painter = painterResource(R.drawable.plus_icon),
                    contentDescription = "Increase questions count",
                    tint = colors.black600,
                )
            }
        }
    }
}

@Composable
private fun StartQuizButton(
    specializationId: Long,
    questionsCount: Int,
    onStartQuizClick: (specializationId: Long, questionsCount: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    PrimaryButton(
        modifier = modifier
            .padding(vertical = FIGMA_VERTICAL_FIRST_AND_LAST_ELEMENT_PADDING)
            .height(48.dp)
            .fillMaxWidth(),
        onClick = { onStartQuizClick(specializationId, questionsCount) }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Начать",
                style = LocalAppTypography.current.body3Strong,
                textAlign = TextAlign.Center,
                color = colors.white900
            )

            Spacer(Modifier.width(8.dp))

            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(ru.yeahub.ui.R.drawable.arrow_next),
                contentDescription = "Start Interview Quiz Session",
                tint = colors.white900
            )
        }
    }
}

val specializations = listOf(
    CreateQuizState.Loaded.VoSpecialization(
        id = 11,
        title = "Frontend"
    ),
    CreateQuizState.Loaded.VoSpecialization(
        id = 1,
        title = "Backend"
    ),
    CreateQuizState.Loaded.VoSpecialization(
        id = 2,
        title = "Data Science"
    ),
    CreateQuizState.Loaded.VoSpecialization(
        id = 3,
        title = "Machine Learning"
    ),
    CreateQuizState.Loaded.VoSpecialization(
        id = 4,
        title = "Testing"
    ),
    CreateQuizState.Loaded.VoSpecialization(
        id = 5,
        title = "iOS Dev"
    ),
    CreateQuizState.Loaded.VoSpecialization(
        id = 21,
        title = "Android Dev"
    ),
    CreateQuizState.Loaded.VoSpecialization(
        id = 6,
        title = "Game Dev"
    )
)

class CreateQuizScreenStateParamProvider : PreviewParameterProvider<CreateQuizState> {
    override val values: Sequence<CreateQuizState> = sequenceOf(
        CreateQuizState.Loaded(
            specializations = specializations
        ),
        CreateQuizState.Loading,
        CreateQuizState.Error(
            Throwable("Не удалось загрузить данные")
        )
    )
}

@StaticPreview
@Composable
fun CreateQuizScreenPreview(
    @PreviewParameter(CreateQuizScreenStateParamProvider::class)
    state: CreateQuizState,
) {
    ScreenUI(
        state = state,
        onEvent = { },
    )
}

@Preview(showBackground = true)
@Composable
fun DynamicPreviewUI() {
    val mockSpecsListUseCase = object : GetSpecializationListUseCase {
        override suspend fun invoke(): DomainSpecializationListResponse {
            delay(RESPONSE_DELAY)
            return DomainSpecializationListResponse(
                data = specializations.map {
                    DomainSpecialization(
                        it.id,
                        it.title
                    )
                },
                total = specializations.count().toLong()
            )
        }
    }

    val mockViewModel = viewModelCreator<CreateQuizViewModel> {
        CreateQuizViewModel(mockSpecsListUseCase)
    }

    val state by mockViewModel.screenState.collectAsState()

    LaunchedEffect(Unit) {
        //Изначальное кол-во == 1
        mockViewModel.onEvent(CreateQuizEvent.OnPlusQuestionClick(1))
        // должно быть 2
        mockViewModel.onEvent(CreateQuizEvent.OnPlusQuestionClick(2))
        // должно быть 3
        mockViewModel.onEvent(CreateQuizEvent.OnMinusQuestionClick(3))
        // должно быть снова 2
        mockViewModel.onEvent(CreateQuizEvent.OnSpecializationClick(21))
        // С изначально выбранного Frontend Dev должно быть выбрано Android Dev
    }

    ProvidePreviewCompositionLocals {
        ScreenUI(
            state = state,
            onEvent = mockViewModel::onEvent
        )
    }
}

typealias ViewModelCreator = () -> ViewModel?

class ViewModelFactory(
    private val viewModelCreator: ViewModelCreator = { null },
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModelCreator() as T
}

@Composable
inline fun <reified VM : ViewModel> viewModelCreator(noinline creator: ViewModelCreator): VM =
    viewModel(factory = remember { ViewModelFactory(creator) })

private const val RESPONSE_DELAY = 2500L