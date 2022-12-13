package com.alex.eyk.kotex.state

interface State<T> {

    fun append(
        rawContent: CharSequence
    )

    fun getContent(): T
}
