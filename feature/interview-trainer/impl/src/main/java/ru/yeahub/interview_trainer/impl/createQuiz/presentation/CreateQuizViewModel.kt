package ru.yeahub.interview_trainer.impl.createQuiz.presentation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.yeahub.core_utils.BaseViewModel
import ru.yeahub.interview_trainer.impl.createQuiz.ui.specializations

open class CreateQuizViewModel(
    private val screenMapper: CreateQuizScreenMapper,
) : BaseViewModel() {

    private val userInputState = MutableStateFlow(
        UserInput(
            selectedSpecializationId = 11,
            questionsCount = MIN_QUESTIONS_COUNT
        )
    )

    val screenState = userInputState
        .map { userInput ->
            screenMapper.getScreenState(
                specializations = specializations,
                selectedSpecializationId = userInput.selectedSpecializationId,
                questionsCount = userInput.questionsCount
            )
        }.stateIn(
            scope = viewModelScopeSafe,
            started = SharingStarted.WhileSubscribed(TIME_TO_CLEAN_UP_RESOURCES),
            initialValue = CreateQuizState.Loading
        )

    private val _commands = MutableSharedFlow<CreateQuizCommand>()
    val commands: SharedFlow<CreateQuizCommand> = _commands

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
        viewModelScopeSafe.launch {
            userInputState.update { currentInputState ->
                val incrementedCount = questionsCount + 1
                val newCount = incrementedCount.coerceAtMost(MAX_QUESTIONS_COUNT)

                currentInputState.copy(questionsCount = newCount)
            }
        }
    }

    private fun decrementQuestionsCount(questionsCount: Int) {
        viewModelScopeSafe.launch {
            userInputState.update { currentInputState ->
                val incrementedCount = questionsCount - 1
                val newCount = incrementedCount.coerceAtLeast(MIN_QUESTIONS_COUNT)

                currentInputState.copy(questionsCount = newCount)
            }
        }
    }

    private fun changeChosenSpecialization(newSpecializationId: Long) {
        viewModelScopeSafe.launch {
            userInputState.update { currentInputState ->
                currentInputState.copy(selectedSpecializationId = newSpecializationId)
            }
        }
    }

    private fun onStartInterviewClick(specializationId: Long, questionCount: Int) {
        viewModelScopeSafe.launch(Dispatchers.IO) {
            _commands.emit(
                CreateQuizCommand.NavigateToInterviewQuizScreen(
                    specializationId = specializationId,
                    questionCount = questionCount
                )
            )
        }
    }

    private data class UserInput(
        val selectedSpecializationId: Long,
        val questionsCount: Int,
    )

    companion object {
        private const val MIN_QUESTIONS_COUNT = 1
        private const val MAX_QUESTIONS_COUNT = 8
        private const val TIME_TO_CLEAN_UP_RESOURCES = 5000L
    }
}
