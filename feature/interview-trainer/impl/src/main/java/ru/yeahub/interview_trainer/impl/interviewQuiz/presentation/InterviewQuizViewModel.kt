package ru.yeahub.interview_trainer.impl.interviewQuiz.presentation

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.yeahub.core_utils.BaseViewModel
import ru.yeahub.interview_trainer.impl.interviewQuiz.presentation.InterviewQuizState.Loaded.QuizAnswer
import ru.yeahub.interview_trainer.impl.interviewQuiz.presentation.InterviewQuizState.Loaded.VoQuestion

open class InterviewQuizViewModel(
    private val screenMapper: InterviewQuizScreenMapper
) : BaseViewModel() {

    // Вопросы для превью. Временно
    private val previewQuestions by lazy {
        previewQuestions()
    }

    private val userInputState = MutableStateFlow(
        UserInput(
            isAnswerVisible = false,
            answers = persistentMapOf(),
            selectedAnswer = QuizAnswer.NONE
        )
    )

    val screenState = userInputState
        .map { userInput ->
            screenMapper.getScreenState(
                questions = previewQuestions,
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

    /** Создание списка вопросов для тестирования превью */
    @Suppress("MagicNumber")
    private fun previewQuestions(): PersistentList<VoQuestion> {
        val shortAnswer = "Виртуальный DOM (VDOM) — это легковесное " +
            "представление реального DOM в памяти, которое используется в " +
            "JavaScript-библиотеках, таких как React и Vue, " +
            "для повышения производительности веб-приложений."

        val base = VoQuestion(
            id = 0,
            title = "Что такое Virtual DOM, и как он работает?",
            shortAnswer = shortAnswer
        )
        val questions = mutableListOf<VoQuestion>()
        repeat(10) { index ->
            questions.add(base.copy(id = index.toLong()))
        }

        return questions.toPersistentList()
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
