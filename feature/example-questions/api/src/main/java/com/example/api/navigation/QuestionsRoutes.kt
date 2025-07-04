package com.example.api.navigation

import ru.yeahub.navigation_api.FeatureRoute

object QuestionsRoutes : FeatureRoute {
    private const val BASE_ROUTE = "questions"
    override val baseRoute: String = BASE_ROUTE

    const val QUESTIONS = BASE_ROUTE

    fun questionsRoute() = BASE_ROUTE
}