package com.alex.eyk.kotex.latex

import com.alex.eyk.kotex.ext.listOf
import com.alex.eyk.kotex.latex.Position.BOTTOM
import com.alex.eyk.kotex.latex.Position.TOP
import com.alex.eyk.kotex.latex.table.Alignment.CENTER
import com.alex.eyk.kotex.latex.table.Alignment.LEFT
import com.alex.eyk.kotex.latex.table.Alignment.RIGHT
import com.alex.eyk.kotex.latex.table.Cline
import com.alex.eyk.kotex.latex.table.Hline
import com.alex.eyk.kotex.latex.table.Multicolumn
import com.alex.eyk.kotex.latex.table.Table
import com.alex.eyk.kotex.latex.table.TableRow
import com.alex.eyk.kotex.latex.table.Tabular
import com.alex.eyk.kotex.util.matrixOf
import com.alex.eyk.kotex.util.plus
import org.junit.jupiter.api.Test

class TableTest {

    @Test
    fun tableMapOneAlignmentTest() = assertLaTeX(
        expected = """
            \begin{tabular}{|c|c|c|}
            \hline
            2 & 3 & 4 \\
            \hline
            3 & 4 & 5 \\
            \hline
            \end{tabular}
            
        """.trimIndent()
    ) {
        val matrix = matrixOf(
            height = 2,
            width = 3
        ) { i, j ->
            (i + 1) + (j + 1)
        }
        Table(
            alignment = CENTER,
            content = matrix,
            map = { num, _, _ -> num.asContent() }
        )
    }

    @Test
    fun tableMapTest() = assertLaTeX(
        expected = """
            \begin{tabular}[b]{|c|c|l|r|}
            \hline
            1 & 2 & 3 & 4 \\
            \hline
            2 & 3 & 4 & 5 \\
            \hline
            \end{tabular}
            
        """.trimIndent()
    ) {
        val matrix = matrixOf(
            height = 2,
            width = 4
        ) { i, j ->
            i + j + 1
        }
        Table(
            alignments = listOf(
                CENTER, CENTER, LEFT, RIGHT
            ),
            content = matrix,
            position = BOTTOM,
            map = { num, _, _ -> num.asContent() }
        )
    }

    @Test
    fun tableLambdaInitOneAlignmentTest() = assertLaTeX(
        expected = """
            \begin{tabular}[t]{|l|l|l|l|}
            \hline
            3 & 2 & 1 & 0 \\
            \hline
            \end{tabular}
            
        """.trimIndent()
    ) {
        Table(
            alignment = LEFT,
            rows = 1,
            columns = 4,
            position = TOP
        ) { _, j ->
            (3 - j).asContent()
        }
    }

    @Test
    fun tableLambdaInitTest() = assertLaTeX(
        expected = """
            \begin{tabular}{|c|c|r|r|}
            \hline
            1 & 2 & 3 & 4 \\
            \hline
            2 & 3 & 4 & 5 \\
            \hline
            3 & 4 & 5 & 6 \\
            \hline
            \end{tabular}
            
        """.trimIndent()
    ) {
        Table(
            alignments = listOf(
                CENTER, CENTER, RIGHT, RIGHT
            ),
            rows = 3,
            columns = 4
        ) { i, j ->
            (i + j + 1).asContent()
        }
    }

    @Test
    fun tableOneAlignmentTest() = assertLaTeX(
        expected = """
            \begin{tabular}{|r|r|r|r|}
            \hline
            1 & 2 & 3 & 4 \\
            \hline
            \end{tabular}
            
        """.trimIndent()
    ) {
        Table(
            alignment = RIGHT,
            content = matrixOf(
                height = 1,
                width = 4
            ) { i, j -> { (i + j + 1).asContent() } }
        )
    }

    @Test
    fun tableTest() = assertLaTeX(
        expected = """
            \begin{tabular}{|c|r|r|}
            \hline
            1 & 2 & 3 \\
            \hline
            2 & 3 & 4 \\
            \hline
            \end{tabular}
            
        """.trimIndent()
    ) {
        Table(
            alignments = listOf(
                CENTER, RIGHT, RIGHT
            ),
            content = matrixOf(
                height = 2,
                width = 3,
            ) { i, j -> { (i + j + 1).asContent() } }
        )
    }

    @Test
    fun tabularTest() = assertLaTeX(
        expected = """
            \begin{tabular}{|c|l|r|}
            \end{tabular}
            
        """.trimIndent()
    ) {
        Tabular(
            alignments = listOf(
                CENTER, LEFT, RIGHT
            )
        ) { }
    }

    @Test
    fun emptyTableRowInitTest() = assertLaTeX(
        expected = ""
    ) {
        TableRow(size = 0) { it.asContent() }
    }

    @Test
    fun tableRowInitTest() = assertLaTeX(
        expected = """
            1 & 2 & 3 & 4 & 5 \\
            \hline
            
        """.trimIndent()
    ) {
        TableRow(5) { j ->
            (j + 1).asContent()
        }
    }

    @Test
    fun emptyTableRowTest() = assertLaTeX(
        expected = ""
    ) {
        TableRow(emptyList())
    }

    @Test
    fun tableRowTest() = assertLaTeX(
        expected = """
            1 & 2 & 3 & 4 \\
            \hline
            
        """.trimIndent()
    ) {
        TableRow(
            listOf(size = 4) { j -> { (j + 1).asContent() } }
        )
    }

    @Test
    fun multicolumnTest() = assertLaTeX(
        expected = """\multicolumn{2}{|l|}{Entry text}"""
    ) {
        Multicolumn(
            columns = 2,
            alignments = listOf(LEFT)
        ) {
            "Entry text".asText()
        }
    }

    @Test
    fun rowEndTest() = assertLaTeX(
        expected = """ \\""" + System.lineSeparator()
    ) {
        RowEnd()
    }

    @Test
    fun andTest() = assertLaTeX(
        expected = """3 & a"""
    ) {
        3.asContent() + `&`() + "a".asContent()
    }

    @Test
    fun nextEntryTest() = assertLaTeX(
        expected = """entry & entry"""
    ) {
        Text("entry") + NextEntry() + Text("entry")
    }

    @Test
    fun clineTest() = assertLaTeX(
        expected = """\cline{1-3}""" + System.lineSeparator()
    ) {
        Cline(from = 1, to = 3)
    }

    @Test
    fun hlineTest() = assertLaTeX(
        expected = """\hline""" + System.lineSeparator()
    ) {
        Hline()
    }
}
