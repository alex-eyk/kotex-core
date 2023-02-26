package com.alex.eyk.kotex.latex.math

import com.alex.eyk.kotex.entity.Package
import com.alex.eyk.kotex.ext.height
import com.alex.eyk.kotex.ext.isNotLast
import com.alex.eyk.kotex.ext.width
import com.alex.eyk.kotex.latex.DeclareExternalPackage
import com.alex.eyk.kotex.latex.Environment
import com.alex.eyk.kotex.latex.LaTeX
import com.alex.eyk.kotex.latex.NextEntry
import com.alex.eyk.kotex.latex.RowEnd
import com.alex.eyk.kotex.latex.asContent

enum class Border(
    val environment: String
) {
    PLAIN("matrix"),
    PARENTHESES("pmatrix"),
    BRACKETS("bmatrix"),
    BRACES("Bmatrix"),
    PIPES("vmatrix"),
    DOUBLE_PIPES("Vmatrix")
}

@LaTeX
suspend fun IntMatrix(
    content: List<List<Int>>,
    border: Border = Border.BRACKETS
) {
    Matrix(content) { item, _, _ ->
        item.asContent()
    }
}

@LaTeX
suspend fun <E> Matrix(
    content: List<List<E>>,
    border: Border = Border.BRACKETS,
    map: @LaTeX suspend (item: E, row: Int, column: Int) -> Unit
) {
    if (content.height == 0 || content.width == 0) {
        throw IllegalArgumentException(
            "Matrix should have height > 0 and width > 0"
        )
    }
    DeclareExternalPackage(
        Package(name = "amsmath")
    )
    Environment(
        name = border.environment
    ) {
        for (i in content.indices) {
            MatrixRow(content[i]) { item: E, j: Int -> map(item, i, j) }
        }
    }
}

@LaTeX
suspend fun Matrix(
    border: Border = Border.BRACKETS,
    content: List<List<@LaTeX suspend () -> Unit>>
) {
    if (content.height == 0 || content.width == 0) {
        throw IllegalArgumentException(
            "Matrix should have height > 0 and width > 0"
        )
    }
    DeclareExternalPackage(
        Package(name = "amsmath")
    )
    Environment(
        name = border.environment
    ) {
        content.forEach {
            MatrixRow(it)
        }
    }
}

@LaTeX
suspend fun MatrixRow(
    size: Int,
    content: @LaTeX (column: Int) -> Unit
) {
    for (j in 0 until size) {
        content(j)
        if (j != size - 1) {
            NextEntry()
        } else {
            RowEnd()
        }
    }
}

@LaTeX
suspend fun <E> MatrixRow(
    content: List<E>,
    map: @LaTeX suspend (item: E, column: Int) -> Unit
) {
    for (j in content.indices) {
        map(content[j], j)
        if (content.isNotLast(j)) {
            NextEntry()
        } else {
            RowEnd()
        }
    }
}

@LaTeX
suspend fun MatrixRow(
    content: List<@LaTeX suspend () -> Unit>
) {
    for (j in content.indices) {
        content[j]()
        if (content.isNotLast(j)) {
            NextEntry()
        } else {
            RowEnd()
        }
    }
}
