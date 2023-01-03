package com.alex.eyk.kotex.document

import com.alex.eyk.kotex.latex.Input
import com.alex.eyk.kotex.latex.LaTeX
import com.alex.eyk.kotex.latex.Tag
import com.alex.eyk.kotex.latex.currentContent
import com.alex.eyk.kotex.latex.currentTag
import com.alex.eyk.kotex.state.TempFilesState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

open class BaseDocument(
    name: String,
    private val multiDeclareAllowed: Boolean = false
) : AbstractDocument<Iterable<File>>(name) {

    private val stateLockMap: MutableMap<String, Lock> = HashMap()
    private val declaredTags: MutableSet<String> = HashSet()

    companion object {

        const val PREAMBLE = TempFilesState.PREAMBLE
    }

    init {
        declare(PREAMBLE)
    }

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

    final override fun declare(
        tag: String
    ) {
        if (multiDeclareAllowed || tag !in declaredTags) {
            append {
                Input(tag)
                declaredTags.add(tag)
            }
        } else {
            throw IllegalStateException("Tag $tag already declared")
        }
    }

    override fun append(
        tag: String,
        declareImmediately: Boolean,
        content: @LaTeX suspend () -> Unit
    ) {
        append {
            if (declareImmediately) {
                declare(tag)
            }
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
