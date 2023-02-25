package com.alex.eyk.kotex.document

import com.alex.eyk.kotex.latex.DocumentClass
import com.alex.eyk.kotex.latex.Input
import com.alex.eyk.kotex.latex.LaTeX
import com.alex.eyk.kotex.latex.currentTag
import com.alex.eyk.kotex.latex.documentContent
import com.alex.eyk.kotex.latex.setTag
import com.alex.eyk.kotex.state.TempFilesState
import com.alex.eyk.kotex.util.PathUtils
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

open class BaseDocument(
    name: String,
    path: String = PathUtils.getJarDirectoryPath(),
    private val multiDeclareAllowed: Boolean = false
) : AbstractDocument<Iterable<File>>(name, path) {

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
            setTag(tag)
            content()
            setTag(lastTag)
        }
    }

    fun setDocumentClass(
        name: String,
        options: List<String>
    ) = append(tag = "preamble") {
        DocumentClass(name, options)
    }

    override suspend fun getContent(): Iterable<File> {
        return withContext(
            context = blockScope.coroutineContext
        ) {
            try {
                lockAll()
                return@withContext documentContent()
            } finally {
                unlockAll()
            }
        }
    }

    override fun getContent(
        onComplete: (Iterable<File>) -> Unit
    ) {
        blockScope.launch {
            onComplete(getContent())
        }
    }

    override fun close() {
        blockScope.launch {
            try {
                lockAll()
                super.close()
            } finally {
                unlockAll()
            }
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

    private fun lockAll() {
        stateLockMap.forEach { (_, lock) ->
            lock.lock()
        }
    }

    private fun unlockAll() {
        stateLockMap.forEach { (_, lock) ->
            lock.unlock()
        }
    }
}
