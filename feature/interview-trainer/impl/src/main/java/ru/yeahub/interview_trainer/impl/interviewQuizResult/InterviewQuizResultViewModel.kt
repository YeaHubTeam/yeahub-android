import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import ru.yeahub.core_utils.BaseViewModel
import ru.yeahub.interview_trainer.impl.interviewQuizResult.InterviewQuizResultEvent
import ru.yeahub.interview_trainer.impl.interviewQuizResult.InterviewQuizResultScreenMapper

private const val STOP_TIMEOUT_MILLIS = 5000L

class InterviewQuizResultViewModel(
    private val mapper: InterviewQuizResultScreenMapper
) : BaseViewModel() {

    val state: StateFlow<InterviewQuizResultState> =
        flow {
            emit(mapper.getScreenState())
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                initialValue = InterviewQuizResultState.Loading
            )

    fun onEvent(event: InterviewQuizResultEvent) {
        // TODO:
    }
}