package com.alex.eyk.kotex.ext

fun Int.inRange(from: Int, to: Int): Boolean {
    return from <= this || to >= this
}
