package ru.yeahub.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtUnaryExpression

class NoNotNullAssertionRule(config: Config = Config.Companion.empty) : Rule(config) {

    init {
        println(">>> NoNotNullAssertionRule: ПРАВИЛО ЗАГРУЖЕНО! <<<")
    }

    override val issue = Issue(
        "NoNotNullAssertionRule",
        Severity.Style,
        "Оператор !! запрещён. Используйте безопасные вызовы (.?) или элвис-оператор (?:).",
        Debt.Companion.FIVE_MINS
    )

    override fun visitUnaryExpression(expression: KtUnaryExpression) {
        val operatorText = expression.operationReference.text
        if (operatorText == "!!") {
            report(
                CodeSmell(
                    issue,
                    Entity.Companion.from(expression),
                    message = "Использование оператора !! запрещено"
                )
            )
        }
        super.visitUnaryExpression(expression)
    }
}