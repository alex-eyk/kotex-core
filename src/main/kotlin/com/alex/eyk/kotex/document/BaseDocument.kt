package com.alex.eyk.kotex.document

import com.alex.eyk.kotex.latex.LaTeX
import com.alex.eyk.kotex.latex.Tag
import com.alex.eyk.kotex.latex.currentContent
import com.alex.eyk.kotex.latex.currentTag
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

open class BaseDocument(
    name: String
) : AbstractDocument<Iterable<File>>(name) {

    private val stateLockMap: MutableMap<String, Lock> = HashMap()

    override fun append(
        content: @LaTeX suspend () -> Unit
    ) {
        blockScope.launch {
            val stateLock = lockFor(currentTag())
            try {
                stateLock.lock()
                content()
            } finally {
                stateLock.unlock()
            }
        }
    }

    override fun declare(
        tag: String
    ) {
        append(
            tag = tag,
            content = {}
        )
    }

    override fun append(
        tag: String,
        content: @LaTeX suspend () -> Unit
    ) {
        append {
            val lastTag = currentTag()
            Tag(tag)
            content()
            Tag(lastTag)
        }
    }

    override suspend fun getContent(): Iterable<File> {
        return withContext(
            context = blockScope.coroutineContext
        ) {
            try {
                stateLockMap.forEach { (_, lock) ->
                    lock.lock()
                }
                return@withContext currentContent()
            } finally {
                stateLockMap.forEach { (_, lock) ->
                    lock.unlock()
                }
            }
        }
    }

    override fun getContent(onComplete: (Iterable<File>) -> Unit) {
        blockScope.launch {
            onComplete(getContent())
        }
    }

    private fun lockFor(
        tag: String
    ): Lock {
        if (tag !in stateLockMap) {
            stateLockMap[tag] = ReentrantLock()
        }
        return stateLockMap[tag]!!
    }
}
