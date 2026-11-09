package ru.yeahub.home.impl.presentation

/** Результаты главной страницы для навигации (обрабатываются в HomeFeatureImpl). */
sealed class HomeResult {
    data object OpenStatistics : HomeResult()
}
