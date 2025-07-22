package com.example.impl.presentation.mapper

import com.example.impl.presentation.view.DetailQuestionState
import com.example.impl.presentation.viewmodel.publicQuestion

class DetailQuestionScreenMapper {
    fun getScreenState(): DetailQuestionState {
        return DetailQuestionState.Success(publicQuestion)
    }
}