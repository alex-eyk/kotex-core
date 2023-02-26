package com.alex.eyk.kotex.latex.table

import com.alex.eyk.kotex.ext.listOf
import com.alex.eyk.kotex.latex.asContent
import com.alex.eyk.kotex.latex.assertLaTeX
import org.junit.jupiter.api.Test

class TableBuilderTest {

    companion object {

        private val SIMPLE_SUM_TABLE = """
            \begin{tabular}{|c|c|c|}
            \hline
            0 & 1 & 2 \\
            \hline
            1 & 2 & 3 \\
            \hline
            2 & 3 & 4 \\
            \hline
            \end{tabular}
            
        """.trimIndent()
    }

    @Test
    fun expandLeftTest() = assertLaTeX(
        expected = SIMPLE_SUM_TABLE
    ) {
        TableBuilder()
            .content(
                height = 3,
                width = 1
            ) { i, j ->
                (i + j + 2).asContent()
            }
            .expandLeft { i ->
                (i + 1).asContent()
            }
            .expandLeft { i ->
                i.asContent()
            }
            .build()
            .asTable()
    }

    @Test
    fun expandRightTest() = assertLaTeX(
        expected = SIMPLE_SUM_TABLE
    ) {
        TableBuilder()
            .content(
                height = 3,
                width = 1
            ) { i, j ->
                (i + j).asContent()
            }
            .expandRight { i ->
                (i + 1).asContent()
            }
            .expandRight { i ->
                (i + 2).asContent()
            }
            .build()
            .asTable()
    }

    @Test
    fun expandBottomTest() = assertLaTeX(
        expected = SIMPLE_SUM_TABLE
    ) {
        TableBuilder()
            .content(
                height = 1,
                width = 3
            ) { i, j ->
                (i + j).asContent()
            }
            .expandBottom { j ->
                (j + 1).asContent()
            }
            .expandBottom { j ->
                (j + 2).asContent()
            }
            .build()
            .asTable()
    }

    @Test
    fun expandTopTwiceTest() = assertLaTeX(
        expected = SIMPLE_SUM_TABLE
    ) {
        TableBuilder()
            .content(
                height = 1,
                width = 3
            ) { i, j ->
                (i + j + 2).asContent()
            }
            .expandTop { j ->
                (j + 1).asContent()
            }
            .expandTop { j ->
                j.asContent()
            }
            .build()
            .asTable()
    }

    @Test
    fun expandTopTest() = assertLaTeX(
        expected = SIMPLE_SUM_TABLE
    ) {
        TableBuilder()
            .content(
                height = 2,
                width = 3
            ) { i, j ->
                (i + j + 1).asContent()
            }
            .expandTop { j ->
                j.asContent()
            }
            .build()
            .asTable()
    }

    @Test
    fun contentTest() = assertLaTeX(
        expected = SIMPLE_SUM_TABLE
    ) {
        TableBuilder()
            .content(
                height = 3,
                width = 3
            ) { i, j ->
                (i + j).asContent()
            }
            .build()
            .asTable()
    }

    @Test
    fun mappedContentTest() = assertLaTeX(
        expected = SIMPLE_SUM_TABLE
    ) {
        TableBuilder()
            .content(
                content = listOf(size = 3) { i ->
                    listOf(size = 3) { j -> i + j }
                },
                map = { item, _, _ ->
                    item.asContent()
                }
            )
            .build()
            .asTable()
    }
}
