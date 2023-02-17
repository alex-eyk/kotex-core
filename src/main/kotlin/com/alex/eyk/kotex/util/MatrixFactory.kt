package com.alex.eyk.kotex.util

import com.alex.eyk.kotex.ext.copy

internal typealias Matrix<E> = List<List<E>>

internal typealias MutableMatrix<E> = MutableList<MutableList<E>>

internal inline fun <E> Matrix(
    height: Int,
    width: Int,
    initial: (i: Int, j: Int) -> E
): Matrix<E> = MutableMatrix(height, width, initial)

internal inline fun <E> MutableMatrix(
    height: Int,
    width: Int,
    initial: (i: Int, j: Int) -> E
): MutableMatrix<E> {
    val matrix = mutableListOf<MutableList<E>>()
    for (i in 0 until height) {
        matrix.add(mutableListOf())
        for (j in 0 until width) {
            matrix[i].add(initial(i, j))
        }
    }
    return matrix
}

internal fun <E> MutableMatrix(
    matrix: Matrix<E>
): MutableMatrix<E> {
    val mutableMatrix = mutableListOf<MutableList<E>>()
    matrix.forEach {
        mutableMatrix.add(it.copy())
    }
    return mutableMatrix
}
