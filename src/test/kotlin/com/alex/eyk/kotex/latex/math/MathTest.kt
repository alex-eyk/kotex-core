package com.alex.eyk.kotex.latex.math

import com.alex.eyk.kotex.latex.asText
import com.alex.eyk.kotex.latex.assertLaTeX
import com.alex.eyk.kotex.util.plus
import org.junit.jupiter.api.Test

class MathTest {

    @Test
    fun intFracTest() = assertLaTeX(
        expected = """\frac{1}{2}"""
    ) {
        Frac(1, 2)
    }

    @Test
    fun fracTest() = assertLaTeX(
        expected = """\frac{x - 3}{2}"""
    ) {
        Frac(
            { "x - 3".asText() },
            { 2.asText() }
        )
    }

    @Test
    fun subscriptTest() = assertLaTeX(
        expected = "x_{1}"
    ) {
        "x".asText() + Subscript { 1.asText() }
    }

    @Test
    fun superscriptTest() = assertLaTeX(
        expected = "2^{3}"
    ) {
        2.asText() + Superscript { 3.asText() }
    }
}
