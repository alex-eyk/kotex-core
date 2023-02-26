package com.alex.eyk.kotex.util

import com.alex.eyk.kotex.ext.copy
import java.lang.IllegalArgumentException

internal typealias Matrix<E> = List<List<E>>

internal typealias MutableMatrix<E> = MutableList<MutableList<E>>

internal inline fun <E> matrixOf(
    height: Int,
    width: Int,
    initial: (i: Int, j: Int) -> E
): Matrix<E> = mutableMatrixOf(height, width, initial)

internal inline fun <E> mutableMatrixOf(
    height: Int,
    width: Int,
    initial: (i: Int, j: Int) -> E
): MutableMatrix<E> {
    if (height <= 0 || width <= 0) {
        throw IllegalArgumentException(
            "Matrix height and width should be > 0, actual: { height: $height, width: $width }"
        )
    }
    val matrix = mutableListOf<MutableList<E>>()
    for (i in 0 until height) {
        matrix.add(mutableListOf())
        for (j in 0 until width) {
            matrix[i].add(initial(i, j))
        }
    }
    return matrix
}

internal fun <E> mutableMatrixOf(
    matrix: Matrix<E>
): MutableMatrix<E> {
    val mutableMatrix = mutableListOf<MutableList<E>>()
    matrix.forEach {
        mutableMatrix.add(it.copy())
    }
    return mutableMatrix
}
