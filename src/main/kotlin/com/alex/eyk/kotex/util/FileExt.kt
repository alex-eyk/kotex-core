package com.alex.eyk.kotex.util

fun Boolean.assertSuccess() {
    if (!this) {
        throw IllegalStateException(
            "Unable to complete action with file"
        )
    }
}
