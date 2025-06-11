package ru.yeahub

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseViewModel : ViewModel() {

    protected val defaultCEH: CoroutineExceptionHandler
        get() = CoroutineExceptionHandler { _, throwable ->
            onDefaultCEH(throwable)
        }

    protected open fun onDefaultCEH(throwable: Throwable) {
        val tag = this.javaClass.simpleName
        Log.e(tag, "Unhandled exception: ${throwable.message}", throwable)
    }

    protected fun runCoroutine(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Unit,
    ) = viewModelScope.launch(context) { block() }
}