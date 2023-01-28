package com.alex.eyk.kotex.ext

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
