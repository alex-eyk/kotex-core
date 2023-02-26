package com.alex.eyk.kotex.latex

import com.alex.eyk.kotex.latex.math.InlineExpression
import org.junit.jupiter.api.Test

class ListTest {

    @Test
    fun descriptionWithMapTest() = assertLaTeX(
        expected = """
            \begin{description}
            \item[item label] First item
            \item Second item
            \end{description}
            
        """.trimIndent()
    ) {
        val list = listOf(Pair("item label", "First item"), Pair("", "Second item"))
        Description(
            elements = list,
            map = {
                if (it.first.isEmpty()) {
                    Item { Text(it.second) }
                } else {
                    Item(label = it.first) { Text(it.second) }
                }
            }
        )
    }

    @Test
    fun descriptionTest() = assertLaTeX(
        expected = """
            \begin{description}
            \item[item label] First item
            \item Second item
            \end{description}
            
        """.trimIndent()
    ) {
        Description {
            Item(label = "item label") { Text("First item") }
            Item { Text("Second item") }
        }
    }

    @Test
    fun enumerateWithMapTest() = assertLaTeX(
        expected = """
            \begin{enumerate}
            \item First item
            \item Second item
            \end{enumerate}
            
        """.trimIndent()
    ) {
        val list = listOf("First item", "Second item")
        Enumerate(
            elements = list,
            map = { Item { Text(it) } }
        )
    }

    @Test
    fun enumerateTest() = assertLaTeX(
        expected = """
            \begin{enumerate}
            \item First item
            \item Second item
            \end{enumerate}
            
        """.trimIndent()
    ) {
        Enumerate {
            Item { Text("First item") }
            Item { Text("Second item") }
        }
    }

    @Test
    fun itemizeWithMapTest() = assertLaTeX(
        expected = """
            \begin{itemize}
            \item First item
            \item Second item
            \end{itemize}
            
        """.trimIndent()
    ) {
        val list = listOf("First item", "Second item")
        Itemize(
            elements = list,
            map = { Item { Text(it) } }
        )
    }

    @Test
    fun itemizeTest() = assertLaTeX(
        expected = """
            \begin{itemize}
            \item First item
            \item Second item
            \end{itemize}
            
        """.trimIndent()
    ) {
        Itemize {
            Item { Text("First item") }
            Item { Text("Second item") }
        }
    }

    @Test
    fun itemWithTextLabelTest() = assertLaTeX(
        expected = "\\item[label for item] Item text" + System.lineSeparator()
    ) {
        Item(
            label = "label for item"
        ) {
            Text("Item text")
        }
    }

    @Test
    fun itemWithLabelTest() = assertLaTeX(
        expected = "\\item[$\\blacksquare$] Item" + System.lineSeparator()
    ) {
        Item(label = {
            InlineExpression {
                Command(name = "blacksquare")
            }
        }) {
            Text("Item")
        }
    }

    @Test
    fun itemTest() = assertLaTeX(
        expected = "\\item Item text" + System.lineSeparator()
    ) {
        Item {
            Text("Item text")
        }
    }
}
