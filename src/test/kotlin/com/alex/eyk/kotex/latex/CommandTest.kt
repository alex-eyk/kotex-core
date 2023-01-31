package com.alex.eyk.kotex.latex

import com.alex.eyk.kotex.util.plus
import org.junit.jupiter.api.Test

class CommandTest {

    @Test
    fun verticalSpaceTest() = assertLaTeX(
        expected = "\\vspace[*]{14.5pt}" + System.lineSeparator() +
                "\\vspace{13px}" + System.lineSeparator()
    ) {
        VerticalSpace(
            length = 14.5F,
            removeSpaceOnPageEnd = false
        )
        VerticalSpace(
            length = 13F,
            dimension = Dimension.PX
        )
    }

    @Test
    fun paragraphIndentTest() = assertLaTeX(
        expected = "\\setlength{\\parindent}{14pt}" + System.lineSeparator()
    ) {
        SetParagraphIndent(length = 14F)
    }

    @Test
    fun noIndentTest() = assertLaTeX(
        expected = "\\noindent"
    ) {
        NoIndent()
    }

    @Test
    fun indentTest() = assertLaTeX(
        expected = "\\indent"
    ) {
        Indent()
    }

    @Test
    fun centeringTest() = assertLaTeX(
        expected = "\\centering" + System.lineSeparator()
    ) {
        Centering()
    }

    @Test
    fun raggedLeftTest() = assertLaTeX(
        expected = "\\raggedleft" + System.lineSeparator()
    ) {
        RaggedLeft()
    }

    @Test
    fun raggedRightTest() = assertLaTeX(
        expected = "\\raggedright" + System.lineSeparator()
    ) {
        RaggedRight()
    }

    @Test
    fun groupTest() = assertLaTeX(
        expected = "\\begingroup" + System.lineSeparator() +
                "Group content" + System.lineSeparator() +
                "\\endgroup" + System.lineSeparator(),
    ) {
        Group {
            Content(raw = "Group content") + Br()
        }
    }

    @Test
    fun beginGroupTest() = assertLaTeX(
        expected = "\\begingroup" + System.lineSeparator()
    ) {
        BeginGroup()
    }

    @Test
    fun endGroupTest() = assertLaTeX(
        expected = "\\endgroup" + System.lineSeparator()
    ) {
        EndGroup()
    }
}
