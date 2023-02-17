package com.alex.eyk.kotex.ext

import com.alex.eyk.kotex.util.Matrix

val <E> Matrix<E>.height: Int
    get() = this.size

val <E> Matrix<E>.width: Int
    get() {
        if (this.isEmpty()) {
            return 0
        }
        return this[0].size
    }
