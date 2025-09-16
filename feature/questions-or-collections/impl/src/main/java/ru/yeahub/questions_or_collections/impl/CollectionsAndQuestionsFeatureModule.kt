package ru.yeahub.questions_or_collections.impl

import org.koin.dsl.bind
import org.koin.dsl.module
import ru.yeahub.navigation_api.FeatureApi

val collectionsAndQuestionsFeatureModule = module {
    single { CollectionsFeatureImpl() } bind FeatureApi::class
    single { QuestionsFeatureImpl() } bind FeatureApi::class
}