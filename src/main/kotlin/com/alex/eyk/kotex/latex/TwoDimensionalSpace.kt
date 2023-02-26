package com.alex.eyk.kotex.latex

import com.alex.eyk.kotex.util.plus

/**
 * The end of the row of table, matrix or array.
 */
@LaTeX
suspend inline fun RowEnd() {
    Content(raw = """ \\""") + LineBreak()
}


/**
 * An analogue of function [NextEntry] with shorter name. Should use for
 * separate one entry from another. Available only inside the row of table,
 * matrix or array.
 */
suspend inline fun `&`() {
    NextEntry()
}

/**
 * This unit should use for separate one entry inside one row from another.
 * Available only inside the row of table, matrix or array.
 */
@LaTeX
suspend inline fun NextEntry() {
    Content(raw = """ & """)
}
