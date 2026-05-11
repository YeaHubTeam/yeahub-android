package ru.yeahub.interview_trainer.impl.createQuiz.ui

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import ru.yeahub.core_ui.component.ErrorScreen
import ru.yeahub.core_ui.component.PrimaryButton
import ru.yeahub.core_ui.component.SkillButton
import ru.yeahub.core_ui.component.TopAppBarWithBottomBorder
import ru.yeahub.core_ui.example.dynamicPreview.DynamicPreview
import ru.yeahub.core_ui.example.dynamicPreview.ProvideDynamicPreview
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.LocalAppTypography
import ru.yeahub.core_ui.theme.colors
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.core_utils.common.observe
import ru.yeahub.interview_trainer.impl.R
import ru.yeahub.interview_trainer.impl.createQuiz.domain.DomainSpecialization
import ru.yeahub.interview_trainer.impl.createQuiz.domain.DomainSpecializationListResponse
import ru.yeahub.interview_trainer.impl.createQuiz.domain.GetSpecializationsListUseCase
import ru.yeahub.interview_trainer.impl.createQuiz.domain.SpecializationsRequest
import ru.yeahub.interview_trainer.impl.createQuiz.presentation.CreateQuizCommand
import ru.yeahub.interview_trainer.impl.createQuiz.presentation.CreateQuizEvent
import ru.yeahub.interview_trainer.impl.createQuiz.presentation.CreateQuizResult
import ru.yeahub.interview_trainer.impl.createQuiz.presentation.CreateQuizScreenMapper
import ru.yeahub.interview_trainer.impl.createQuiz.presentation.CreateQuizState
import ru.yeahub.interview_trainer.impl.createQuiz.presentation.CreateQuizViewModel

private val FIGMA_VERTICAL_FIRST_AND_LAST_ELEMENT_PADDING = 24.dp

@Composable
fun CreateQuizScreen(onResult: (CreateQuizResult) -> Unit) {
    val viewModel: CreateQuizViewModel = koinViewModel()

    val screenState = viewModel.screenState.collectAsStateWithLifecycle()

    HandleCommand(
        commandFlow = viewModel.commands,
        onResult = onResult
    )

    ScreenUI(
        state = screenState,
        onEvent = viewModel::onEvent,
        titleTopAppBar = screenState.value.titleTopAppBar
    )
}

@Composable
private fun ScreenUI(
    state: State<CreateQuizState>,
    onEvent: (CreateQuizEvent) -> Unit,
    titleTopAppBar: TextOrResource,
) {
    Scaffold(
        containerColor = colors.black10,
        topBar = {
            TopAppBarWithBottomBorder(
                title = titleTopAppBar,
                onBackClick = { onEvent(CreateQuizEvent.OnBackClick) }
            )
        }
    ) { paddingValues ->
        when (val currentState = state.value) {
            CreateQuizState.Loading -> CreateQuizLoading(paddingValues = paddingValues)

            is CreateQuizState.Error -> ErrorScreen(
                error = currentState.throwable.localizedMessage,
                errorText = TextOrResource.Resource(R.string.error_screen_text),
                titleText = TextOrResource.Resource(R.string.title_error_screen_text),
                backText = TextOrResource.Resource(R.string.back_error_screen_text),
                unknownErrorText = TextOrResource.Resource(R.string.unknown_error_screen_text),
                onBack = { onEvent(CreateQuizEvent.OnBackClick) }
            )

            is CreateQuizState.Loaded -> BaseCreateQuizScreen(
                specializations = currentState.specializations,
                selectedSpecializationId = currentState.selectedSpecializationId,
                questionsCount = currentState.questionsCount,
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
                },
                titleText = stringResource(R.string.create_quiz_screen_main_title),
                paddingValues = paddingValues
            )
        }
    }
}

@Composable
private fun HandleCommand(
    commandFlow: Flow<CreateQuizCommand>,
    onResult: (CreateQuizResult) -> Unit,
) {
    commandFlow.observe { command ->
        when (command) {
            is CreateQuizCommand.NavigateBack -> onResult(CreateQuizResult.NavigateBack)
            is CreateQuizCommand.NavigateToInterviewQuizScreen -> onResult(
                CreateQuizResult.NavigateToInterviewQuizScreen(
                    specializationId = command.specializationId,
                    questionCount = command.questionCount
                )
            )
        }
    }
}

