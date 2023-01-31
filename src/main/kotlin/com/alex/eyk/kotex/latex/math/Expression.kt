package com.alex.eyk.kotex.latex.math

import com.alex.eyk.kotex.latex.LaTeX
import com.alex.eyk.kotex.latex.LineBreak
import com.alex.eyk.kotex.latex.Wrapped

@LaTeX
suspend inline fun Expression(
    content: @LaTeX () -> Unit
) {
    Wrapped(
        start = "\\[",
        end = "\\]",
        content = content
    )
    LineBreak()
}

@LaTeX
suspend inline fun InlineExpression(
    content: @LaTeX () -> Unit
) {
    Wrapped(
        with = "$",
        content = content
    )
}
