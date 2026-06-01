package ru.yeahub.core_utils.validation

import android.util.Patterns

object EmailValidator {

    fun isValid(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
