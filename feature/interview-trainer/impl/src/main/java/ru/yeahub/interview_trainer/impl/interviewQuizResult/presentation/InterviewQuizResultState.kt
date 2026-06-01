import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.PersistentList
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.interview_trainer.impl.R

@Immutable
sealed interface InterviewQuizResultState {

    val titleTopAppBar: TextOrResource

    data object Loading : InterviewQuizResultState {
        override val titleTopAppBar = TextOrResource.Resource(R.string.create_quiz_top_bar_header_text)
    }

    data class Loaded(
        override val titleTopAppBar: TextOrResource,
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

    data class Error(
        val throwable: Throwable
    ) : InterviewQuizResultState {
        override val titleTopAppBar = TextOrResource.Resource(R.string.create_quiz_top_bar_header_text)
    }
}