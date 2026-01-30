package ru.yeahub.interview_trainer.impl.interviewQuiz.presentation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.yeahub.core_utils.BaseViewModel

open class InterviewQuizViewModel(
    private val screenMapper: InterviewQuizScreenMapper
) : BaseViewModel() {

    private val _screenState = MutableStateFlow<InterviewQuizState>(
        InterviewQuizState.Loading
    )

    val screenState = _screenState.stateIn(
        scope = viewModelScopeSafe,
        started = SharingStarted.WhileSubscribed(TIME_TO_CLEAN_UP_RESOURCES),
        initialValue = InterviewQuizState.Loading
    )

    private val _commands = MutableSharedFlow<InterviewQuizCommand>()
    val commands = _commands.asSharedFlow()

    init {
        viewModelScopeSafe.launch {
            delay(RESPONSE_DELAY)
            initialLoad()
        }
    }

    fun onEvent(event: InterviewQuizEvent) {
        when (event) {
            InterviewQuizEvent.OnBackClick -> onBackClick()
            InterviewQuizEvent.OnKnownAnswerClick -> onKnownAnswerClick()
            InterviewQuizEvent.OnUnknownAnswerClick -> onUnknownAnswerClick()
            InterviewQuizEvent.OnShowResultClick -> onShowResultClick()
            InterviewQuizEvent.OnFavoriteQuestionClick -> { /* TODO: нет профиля */ }
            InterviewQuizEvent.OnNextQuestionClick -> onNextQuestionClick()
            InterviewQuizEvent.OnPreviousQuestionClick -> onPreviousQuestionClick()
            InterviewQuizEvent.OnShowHideAnswerClick -> onShowHideAnswerClick()
        }
    }

    private fun onShowHideAnswerClick() {
        viewModelScopeSafe.launch {
            _screenState.updateLoaded { loaded ->
                loaded.copy(isAnswerVisible = !loaded.isAnswerVisible)
            }
        }
    }

    private fun onPreviousQuestionClick() {
        viewModelScopeSafe.launch {
            _screenState.updateLoaded { loaded ->
                if (loaded.canGoPrevious) {
                    loaded.copy(
                        currentQuestionIndex = loaded.currentQuestionIndex - 1,
                        isAnswerVisible = false
                    )
                } else {
                    loaded
                }
            }
        }
    }

    private fun onNextQuestionClick() {
        viewModelScopeSafe.launch {
            _screenState.updateLoaded { loaded ->
                if (loaded.canGoNext) {
                    loaded.copy(
                        currentQuestionIndex = loaded.currentQuestionIndex + 1,
                        isAnswerVisible = false
                    )
                } else {
                    loaded
                }
            }
        }
    }

    /** Пока нет domain/data - загрузка Мок данных */
    protected open suspend fun initialLoad() {
        val questions = previewQuestions()
        _screenState.value = InterviewQuizState.Loaded(
            questions = questions,
            questionsCount = questions.count(),
            currentQuestionIndex = 0,
            isAnswerVisible = false,
        )
    }

    private fun onBackClick() {
        viewModelScopeSafe.launch {
            _commands.emit(InterviewQuizCommand.NavigateBack)
        }
    }

    private fun onKnownAnswerClick() {
        viewModelScopeSafe.launch {
            _screenState.updateLoaded { loaded ->
                val currentQuestion = loaded.questions.getOrNull(loaded.currentQuestionIndex)
                    ?: return@updateLoaded loaded
                loaded.copy(
                    answers = loaded.answers
                            + (currentQuestion.id to InterviewQuizState.Loaded.QuizAnswer.KNOWN)
                )
            }
        }
    }

    private fun onUnknownAnswerClick() {
        viewModelScopeSafe.launch {
            _screenState.updateLoaded { loaded ->
                val currentQuestion = loaded.questions.getOrNull(loaded.currentQuestionIndex)
                    ?: return@updateLoaded loaded
                loaded.copy(
                    answers = loaded.answers
                            + (currentQuestion.id to InterviewQuizState.Loaded.QuizAnswer.UNKNOWN)
                )
            }
        }
    }

    private fun onShowResultClick() {
        viewModelScopeSafe.launch(Dispatchers.IO) {
            _commands.emit(
                InterviewQuizCommand.NavigateToInterviewQuizResultScreen
            )
        }
    }

    /** Метод-хелпер для обновления состояния Loaded */
    private inline fun MutableStateFlow<InterviewQuizState>.updateLoaded(
        transform: (InterviewQuizState.Loaded) -> InterviewQuizState.Loaded
    ) {
        update { state ->
            val loaded = state as? InterviewQuizState.Loaded ?: return@update state
            transform(loaded)
        }
    }

    /** Создание списка вопросов для тестирования превью */
    private fun previewQuestions(): List<InterviewQuizState.Loaded.VoQuestion> {
        val base1 = InterviewQuizState.Loaded.VoQuestion(0, "Что такое Virtual DOM, и как он работает?", "Виртуальный DOM (VDOM) — это легковесное представление реального DOM в памяти, которое используется в JavaScript-библиотеках, таких как React и Vue, для повышения производительности веб-приложений.")
        val base2 = InterviewQuizState.Loaded.VoQuestion(0, "Пример вопроса, на который пользователь должен ответить?", "Пример ответа, который пользователю должен высветиться, когда будет нажата кнопка Показать ответ, и скрыться, когда будет нажата кнопка Скрыть ответПример ответа, который пользователю должен высветиться, когда будет нажата кнопка Показать ответ, и скрыться, когда будет нажата кнопка Скрыть ответПример ответа, который пользователю должен высветиться, когда будет нажата кнопка Показать ответ, и скрыться, когда будет нажата кнопка Скрыть ответПример ответа, который пользователю должен высветиться, когда будет нажата кнопка Показать ответ, и скрыться, когда будет нажата кнопка Скрыть ответ")
        val questions = mutableListOf<InterviewQuizState.Loaded.VoQuestion>()
        repeat(10) { index ->
            if (index % 2 == 0) {
                questions.add(base1.copy(id = index.toLong()))
            } else {
                questions.add(base2.copy(id = index.toLong()))
            }
        }
        return questions
    }

    companion object {

        private const val RESPONSE_DELAY = 2500L
        private const val TIME_TO_CLEAN_UP_RESOURCES = 5000L
    }
}
