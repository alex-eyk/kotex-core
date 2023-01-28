package com.alex.eyk.kotex.latex.math

import com.alex.eyk.kotex.latex.LaTeX
import com.alex.eyk.kotex.latex.Content
import com.alex.eyk.kotex.latex.env.BraceWrapped

@LaTeX
suspend fun Frac(
    numerator: Int,
    denominator: Int,
) {
    Content(
        raw = "\\frac{$numerator}{$denominator}"
    )
}

suspend fun Frac(
    numerator: @LaTeX suspend () -> Unit,
    denominator: @LaTeX suspend () -> Unit,
) {
    Content(
        raw = "\\frac"
    )
    BraceWrapped {
        numerator()
    }
    BraceWrapped {
        denominator()
    }
}
