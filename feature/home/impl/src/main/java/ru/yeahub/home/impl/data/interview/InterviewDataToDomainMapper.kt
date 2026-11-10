package ru.yeahub.home.impl.data.interview

import ru.yeahub.home.impl.domain.interview.InterviewData
import ru.yeahub.network_api.models.GetQuestionsForLearnResponse

/**
 * Маппер ответа /interview-preparation/learn/{profileId} в доменную модель плашки тренажёра.
 *
 * currentProgress — сколько вопросов уже выучено;
 * allProgress — total по выдаче;
 * currentQuestion — заголовок первого невыученного вопроса.
 */
class InterviewDataToDomainMapper {

    fun mapToDomain(response: GetQuestionsForLearnResponse): InterviewData {
        val questions = response.data
        val learnedCount = questions.count { it.isLearned == true }
        val nextQuestion = questions.firstOrNull { it.isLearned != true }
            ?: questions.firstOrNull()
        return InterviewData(
            currentProgress = learnedCount,
            // coerceAtLeast(1) защищает деление прогресса в UI от нуля
            allProgress = response.total.toInt().coerceAtLeast(MIN_PROGRESS),
            currentQuestion = nextQuestion?.title ?: ""
        )
    }

    private companion object {
        const val MIN_PROGRESS = 1
    }
}
