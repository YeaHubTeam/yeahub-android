package ru.yeahub.example_questions.impl.domain

data class QuestionMainDomainModel(
    val title: String,
    val description: String,
    val images: List<Int>,
    val isLoaded: Boolean
)