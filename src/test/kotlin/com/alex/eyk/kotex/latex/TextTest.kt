package com.alex.eyk.kotex.latex

import com.alex.eyk.kotex.util.plus
import org.junit.jupiter.api.Test

class TextTest {

    @Test
    fun parTest() = assertLaTeX(
        expected = """\par""" + System.lineSeparator()
    ) {
        Par()
    }

    @Test
    fun paragraphTest() = assertLaTeX(
        expected = """\par""" + System.lineSeparator()
    ) {
        Paragraph()
    }

    @Test
    fun spaceWithSizeTest() = assertLaTeX(
        expected = """\  \quad \qquad \, \: \; """
    ) {
        listOf(
            SpaceSize.AS_SPACE,
            SpaceSize.QUAD,
            SpaceSize.QUAD_TWICE,
            SpaceSize.MINIMAL,
            SpaceSize.SMALL,
            SpaceSize.MEDIUM
        ).forEach {
            Space(size = it) + Space()
        }
    }

    @Test
    fun spcTest() = assertLaTeX(
        expected = " "
    ) {
        ` `()
    }

    @Test
    fun spaceTest() = assertLaTeX(
        expected = " "
    ) {
        Space()
    }

    @Test
    fun boldTest() = assertLaTeX(
        expected = """\textbf{Text}"""
    ) {
        Bold { "Text".asText() }
    }

    @Test
    fun italicTest() = assertLaTeX(
        expected = """\textit{Text}"""
    ) {
        Italic { "Text".asText() }
    }

    @Test
    fun underlineTest() = assertLaTeX(
        expected = """\underline{Text}"""
    ) {
        Underline { "Text".asText() }
    }

    @Test
    fun emphTest() = assertLaTeX(
        expected = """\emph{Text}"""
    ) {
        Emph { "Text".asText() }
    }

    @Test
    fun textlnLambdaWithModifierTest() = assertLaTeX(
        expected = """\textbf{Bold text}""" + System.lineSeparator()
    ) {
        Textln(Modifier.BOLD) {
            "Bold text"
        }
    }

    @Test
    fun textlnWithModifierTest() = assertLaTeX(
        expected = """\textbf{Bold text}""" + System.lineSeparator()
    ) {
        Textln(
            modifier = Modifier.BOLD,
            text = "Bold text"
        )
    }

    @Test
    fun textLambdaWithModifierTest() = assertLaTeX(
        expected = """\textbf{Text}"""
    ) {
        Text(Modifier.BOLD) {
            "Text"
        }
    }

    @Test
    fun textWithModifierTest() = assertLaTeX(
        expected = """\textbf{Text}"""
    ) {
        Text(
            modifier = Modifier.BOLD,
            text = "Text"
        )
    }

    @Test
    fun modifiedNormalTest() = assertLaTeX(
        expected = """\textnormal{Normal text}"""
    ) {
        Modified(Modifier.NORMAL) {
            "Normal text".asText()
        }
    }

    @Test
    fun modifiedSmallCapitalTest() = assertLaTeX(
        expected = """\textsc{Small capital text}"""
    ) {
        Modified(Modifier.SMALL_CAPITAL) {
            "Small capital text".asText()
        }
    }

    @Test
    fun modifiedSlantedTest() = assertLaTeX(
        expected = """\textsl{Slanted text}"""
    ) {
        Modified(Modifier.SLANTED) {
            "Slanted text".asText()
        }
    }

    @Test
    fun modifiedUprightTest() = assertLaTeX(
        expected = """\textup{Upright text}"""
    ) {
        Modified(Modifier.UPRIGHT) {
            "Upright text".asText()
        }
    }

    @Test
    fun modifiedMediumTest() = assertLaTeX(
        expected = """\textmd{Medium text}"""
    ) {
        Modified(Modifier.MEDIUM) {
            "Medium text".asText()
        }
    }

    @Test
    fun modifiedTypewriterTest() = assertLaTeX(
        expected = """\texttt{Typewriter text}"""
    ) {
        Modified(Modifier.TYPEWRITER) {
            "Typewriter text".asText()
        }
    }

    @Test
    fun modifiedSansSerifTest() = assertLaTeX(
        expected = """\textsf{Sans-serif text}"""
    ) {
        Modified(Modifier.SANS_SERIF) {
            "Sans-serif text".asText()
        }
    }

    @Test
    fun modifiedRomanTest() = assertLaTeX(
        expected = """\textrm{Roman text}"""
    ) {
        Modified(Modifier.ROMAN) {
            "Roman text".asText()
        }
    }

    @Test
    fun modifiedUnderlineTest() = assertLaTeX(
        expected = """\underline{Underline text}"""
    ) {
        Modified(Modifier.UNDERLINE) {
            "Underline text".asText()
        }
    }

    @Test
    fun modifiedEmphTest() = assertLaTeX(
        expected = """\emph{Emph text}"""
    ) {
        Modified(Modifier.EMPHASISING) {
            "Emph text".asText()
        }
    }

    @Test
    fun modifiedItalicTest() = assertLaTeX(
        expected = """\textit{Italic text}"""
    ) {
        Modified(Modifier.ITALIC) {
            "Italic text".asText()
        }
    }

    @Test
    fun modifiedWithInnerModifierTest() = assertLaTeX(
        expected = """\textbf{Text and \textit{text}}"""
    ) {
        Modified(Modifier.BOLD) {
            "Text and ".asText()
            Modified(Modifier.ITALIC) {
                "text".asText()
            }
        }
    }

    @Test
    fun modifiedEmptyTest() = assertLaTeX(
        expected = """\textbf{}"""
    ) {
        Modified(Modifier.BOLD) {
            "".asText()
        }
    }

    @Test
    fun modifiedBoldTest() = assertLaTeX(
        expected = """\textbf{Bold text}"""
    ) {
        Modified(Modifier.BOLD) {
            "Bold text".asText()
        }
    }

    @Test
    fun strAsTextlnTest() = assertLaTeX(
        expected = "String as textln" + System.lineSeparator()
    ) {
        "String as textln".asTextln()
    }

    @Test
    fun strAsTextTest() = assertLaTeX(
        expected = "String as text"
    ) {
        "String as text".asText()
    }

    @Test
    fun textlnLambdaTest() = assertLaTeX(
        expected = "Test test text" + System.lineSeparator()
    ) {
        Textln {
            "Test test text"
        }
    }

    @Test
    fun textlnTest() = assertLaTeX(
        expected = "Test test" + System.lineSeparator()
    ) {
        Textln("Test test")
    }

    @Test
    fun textLambdaTest() = assertLaTeX(
        expected = "Another test text"
    ) {
        Text {
            "Another test text"
        }
    }

    @Test
    fun textTest() = assertLaTeX(
        expected = "Test text"
    ) {
        Text("Test text")
    }

    @Test
    fun brTest() = assertLaTeX(
        expected = System.lineSeparator()
    ) {
        Br()
    }

    @Test
    fun lineBreakTest() = assertLaTeX(
        expected = System.lineSeparator()
    ) {
        LineBreak()
    }

    @Test
    fun emptyTest() = assertLaTeX(
        expected = ""
    ) {
        Empty()
    }
}
