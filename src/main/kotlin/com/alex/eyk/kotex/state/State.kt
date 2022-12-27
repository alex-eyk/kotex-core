package com.alex.eyk.kotex.state

import java.io.Closeable

interface State<T> : Closeable {

    fun append(
        rawContent: CharSequence
    )

    fun getContent(): T
}
