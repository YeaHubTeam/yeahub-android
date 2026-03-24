import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.PersistentList

@Immutable
sealed interface InterviewQuizResultState {
    data object Loading : InterviewQuizResultState

    data class Loaded(
        val overallPercentage: Float,
        val totalQuestions: Int,
        val newQuestions: Int,
        val inProgress: Int,
        val studied: Int,
        val skills: PersistentList<VoSkillStat>,
        val questions: PersistentList<VoQuestionResult>
    ) : InterviewQuizResultState {
        @Immutable
        data class VoSkillStat(
            val name: String,
            val current: Int,
            val max: Int
        )

        @Immutable
        data class VoQuestionResult(
            val questionText: String,
            val isCorrect: Boolean
        )
    }

    data class Error(val throwable: Throwable) : InterviewQuizResultState
}