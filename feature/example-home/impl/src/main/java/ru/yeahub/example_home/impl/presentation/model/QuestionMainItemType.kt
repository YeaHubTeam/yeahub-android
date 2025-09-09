package ru.yeahub.example_home.impl.presentation.model

sealed class QuestionMainItemType {
    object BaseQuestions : QuestionMainItemType()
    object Collections : QuestionMainItemType()
}
