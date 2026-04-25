package ru.yeahub.interview_trainer.impl.createQuiz.presentation

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.interview_trainer.impl.R

@Immutable
sealed interface CreateQuizState {

    val titleTopAppBar: TextOrResource

    //Изначальный
    data object Loading : CreateQuizState {
        override val titleTopAppBar = TextOrResource.Resource(R.string.create_quiz_top_bar_header_text)
    }

    data class Loaded(
        val specializations: ImmutableList<VoSpecialization>,
        val selectedSpecializationId: Long,
        val questionsCount: Int,
    ) : CreateQuizState {
        override val titleTopAppBar = TextOrResource.Resource(R.string.create_quiz_top_bar_header_text)

        @Immutable
        data class VoSpecialization(
            val id: Long,
            val title: String,
        )
    }

    data class Error(val throwable: Throwable) : CreateQuizState {
        override val titleTopAppBar = TextOrResource.Resource(R.string.create_quiz_top_bar_header_text)
    }
}