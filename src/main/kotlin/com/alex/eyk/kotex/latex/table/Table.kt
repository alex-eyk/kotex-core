package com.alex.eyk.kotex.latex.table

import com.alex.eyk.kotex.ext.isNotLast
import com.alex.eyk.kotex.ext.listOf
import com.alex.eyk.kotex.ext.width
import com.alex.eyk.kotex.latex.BraceWrapped
import com.alex.eyk.kotex.latex.Command
import com.alex.eyk.kotex.latex.Content
import com.alex.eyk.kotex.latex.DeclareExternalPackage
import com.alex.eyk.kotex.latex.Environment
import com.alex.eyk.kotex.latex.LaTeX
import com.alex.eyk.kotex.latex.LineBreak
import com.alex.eyk.kotex.latex.NextEntry
import com.alex.eyk.kotex.latex.Position
import com.alex.eyk.kotex.latex.RowEnd
import com.alex.eyk.kotex.latex.table.Alignment.CENTER
import com.alex.eyk.kotex.util.Matrix
import com.alex.eyk.kotex.util.plus

enum class Alignment(
    val code: String
) {

    LEFT("l"),
    CENTER("c"),
    RIGHT("r")
}

@LaTeX
suspend fun Matrix<@LaTeX suspend () -> Unit>.asTable(
    alignment: Alignment = CENTER,
    position: Position? = null
) {
    Table(alignment, position, content = this)
}

@LaTeX
suspend fun Matrix<@LaTeX suspend () -> Unit>.asTable(
    alignments: List<Alignment>,
    position: Position? = null
) {
    Table(alignments, position, content = this)
}

@LaTeX
suspend fun <T> Table(
    alignment: Alignment = CENTER,
    content: List<List<T>>,
    position: Position? = null,
    map: @LaTeX suspend (item: T, row: Int, column: Int) -> Unit
) {
    val alignments = listOf(size = content.width) { alignment }
    Table(alignments, content, position, map)
}

@LaTeX
suspend fun <T> Table(
    alignments: List<Alignment>,
    content: List<List<T>>,
    position: Position? = null,
    map: @LaTeX suspend (item: T, row: Int, column: Int) -> Unit
) {
    if (content.isEmpty() || content[0].isEmpty()) {
        return
    }
    Tabular(alignments, position) {
        Hline()
        for (i in content.indices) {
            TableRow(content[i].size) { j ->
                map(content[i][j], i, j)
            }
        }
    }
}

@LaTeX
suspend fun Table(
    alignment: Alignment = CENTER,
    rows: Int,
    columns: Int,
    position: Position? = null,
    content: @LaTeX suspend (row: Int, col: Int) -> Unit
) {
    val alignments = listOf(size = columns) { alignment }
    Table(alignments, rows, columns, position, content)
}

@LaTeX
suspend fun Table(
    alignments: List<Alignment>,
    rows: Int,
    columns: Int,
    position: Position? = null,
    content: @LaTeX suspend (row: Int, col: Int) -> Unit
) {
    Tabular(alignments, position) {
        Hline()
        for (i in 0 until rows) {
            TableRow(size = columns) { j: Int ->
                content(i, j)
            }
        }
    }
}

@LaTeX
suspend fun Table(
    alignment: Alignment = CENTER,
    position: Position? = null,
    content: List<List<@LaTeX suspend () -> Unit>>
) {
    val alignments = listOf(size = content.width) { alignment }
    Table(alignments, position, content)
}

@LaTeX
suspend fun Table(
    alignments: List<Alignment>,
    position: Position? = null,
    content: List<List<@LaTeX suspend () -> Unit>>
) {
    Tabular(alignments, position) {
        Hline()
        content.forEach {
            TableRow(it)
        }
    }
}

/**
 * Tabular environment. Within this environment it is allowed to use the [Hline]
 * and [Cline] commands and [TableRow].
 *
 * @param alignments [Alignment] list, defines how the elements of each of the
 * columns will be in the table.
 * @param position Vertical position of table. Optional argument.
 * @param content Content of the [Tabular] environment.
 */
@LaTeX
suspend fun Tabular(
    alignments: List<Alignment>,
    position: Position? = null,
    content: @LaTeX suspend () -> Unit
) {
    val posOption = if (position != null) listOf(position.parameter) else emptyList()
    position?.let { pos ->
        pos.requiresPackage?.let {
            DeclareExternalPackage(it)
        }
    }
    Environment(
        name = "tabular",
        arguments = listOf(
            makeTablePreamble(alignments)
        ),
        additionalOptions = posOption,
        content = content
    )
}

/**
 * Table row. Unlike the function `TableRow(content: List<suspend () -> Unit>)`,
 * it does not take a list of lambda functions, but one lambda function with a
 * column parameter and a row size.
 *
 * @param size Size of the table row.
 * @param content A function that takes a column index as input and draws
 * content after the call.
 */
@LaTeX
suspend fun TableRow(
    size: Int,
    content: @LaTeX suspend (column: Int) -> Unit
) {
    if (size == 0) {
        return
    }
    if (size < 0) {
        throw IllegalArgumentException("Table row size should be > 0, actual: $size")
    }
    for (j in 0 until size) {
        content(j)
        if (j == size - 1) {
            RowEnd()
        } else {
            NextEntry()
        }
    }
    Hline()
}

/**
 * Table row. A horizontal line ([Hline]) is drawn after each row of the
 * table.
 *
 * @param content List of @LaTeX content, when each item is row entry. If it
 * is empty, the row will not be output.
 */
@LaTeX
suspend fun TableRow(
    content: List<@LaTeX suspend () -> Unit>
) {
    if (content.isEmpty()) {
        return
    }
    for (j in content.indices) {
        content[j]()
        if (content.isNotLast(j)) {
            NextEntry()
        } else {
            RowEnd()
        }
    }
    Hline()
}

/**
 * Used to position content in a different way (for example, combine 2 columns
 * and position text inside). This command must be after [NextEntry] or at the
 * beginning of the [TableRow].
 *
 * @param columns The number of columns whose contents will be different.
 * @param alignments [Alignment] list, defines how the elements of each of the
 * columns will be inside the multicolumn.
 * @param content Content of the multicolumn.
 */
@LaTeX
suspend fun Multicolumn(
    columns: Int,
    alignments: List<Alignment>,
    content: @LaTeX suspend () -> Unit
) {
    Command(
        name = "multicolumn",
        arguments = listOf(
            columns.toString(),
            makeTablePreamble(alignments)
        )
    ) + BraceWrapped(content)
}

/**
 * Draws a horizontal line in the size from first to second item. This command
 * is only available inside a table.
 *
 * @param from First item, start of horizontal line.
 * @param to Second item, end of horizontal line.
 */
@LaTeX
suspend inline fun Cline(
    from: Int,
    to: Int
) {
    Content(raw = """\cline{$from-$to}""") + LineBreak()
}

/**
 * Draws a horizontal line in the size of the entire table. This command is
 * only available inside a table.
 */
@LaTeX
suspend inline fun Hline() {
    Command(
        name = "hline",
        endLineBreak = true
    )
}

private fun makeTablePreamble(
    alignments: List<Alignment>
): String {
    if (alignments.isEmpty()) {
        throw IllegalArgumentException(
            "Unable to make table preamble. Alignment list can not be empty."
        )
    }
    return StringBuilder().apply {
        append("|")
        alignments.forEach {
            append(it.code)
            append("|")
        }
    }.toString()
}
