package ru.yeahub.interview_trainer.impl.createQuiz.presentation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.yeahub.core_utils.BaseViewModel
import ru.yeahub.interview_trainer.impl.createQuiz.domain.GetSpecializationListUseCase
import ru.yeahub.interview_trainer.impl.createQuiz.ui.specializations

open class CreateQuizViewModel(
    private val getSpecializationListUseCase: GetSpecializationListUseCase,
) : BaseViewModel() {

    //Потом поменять на Loading. Сделано Loaded для dynamic превью. Переменная из экрана CreateQuiz
    private val _screenState = MutableStateFlow<CreateQuizState>(
        CreateQuizState.Loaded(
            specializations
        )
    )
    val screenState: StateFlow<CreateQuizState> = _screenState

    private val _commands = MutableSharedFlow<CreateQuizCommand>()
    val commands: SharedFlow<CreateQuizCommand> = _commands

    init {
        viewModelScopeSafe.launch(Dispatchers.IO) {
            val specializations = getSpecializationListUseCase()
                .data
                .map {
                    CreateQuizState.Loaded.VoSpecialization(
                        id = it.id,
                        title = it.title
                    )
                }
            _screenState.update { CreateQuizState.Loaded(specializations) }
        }
    }

    fun onEvent(event: CreateQuizEvent) {
        when (event) {
            CreateQuizEvent.OnBackClick -> onBackClick()

            is CreateQuizEvent.OnPlusQuestionClick -> incrementQuestionsCount(
                questionsCount = event.questionsCount
            )

            is CreateQuizEvent.OnMinusQuestionClick -> decrementQuestionsCount(
                questionsCount = event.questionsCount
            )

            is CreateQuizEvent.OnSpecializationClick -> changeChosenSpecialization(
                newSpecializationId = event.specializationId
            )

            is CreateQuizEvent.OnStartInterviewQuizClick -> onStartInterviewClick(
                specializationId = event.specializationId,
                questionCount = event.questionCount
            )
        }
    }

    private fun onBackClick() {
        viewModelScopeSafe.launch(Dispatchers.IO) {
            _commands.emit(CreateQuizCommand.NavigateBack)
        }
    }

    private fun incrementQuestionsCount(questionsCount: Int) {
        viewModelScopeSafe.launch(Dispatchers.Default) {
            _screenState.update { currentState ->
                if (currentState is CreateQuizState.Loaded) {
                    val incrementedCount = questionsCount + 1
                    val newCount = incrementedCount.coerceAtMost(MAX_QUESTIONS_COUNT)

                    currentState.copy(questionsCount = newCount)
                } else {
                    currentState
                }
            }
        }
    }

    private fun decrementQuestionsCount(questionsCount: Int) {
        viewModelScopeSafe.launch(Dispatchers.Default) {
            _screenState.update { currentState ->
                if (currentState is CreateQuizState.Loaded) {
                    val incrementedCount = questionsCount - 1
                    val newCount = incrementedCount.coerceAtLeast(MIN_QUESTIONS_COUNT)

                    currentState.copy(questionsCount = newCount)
                } else {
                    currentState
                }
            }
        }
    }

    private fun changeChosenSpecialization(newSpecializationId: Long) {
        viewModelScopeSafe.launch(Dispatchers.Default) {
            _screenState.update { currentState ->
                if (currentState is CreateQuizState.Loaded) {
                    currentState.copy(selectedSpecializationId = newSpecializationId)
                } else {
                    currentState
                }
            }
        }
    }

    private fun onStartInterviewClick(specializationId: Long, questionCount: Int) {
        viewModelScopeSafe.launch(Dispatchers.Default) {
            _commands.emit(
                CreateQuizCommand.NavigateToInterviewQuizScreen(
                    specializationId = specializationId,
                    questionCount = questionCount
                )
            )
        }
    }

    companion object {
        private const val MIN_QUESTIONS_COUNT = 1
        private const val MAX_QUESTIONS_COUNT = 8
    }
}
