import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.yeahub.interview_trainer.impl.interviewQuizResult.InterviewQuizResultEvent

class InterviewQuizResultViewModel : ViewModel() {

    private val _state =
        MutableStateFlow<InterviewQuizResultState>(InterviewQuizResultState.Loading)

    val state: StateFlow<InterviewQuizResultState> = _state

    fun onEvent(event: InterviewQuizResultEvent) {
        when (event) {
            InterviewQuizResultEvent.OnBackClick -> {
                // TODO
            }

            InterviewQuizResultEvent.ViewDetailedStats -> {
                // TODO
            }

            InterviewQuizResultEvent.Retry -> TODO()
            InterviewQuizResultEvent.ShareResults -> TODO()
        }
    }
}