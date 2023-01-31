package com.alex.eyk.kotex.ext

fun Float.minimize(): String {
    return if ((this - this.toInt()) == 0F) {
        this.toInt().toString()
    } else {
        this.toString()
    }
}
