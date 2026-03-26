package ru.yeahub.interview_trainer.impl.interviewQuiz.presentation

import androidx.lifecycle.SavedStateHandle
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.yeahub.core_utils.BaseViewModel
import ru.yeahub.interview_trainer.impl.interviewQuiz.domain.GetQuestionsListUseCase
import ru.yeahub.interview_trainer.impl.interviewQuiz.domain.QuestionsRequest
import ru.yeahub.interview_trainer.impl.interviewQuiz.presentation.InterviewQuizState.Loaded.QuizAnswer

open class InterviewQuizViewModel(
    savedStateHandle: SavedStateHandle,
    private val screenMapper: InterviewQuizScreenMapper,
    private val getQuestionsListUseCase: GetQuestionsListUseCase
) : BaseViewModel() {

    private val specializationId: Int =
        requireNotNull(savedStateHandle.get<String>("specializationId")).toInt()

    private val questionsCount: Int =
        requireNotNull(savedStateHandle.get<String>("questionsCount")).toInt()

    private val questionsDeferred = viewModelScopeSafe.async(Dispatchers.IO) {
        val request = QuestionsRequest(questionsCount, specializationId)
        getQuestionsListUseCase(request)
    }

    private val userInputState = MutableStateFlow(
        UserInput(
            questionIndex = FIRST_QUESTION_INDEX,
            isAnswerVisible = false,
            answers = persistentMapOf(),
            selectedAnswer = QuizAnswer.NONE
        )
    )

    val screenState = userInputState.map { userInput ->
        val response = questionsDeferred.await()

        screenMapper.getScreenState(
            domainQuestions = response.questions,
            questionIndex = userInput.questionIndex,
            isAnswerVisible = userInput.isAnswerVisible,
            answers = userInput.answers,
            selectedAnswer = userInput.selectedAnswer
        )
    }.catch { e ->
        screenMapper.getScreenState(e)
    }.stateIn(
        scope = viewModelScopeSafe,
        started = SharingStarted.WhileSubscribed(TIME_TO_CLEAN_UP_RESOURCES),
        initialValue = InterviewQuizState.Loading
    )

    private val _commands = MutableSharedFlow<InterviewQuizCommand>()
    val commands = _commands.asSharedFlow()

    fun onEvent(event: InterviewQuizEvent) {
        when (event) {
            InterviewQuizEvent.OnBackClick -> onBackClick()
            InterviewQuizEvent.OnKnownAnswerClick -> onKnownAnswerClick()
            InterviewQuizEvent.OnUnknownAnswerClick -> onUnknownAnswerClick()
            InterviewQuizEvent.OnShowResultClick -> onFinishInterviewClick()
            InterviewQuizEvent.OnFavoriteQuestionClick -> onFavoriteQuestionClick()
            InterviewQuizEvent.OnNextQuestionClick -> onNextQuestionClick()
            InterviewQuizEvent.OnPreviousQuestionClick -> onPreviousQuestionClick()
            InterviewQuizEvent.OnShowHideAnswerClick -> onShowHideAnswerClick()
        }
    }

    private fun onBackClick() {
        viewModelScopeSafe.launch(Dispatchers.IO) {
            _commands.emit(InterviewQuizCommand.NavigateBack)
        }
    }

    private fun onKnownAnswerClick() {
        val state = screenState.value as? InterviewQuizState.Loaded ?: return
        val currentQuestionId = state.question.id

        userInputState.update { currentInputState ->
            val newAnswer = QuizAnswer.KNOWN

            currentInputState.copy(
                selectedAnswer = newAnswer,
                answers = currentInputState.answers.put(currentQuestionId, newAnswer)
            )
        }
    }

    private fun onUnknownAnswerClick() {
        val state = screenState.value as? InterviewQuizState.Loaded ?: return
        val currentQuestionId = state.question.id

        userInputState.update { currentInputState ->
            val newAnswer = QuizAnswer.UNKNOWN

            currentInputState.copy(
                selectedAnswer = newAnswer,
                answers = currentInputState.answers.put(currentQuestionId, newAnswer)
            )
        }
    }

    private fun onFinishInterviewClick() {
        viewModelScopeSafe.launch(Dispatchers.IO) {
            val resultList = buildQuestionsWithAnswers()
            if (resultList.isEmpty()) return@launch

            _commands.emit(
                InterviewQuizCommand.NavigateToInterviewQuizResultScreen(
                    questionsWithAnswersList = resultList
                )
            )
        }
    }

    private fun onFavoriteQuestionClick() {
        // TODO: Нет авторизации
    }

    private fun onNextQuestionClick() {
        val state = screenState.value as? InterviewQuizState.Loaded ?: return

        userInputState.update { currentInputState ->
            if (!state.canGoNext) return@update currentInputState

            val newQuestionIndex = currentInputState.questionIndex + 1
            val newQuestion = state.questions[newQuestionIndex]
            val newSelectedAnswer = currentInputState.answers[newQuestion.id] ?: QuizAnswer.NONE

            currentInputState.copy(
                questionIndex = newQuestionIndex,
                isAnswerVisible = false,
                selectedAnswer = newSelectedAnswer
            )
        }
    }

    private fun onPreviousQuestionClick() {
        val state = screenState.value as? InterviewQuizState.Loaded ?: return

        userInputState.update { currentInputState ->
            if (!state.canGoPrev) return@update currentInputState

            val newQuestionIndex = currentInputState.questionIndex - 1
            val newQuestion = state.questions[newQuestionIndex]
            val newSelectedAnswer = currentInputState.answers[newQuestion.id] ?: QuizAnswer.NONE

            currentInputState.copy(
                questionIndex = newQuestionIndex,
                isAnswerVisible = false,
                selectedAnswer = newSelectedAnswer
            )
        }
    }

    private fun onShowHideAnswerClick() {
        userInputState.update { currentInputState ->
            currentInputState.copy(
                isAnswerVisible = !currentInputState.isAnswerVisible
            )
        }
    }

    private fun buildQuestionsWithAnswers(): List<VoQuestionWithAnswer> {
        val state = screenState.value as? InterviewQuizState.Loaded
            ?: return emptyList()

        return state.questions.mapNotNull { question ->
            when (state.answers[question.id]) {
                QuizAnswer.KNOWN -> {
                    VoQuestionWithAnswer(
                        id = question.id,
                        title = question.title,
                        shortAnswer = question.shortAnswer,
                        userAnswer = QuizAnswerResult.KNOWN
                    )
                }

                QuizAnswer.UNKNOWN -> {
                    VoQuestionWithAnswer(
                        id = question.id,
                        title = question.title,
                        shortAnswer = question.shortAnswer,
                        userAnswer = QuizAnswerResult.UNKNOWN
                    )
                }

                QuizAnswer.NONE, null -> null
            }
        }
    }

    private data class UserInput(
        val questionIndex: Int,
        val isAnswerVisible: Boolean,
        val answers: PersistentMap<Long, QuizAnswer>,
        val selectedAnswer: QuizAnswer
    )

    companion object {

        private const val TIME_TO_CLEAN_UP_RESOURCES = 5000L

        private const val FIRST_QUESTION_INDEX = 0
    }
}
