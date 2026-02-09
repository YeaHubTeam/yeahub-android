package ru.yeahub.core_utils.common

import android.content.Context

sealed class TextOrResource {
    data class Text(val text: String) : TextOrResource()
    data class Resource(val resource: Int) : TextOrResource()

    fun getString(context: Context) = when (this) {
        is Resource -> context.getString(resource)
        is Text -> text
    }
}