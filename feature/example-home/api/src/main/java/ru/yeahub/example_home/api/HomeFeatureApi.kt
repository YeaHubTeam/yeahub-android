package ru.yeahub.example_home.api

import ru.yeahub.navigation_api.FeatureApi

interface HomeFeatureApi : FeatureApi {
    val homeRoute: String
}