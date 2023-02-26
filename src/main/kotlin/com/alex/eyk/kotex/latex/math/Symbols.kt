package com.alex.eyk.kotex.latex.math

import com.alex.eyk.kotex.latex.Command
import com.alex.eyk.kotex.latex.LaTeX

@LaTeX
suspend fun Epsilon(
    upper: Boolean = false
) {
    GreekLetter(name = "epsilon", upper)
}

@LaTeX
suspend inline fun Delta(
    upper: Boolean = false
) {
    GreekLetter(name = "delta", upper)
}

@LaTeX
suspend inline fun Gamma(
    upper: Boolean = false
) {
    GreekLetter(name = "gamma", upper)
}

@LaTeX
suspend inline fun Beta(
    upper: Boolean = false
) {
    GreekLetter(name = "beta", upper)
}

@LaTeX
suspend inline fun Alpha(
    upper: Boolean = false
) {
    GreekLetter(name = "alpha", upper)
}

@LaTeX
suspend fun GreekLetter(
    name: String,
    upper: Boolean = false
) {
    if (upper) {
        val firstUpper = name.first().uppercase()
        Command(name = firstUpper + name.substring(1))
    } else {
        Command(name)
    }
}
