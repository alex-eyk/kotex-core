package com.alex.eyk.kotex.latex

import org.junit.jupiter.api.Test

class EnvironmentTest {

    @Test
    fun quoteTest() = assertLaTeX(
        expected = """
            \begin{quote}
            Quote
            \end{quote}
            
        """.trimIndent()
    ) {
        Quote {
            "Quote".asTextln()
        }
    }

    @Test
    fun flushRightTest() = assertLaTeX(
        expected = """
            \begin{flushright}
            Text
            \end{flushright}
            
        """.trimIndent()
    ) {
        FlushRight {
            "Text".asTextln()
        }
    }

    @Test
    fun flushLeftTest() = assertLaTeX(
        expected = """
            \begin{flushleft}
            Text
            \end{flushleft}
            
        """.trimIndent()
    ) {
        FlushLeft {
            "Text".asTextln()
        }
    }

    @LaTeX
    fun centerTest() = assertLaTeX(
        expected = """
            \begin{center}
            Center text
            \end{center}
            
        """.trimIndent()
    ) {
        Center {
            "Center text".asTextln()
        }
    }

    @Test
    fun environmentTest() = assertLaTeX(
        expected = """
            \begin{env}
            Text text
            \end{env}
            
        """.trimIndent()
    ) {
        Environment(name = "env") {
            "Text text".asTextln()
        }
    }

    @Test
    fun angleBraceWrappedTest() = assertLaTeX(
        expected = "[test]"
    ) {
        AngleBraceWrapped {
            "test".asText()
        }
    }

    @Test
    fun braceWrappedTest() = assertLaTeX(
        expected = "{test}"
    ) {
        BraceWrapped {
            "test".asText()
        }
    }

    @Test
    fun wrappedWithTest() = assertLaTeX(
        expected = """$1x + 2$"""
    ) {
        Wrapped(
            with = "$"
        ) {
            "1x + 2".asText()
        }
    }

    @Test
    fun wrappedTest() = assertLaTeX(
        expected = "{test}"
    ) {
        Wrapped(
            start = "{",
            end = "}"
        ) {
            "test".asText()
        }
    }

    @Test
    fun environmentBeginTest() = assertLaTeX(
        expected = """\begin{env}""" + System.lineSeparator()
    ) {
        EnvironmentBegin(name = "env")
    }

    @Test
    fun environmentEndTest() = assertLaTeX(
        expected = """\end{env}""" + System.lineSeparator()
    ) {
        EnvironmentEnd(name = "env")
    }
}
