package ru.yeahub.example_profile.api

import ru.yeahub.navigation_api.FeatureApi

interface ProfileFeatureApi : FeatureApi {
    val profileRoute: String
}