package ru.yeahub

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {

    private val defaultCEH: CoroutineExceptionHandler
        get() = CoroutineExceptionHandler { _, throwable ->
            onDefaultCEH(throwable)
        }

    protected open fun onDefaultCEH(throwable: Throwable) {
        Timber.e(throwable, "Unhandled exception in ${this.javaClass.simpleName}")
    }

    fun CoroutineScope.withDefaultCEH(): CoroutineScope {
        return CoroutineScope(this.coroutineContext + defaultCEH)
    }
}