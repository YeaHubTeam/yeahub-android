package ru.yeahub.feature_toggle_api

@JvmInline
value class FeatureKey(
    val value: String
)

/**
 * Реестр feature-toggle ключей
 *
 * Как добавить новую фичу:
 * 1. Объявъляем новый FeatureKey
 * 2. Добавляем его в all
 *
 * По умолчанию:
 * - backend key берётся из FeatureKey.value
 * - значение bootstrap fallback (если не пришел ключ с бэка) = `false`
 *
 * Если нужен по умолчанию true bootstrap fallback, добавьте его в `FeatureValueResolver`.
 *
 * Пример:
 * val InterviewTrainer = FeatureKey(value = "interview_trainer")
 *
 * val all: Set<FeatureKey> = setOf(
 *     InterviewTrainer
 * )
 */

object FeatureKeys {
    // val InterviewTrainer = FeatureKey(value = "interview_trainer")
    val all: Set<FeatureKey> = setOf(
        //InterviewTrainer
    )
}
