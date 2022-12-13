package com.alex.eyk.kotex.state.factory

import com.alex.eyk.kotex.state.State

interface StateFactory<K, S : State<*>> {

    fun get(
        key: K
    ): S

    fun put(
        key: K,
        state: S
    )

    fun contains(
        key: K
    ): Boolean

    fun remove(
        key: K
    )
}
