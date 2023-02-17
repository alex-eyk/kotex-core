@file:Suppress("REDUNDANT_INLINE_SUSPEND_FUNCTION_TYPE")

package com.alex.eyk.kotex.latex.math

import com.alex.eyk.kotex.latex.BraceWrapped
import com.alex.eyk.kotex.latex.Content
import com.alex.eyk.kotex.latex.LaTeX
import com.alex.eyk.kotex.latex.asText
import com.alex.eyk.kotex.util.plus

@LaTeX
suspend fun Frac(
    numerator: Int,
    denominator: Int
) {
    Frac(
        numerator = { numerator.asText() },
        denominator = { denominator.asText() }
    )
}

@LaTeX
suspend inline fun Frac(
    numerator: @LaTeX suspend () -> Unit,
    denominator: @LaTeX suspend () -> Unit,
) {
    Content(raw = "\\frac") + BraceWrapped(numerator) + BraceWrapped(denominator)
}

@LaTeX
suspend inline fun Subscript(
    content: @LaTeX suspend () -> Unit
) {
    Content(raw = "_") + BraceWrapped(content)
}

@LaTeX
suspend inline fun Superscript(
    content: @LaTeX suspend () -> Unit
) {
    Content(raw = "^") + BraceWrapped(content)
}
