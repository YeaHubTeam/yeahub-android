package ru.yeahub.example_questions.impl.presentation.model

sealed class QuestionMainItemType {
    object BaseQuestions : QuestionMainItemType()
    object Collections : QuestionMainItemType()
}
