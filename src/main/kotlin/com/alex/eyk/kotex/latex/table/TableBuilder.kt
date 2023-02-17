package com.alex.eyk.kotex.latex.table

import com.alex.eyk.kotex.ext.height
import com.alex.eyk.kotex.ext.insert
import com.alex.eyk.kotex.ext.width
import com.alex.eyk.kotex.latex.LaTeX
import com.alex.eyk.kotex.util.MutableMatrix

class TableBuilder {

    private lateinit var content: MutableMatrix<suspend () -> Unit>

    fun <T> content(
        content: List<List<T>>,
        map: suspend (item: T, row: Int, column: Int) -> Unit
    ): TableBuilder = apply {
        this.content = MutableMatrix(
            height = content.height,
            width = content.width
        ) { i, j ->
            { map(content[i][j], i, j) }
        }
    }

    fun content(
        height: Int,
        width: Int,
        content: @LaTeX suspend (row: Int, column: Int) -> Unit
    ): TableBuilder = apply {
        this.content = MutableMatrix(height, width) { i, j ->
            { content(i, j) }
        }
    }

    fun expandTop(
        content: @LaTeX suspend (column: Int) -> Unit,
    ): TableBuilder = apply {
        addRow(addToTop = true, content)
    }

    fun expandBottom(
        content: @LaTeX suspend (column: Int) -> Unit
    ): TableBuilder = apply {
        addRow(addToTop = false, content)
    }

    fun expandRight(
        content: @LaTeX suspend (row: Int) -> Unit
    ): TableBuilder = apply {
        addColumn(addToRight = true, content)
    }

    fun expandLeft(
        content: @LaTeX suspend (row: Int) -> Unit
    ): TableBuilder = apply {
        addColumn(addToRight = false, content)
    }

    private fun addRow(
        addToTop: Boolean,
        content: @LaTeX suspend (column: Int) -> Unit
    ) {
        val row = mutableListOf<@LaTeX suspend () -> Unit>()
        for (j in this.content[0].indices) {
            row.add { content(j) }
        }
        if (addToTop) {
            this.content.insert(row)
        } else {
            this.content.add(row)
        }
    }

    private fun addColumn(
        addToRight: Boolean,
        content: @LaTeX suspend (row: Int) -> Unit
    ) {
        for (i in this.content.indices) {
            if (addToRight) {
                this.content[i].add { content(i) }
            } else {
                this.content[i].insert { content(i) }
            }
        }
    }

    fun build(): MutableMatrix<@LaTeX suspend () -> Unit> {
        return content
    }
}
