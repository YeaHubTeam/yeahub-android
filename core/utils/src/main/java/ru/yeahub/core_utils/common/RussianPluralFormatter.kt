package ru.yeahub.core_utils.common

import kotlin.math.abs

private const val TEENS_START = 11
private const val TEENS_END = 14
private const val LAST_TWO_DIGITS_BASE = 100
private const val LAST_DIGIT_BASE = 10
private const val ONE_ITEM_LAST_DIGIT = 1
private const val FEW_ITEMS_LAST_DIGIT_START = 2
private const val FEW_ITEMS_LAST_DIGIT_END = 4

data class RussianPluralForms(
    val one: String,
    val few: String,
    val many: String,
)

fun formatRussianCount(
    count: Int,
    forms: RussianPluralForms,
): String = "$count ${selectRussianPluralForm(count, forms)}"

private fun selectRussianPluralForm(
    count: Int,
    forms: RussianPluralForms,
): String {
    val absoluteCount = abs(count)
    val lastTwoDigits = absoluteCount % LAST_TWO_DIGITS_BASE
    val lastDigit = absoluteCount % LAST_DIGIT_BASE

    return when {
        lastTwoDigits in TEENS_START..TEENS_END -> forms.many
        lastDigit == ONE_ITEM_LAST_DIGIT -> forms.one
        lastDigit in FEW_ITEMS_LAST_DIGIT_START..FEW_ITEMS_LAST_DIGIT_END -> forms.few
        else -> forms.many
    }
}
