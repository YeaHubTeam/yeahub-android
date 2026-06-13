package ru.yeahub.datastore_api

/**
 * Результат операции локального хранения токена:
 * - Success - операция выполнена успешно
 * - Error - операция завершилась ошибкой
 */
sealed interface TokenStorageResult {

    data object Success : TokenStorageResult

    data object Error : TokenStorageResult
}