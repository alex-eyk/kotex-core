package com.alex.eyk.kotex.ext

operator fun String.times(
    times: Int
): String {
    StringBuilder().apply {
        repeat(times) {
            append(this@times)
        }
        return this.toString()
    }
}
