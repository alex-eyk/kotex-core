package com.alex.eyk.kotex.ext

fun Boolean.assertSuccess() {
    if (!this) {
        throw IllegalStateException(
            "Unable to complete action with file"
        )
    }
}
