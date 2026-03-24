import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import ru.yeahub.interview_trainer.impl.interviewQuizResult.InterviewQuizResultEvent
import ru.yeahub.interview_trainer.impl.interviewQuizResult.InterviewQuizResultScreenMapper

class InterviewQuizResultViewModel(
    private val mapper: InterviewQuizResultScreenMapper
) : ViewModel() {

    val state: StateFlow<InterviewQuizResultState> =
        flow {
            emit(mapper.getScreenState())
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = InterviewQuizResultState.Loading
            )

    fun onEvent(event: InterviewQuizResultEvent) {
        // TODO:
    }
}