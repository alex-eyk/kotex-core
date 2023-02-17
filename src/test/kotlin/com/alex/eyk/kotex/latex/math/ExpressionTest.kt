package com.alex.eyk.kotex.latex.math

import com.alex.eyk.kotex.latex.Content
import com.alex.eyk.kotex.latex.assertLaTeX
import org.junit.jupiter.api.Test

class ExpressionTest {

    @Test
    fun inlineMathTest() = assertLaTeX(
        expected = """$\sum_{i = 0}^{n}{x_{i}} > 0$"""
    ) {
        InlineMath { sum(`for` = "i = 0", border = "n") { "x" `_` "i" } greater 0 }
    }

    @Test
    fun asInlineExprTest() = assertLaTeX(
        expected = """$8x - 3$"""
    ) {
        "8x - 3".asInlineExpr()
    }

    @Test
    fun inlineExpressionTest() = assertLaTeX(
        expected = """$3x + 4$"""
    ) {
        InlineExpression {
            Content("3x + 4")
        }
    }
}
