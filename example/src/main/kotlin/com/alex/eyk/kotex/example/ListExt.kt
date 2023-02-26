package com.alex.eyk.kotex.example

fun <E> List<E>.copy(): MutableList<E> = ArrayList(this)

fun <E> List<List<E>>.deepcopy(): MutableList<MutableList<E>> {
    return mutableListOf<MutableList<E>>().apply {
        this@deepcopy.forEach {
            add(it.copy())
        }
    }
}

fun List<Int>.sum(): Int {
    var sum = 0
    forEach {
        sum += it
    }
    return sum
}

fun <T> listOf(
    size: Int,
    init: (i: Int) -> T
): MutableList<T> {
    return ArrayList<T>().apply {
        for (i in 0 until size) {
            add(init(i))
        }
    }
}

fun <E> matrixOf(
    height: Int,
    width: Int,
    init: (i: Int, j: Int) -> E
): MutableList<MutableList<E>> {
    if (height <= 0 || width <= 0) {
        throw IllegalArgumentException(
            "Matrix height and width should be > 0, actual: { height: $height, width: $width }"
        )
    }
    val matrix = mutableListOf<MutableList<E>>()
    for (i in 0 until height) {
        matrix.add(mutableListOf())
        for (j in 0 until width) {
            matrix[i].add(init(i, j))
        }
    }
    return matrix
}
