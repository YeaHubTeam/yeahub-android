package ru.yeahub.network_api

/**
 * Провайдер access-токена для авторизации запросов (Bearer).
 * Реализация живёт в app-модуле поверх BuildConfig/local.properties.
 */
fun interface AuthTokenProvider {

    fun token(): String
}

/**
 * Провайдер id профиля текущего пользователя для персональных эндпоинтов
 * (например, interview-preparation/learn/{profileId}).
 */
fun interface ProfileIdProvider {

    fun profileId(): String
}
