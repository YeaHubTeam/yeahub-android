package ru.yeahub

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {

    protected val ViewModel.viewModelScopeSafe: CoroutineScope
        get() = viewModelScope.plus(defaultCEH)

    private val defaultCEH: CoroutineExceptionHandler
        get() = CoroutineExceptionHandler { _, throwable ->
            onDefaultCEH(throwable)
        }

    protected open fun onDefaultCEH(throwable: Throwable) {
        Timber.e(throwable, "Unhandled exception in ${this.javaClass.simpleName}")
    }
}