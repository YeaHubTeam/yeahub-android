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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
            isAnswerVisible = false,
            answers = persistentMapOf(),
            selectedAnswer = QuizAnswer.NONE
        )
    )

    val screenState = userInputState.map { userInput ->
        val response = questionsDeferred.await()

        screenMapper.getScreenState(
            domainQuestions = response.questions,
            questionIndex = FIRST_QUESTION_INDEX,
            isAnswerVisible = userInput.isAnswerVisible,
            answers = userInput.answers,
            selectedAnswer = userInput.selectedAnswer
        )
    }.stateIn(
        scope = viewModelScopeSafe,
        started = SharingStarted.WhileSubscribed(TIME_TO_CLEAN_UP_RESOURCES),
        initialValue = InterviewQuizState.Loading
    )

    private val _commands = MutableSharedFlow<InterviewQuizCommand>()
    val commands = _commands.asSharedFlow()

    fun onEvent(event: InterviewQuizEvent) {
        when (event) {
            InterviewQuizEvent.ToDo -> { /* TODO */ }
        }
    }

    private data class UserInput(
        val isAnswerVisible: Boolean,
        val answers: PersistentMap<Long, QuizAnswer>,
        val selectedAnswer: QuizAnswer
    )

    companion object {

        private const val TIME_TO_CLEAN_UP_RESOURCES = 5000L

        private const val FIRST_QUESTION_INDEX = 0
    }
}
