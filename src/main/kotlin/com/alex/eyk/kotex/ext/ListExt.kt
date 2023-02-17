package com.alex.eyk.kotex.ext

fun <T> listOf(
    size: Int,
    init: (i: Int) -> T
) : List<T> {
    return ArrayList<T>().apply {
        for (i in 0 until size) {
            add(init(i))
        }
    }
}

fun <E> MutableList<E>.insert(
    item: E
) {
    this.add(0, item)
}

fun <E> List<E>.isNotLast(
    i: Int
): Boolean {
    return !isLast(i)
}

fun <E> List<E>.isLast(
    i: Int
): Boolean {
    return i == (this.size - 1)
}

fun <E> List<E>.copy(): MutableList<E> {
    return ArrayList(this)
}