@Composable
private fun BaseCreateQuizScreen(
    specializations: ImmutableList<CreateQuizState.Loaded.VoSpecialization>,
    selectedSpecializationId: Long,
    questionsCount: Int,
    onSpecializationClick: (id: Long) -> Unit,
    onPlusQuestionCountClick: (count: Int) -> Unit,
    onMinusQuestionCountClick: (count: Int) -> Unit,
    onStartQuizClick: (specializationId: Long, questionsCount: Int) -> Unit,
    titleText: String,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(paddingValues = paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = FIGMA_VERTICAL_FIRST_AND_LAST_ELEMENT_PADDING),
                text = titleText,
                style = LocalAppTypography.current.head5,
            )

            ChooseSpecializationBlock(
                specializations = specializations,
                selectedSpecializationId = selectedSpecializationId,
                onSpecializationClick = onSpecializationClick,
                titleText = stringResource(R.string.create_quiz_specialization_param_header_text)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ChooseQuestionsCountBlock(
                questionsCount = questionsCount,
                onPlusQuestionCountClick = onPlusQuestionCountClick,
                onMinusQuestionCountClick = onMinusQuestionCountClick,
                titleText = stringResource(R.string.create_quiz_question_count_param_header_text)
            )

            Spacer(modifier = Modifier.weight(1f))

            StartQuizButton(
                specializationId = selectedSpecializationId,
                questionsCount = questionsCount,
                onStartQuizClick = onStartQuizClick,
            )
        }
    }
}

@Composable
private fun ChooseSpecializationBlock(
    specializations: ImmutableList<CreateQuizState.Loaded.VoSpecialization>,
    selectedSpecializationId: Long,
    onSpecializationClick: (Long) -> Unit,
    titleText: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            style = LocalAppTypography.current.body3Accent,
            text = titleText,
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
                    text = specialization.title,
                    selected = specialization.id == selectedSpecializationId,
                    onClick = { onSpecializationClick(specialization.id) },
                )
            }
        }
    }
}

@Composable
private fun ChooseQuestionsCountBlock(
    questionsCount: Int,
    onPlusQuestionCountClick: (count: Int) -> Unit,
    onMinusQuestionCountClick: (count: Int) -> Unit,
    titleText: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            style = LocalAppTypography.current.body3Accent,
            text = titleText,
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
    count: Int,
    modifier: Modifier = Modifier,
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
                    contentDescription = stringResource(R.string.decrease_question_count_content_description),
                    tint = colors.black600
                )
            }

            Text(
                text = count.toString(),
                style = LocalAppTypography.current.body5Accent,
                textAlign = TextAlign.Center,
                color = colors.black600,
                modifier = Modifier.widthIn(min = 32.dp)
            )

            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = { onPlusQuestionCountClick(count) }
            ) {
                Icon(
                    painter = painterResource(R.drawable.plus_icon),
                    contentDescription = stringResource(R.string.increase_question_count_content_description),
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
                text = stringResource(R.string.create_quiz_start_quiz_button_text),
                style = LocalAppTypography.current.body3Strong,
                textAlign = TextAlign.Center,
                color = colors.white900
            )

            Spacer(Modifier.width(8.dp))

            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(ru.yeahub.ui.R.drawable.arrow_next),
                contentDescription = stringResource(R.string.start_interview_training_content_description),
                tint = colors.white900
            )
        }
    }
}

private val testSpecializations = persistentListOf(
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
        id = 27,
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
            specializations = testSpecializations,
            selectedSpecializationId = 11,
            questionsCount = 1
        ),
        CreateQuizState.Loading,
        CreateQuizState.Error(Throwable("Не удалось загрузить данные"))
    )
}

@StaticPreview
@Composable
internal fun CreateQuizScreenStaticPreview(
    @PreviewParameter(CreateQuizScreenStateParamProvider::class)
    state: CreateQuizState,
) {
    ScreenUI(
        state = rememberUpdatedState(state),
        onEvent = { },
        titleTopAppBar = TextOrResource.Resource(R.string.create_quiz_top_bar_header_text),
    )
}

@DynamicPreview
@Composable
internal fun CreateQuizScreenDynamicPreview() {
    val previewDomainList = testSpecializations.map { voSpec ->
        DomainSpecialization(id = voSpec.id, title = voSpec.title)
    }

    val mockUseCase = object : GetSpecializationsListUseCase {
        override suspend fun invoke(
            request: SpecializationsRequest,
        ): DomainSpecializationListResponse = DomainSpecializationListResponse(
            total = previewDomainList.size.toLong(),
            data = previewDomainList
        )
    }

    ProvideDynamicPreview(
        moduleDeclaration = {
            single<GetSpecializationsListUseCase> { mockUseCase }
            single { CreateQuizScreenMapper() }
            viewModel {
                CreateQuizViewModel(getSpecializationsListUseCase = get(), screenMapper = get())
            }
        },
        content = {
            val previewViewModel = koinViewModel<CreateQuizViewModel>()
            val previewState = previewViewModel.screenState.collectAsStateWithLifecycle()

            ScreenUI(
                state = previewState,
                onEvent = previewViewModel::onEvent,
                titleTopAppBar = previewState.value.titleTopAppBar
            )
        }
    )
}