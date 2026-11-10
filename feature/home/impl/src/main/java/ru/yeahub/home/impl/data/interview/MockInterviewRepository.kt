package ru.yeahub.home.impl.data.interview

import kotlinx.coroutines.delay
import ru.yeahub.home.impl.domain.interview.InterviewData
import ru.yeahub.home.impl.domain.interview.InterviewRepository
import ru.yeahub.network_api.models.AdvancedQuestionResponse
import ru.yeahub.network_api.models.GetQuestionsForLearnResponse

/**
 * Мок в структуре ответа бэкенда (GetQuestionsForLearnResponse):
 * прогоняется через тот же [InterviewDataToDomainMapper], что и сетевой репозиторий,
 * поэтому переключение мок/сеть — одна строка в DI.
 * Задержка 1–3 секунды эмулирует асинхронную загрузку.
 */
class MockInterviewRepository(
    private val mapper: InterviewDataToDomainMapper
) : InterviewRepository {

    override suspend fun getInterviewData(): InterviewData {
        delay((DELAY_MIN_MILLIS..DELAY_MAX_MILLIS).random())
        return mapper.mapToDomain(mockResponse)
    }

    private val mockResponse = GetQuestionsForLearnResponse(
        data = listOf(
            question(id = 1, title = "Чем val отличается от var в Kotlin?", isLearned = true),
            question(id = 2, title = "Что такое data class и для чего он нужен?", isLearned = true),
            question(id = 3, title = "Как работает null safety в Kotlin?", isLearned = true),
            question(id = 4, title = "Что такое suspend-функции?"),
            question(id = 5, title = "В чём разница между lazy и lateinit?"),
            question(id = 6, title = "Что такое sealed class и когда его использовать?"),
            question(id = 7, title = "Как устроены корутины и что такое CoroutineScope?"),
            question(id = 8, title = "Чем object отличается от companion object?"),
            question(id = 9, title = "Что такое extension-функции?"),
            question(id = 10, title = "В чём разница между map и flatMap?")
        ),
        page = 1,
        limit = 10,
        total = 45
    )

    private fun question(id: Long, title: String, isLearned: Boolean = false) =
        AdvancedQuestionResponse(
            id = id,
            questionId = id,
            title = title,
            description = "",
            complexity = 2L,
            isLearned = isLearned
        )

    private companion object {
        const val DELAY_MIN_MILLIS = 1_000L
        const val DELAY_MAX_MILLIS = 3_000L
    }
}
