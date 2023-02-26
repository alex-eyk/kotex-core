package com.alex.eyk.kotex.state.factory

import com.alex.eyk.kotex.state.State
import java.util.concurrent.locks.ReentrantReadWriteLock

open class CacheStateFactory<S : State<*>>
internal constructor() : StateFactory<CharSequence, S> {

    private val stateCache: MutableMap<CharSequence, S> = HashMap()

    private val lock = ReentrantReadWriteLock()
    private val readLock = lock.readLock()
    private val writeLock = lock.writeLock()

    override fun get(
        key: CharSequence
    ): S {
        try {
            readLock.lock()
            return stateCache[key]
                ?: throw NoSuchElementException()
        } finally {
            readLock.unlock()
        }
    }

    override fun put(
        key: CharSequence,
        state: S
    ) {
        try {
            writeLock.lock()
            stateCache[key] = state
        } finally {
            writeLock.unlock()
        }
    }

    override fun contains(
        key: CharSequence
    ): Boolean {
        try {
            readLock.lock()
            return stateCache.contains(key)
        } finally {
            readLock.unlock()
        }
    }

    override fun remove(
        key: CharSequence
    ) {
        try {
            writeLock.lock()
            if (stateCache.contains(key)) {
                stateCache[key]?.close()
                stateCache.remove(key)
            }
        } finally {
            writeLock.unlock()
        }
    }
}
