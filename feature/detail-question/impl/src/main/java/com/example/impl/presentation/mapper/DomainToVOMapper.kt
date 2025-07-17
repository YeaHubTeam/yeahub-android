package com.example.impl.presentation.mapper

import com.example.impl.presentation.view.DetailQuestionState
import com.example.impl.presentation.viewmodel.publicQuestion

object DomainToVOMapper {

    fun toVO(): DetailQuestionState.LoadedState {
        return DetailQuestionState.LoadedState(publicQuestion)
    }
}