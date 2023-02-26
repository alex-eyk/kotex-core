@file:Suppress("REDUNDANT_INLINE_SUSPEND_FUNCTION_TYPE")

package com.alex.eyk.kotex.latex.math

import com.alex.eyk.kotex.latex.Environment
import com.alex.eyk.kotex.latex.LaTeX
import com.alex.eyk.kotex.latex.LineBreak
import com.alex.eyk.kotex.latex.Wrapped
import com.alex.eyk.kotex.latex.asContent
import com.alex.eyk.kotex.latex.asText

/**
 * Environment [Equation] does the same as [Expression], but the math
 * expression will be numbered.
 *
 * @param content Mathematical expression.
 */
@LaTeX
suspend inline fun Equation(
    content: @LaTeX suspend () -> Unit
) {
    Environment(
        name = "equation",
        content = content
    )
}

@LaTeX
suspend inline fun Math(
    content: () -> MathContent
) {
    Wrapped(
        start = """\[""",
        end = """\]"""
    ) {
        content().asContent()
    }
}

@LaTeX
suspend fun String.asExpr() {
    Expression { this.asText() }
}

/**
 * Mathematical expression that is separate from a paragraph.
 *
 * @param content Mathematical expression.
 */
@LaTeX
suspend inline fun Expression(
    content: @LaTeX suspend () -> Unit
) {
    Wrapped(
        start = """\[""",
        end = """\]""",
        content = content
    )
    LineBreak()
}

/**
 * An alternative, easier way to specify an inline mathematical expression.
 * Instead of calling suspend functions that return nothing, you need to
 * collect the [MathContent] object using the appropriate functions. Some of
 * them are infix.
 *
 * For example, an inline expression that would look like this:
 * `$\sum_{i = 0}^{n}{x_{i}} > 0$` can be written like this:
 * ```
 *  InlineMath {
 *      sum(`for` = "i = 0", border = "n") { "x" `_` "i" } greater 0
 *  }
 * ```
 *
 * @see MathContent
 * @param content Mathematical expression.
 */
@LaTeX
suspend inline fun InlineMath(
    content: () -> MathContent
) {
    Wrapped(with = "$") {
        content().asContent()
    }
}

/**
 * Extension for a string that allows it to be converted to an [LaTeX]
 * internal expression.
 */
@LaTeX
suspend fun String.asInlineExpr() {
    InlineExpression { this.asText() }
}

/**
 * A mathematical expression within a paragraph.
 *
 * @param content Mathematical expression.
 */
@LaTeX
suspend inline fun InlineExpression(
    content: @LaTeX suspend () -> Unit
) {
    Wrapped(
        with = "$",
        content = content
    )
}
