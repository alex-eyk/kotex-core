package com.alex.eyk.kotex.state

interface MultiState<T> : State<T> {

    fun getTag(): CharSequence

    fun setTag(
        tag: CharSequence
    )
}
