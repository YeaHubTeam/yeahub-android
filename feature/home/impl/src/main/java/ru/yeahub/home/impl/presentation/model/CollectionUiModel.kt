package ru.yeahub.home.impl.presentation.model

import androidx.annotation.DrawableRes

data class CollectionUiModel(
    @DrawableRes val imageRes: Int,
    val tags: List<String>,
    val title: String,
    val isForParticipants: Boolean,
    val questionsCount: Int,
    val additionalTags: List<String>,
)
