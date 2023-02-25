package com.alex.eyk.kotex.document

import com.alex.eyk.kotex.latex.LaTeX
import com.alex.eyk.kotex.latex.registerDocument
import com.alex.eyk.kotex.latex.removeDocument
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import java.io.Closeable
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

abstract class AbstractDocument<C>(
    val name: String,
    path: String
) : Closeable {

    private val dispatcher: CoroutineDispatcher = Executors.newSingleThreadExecutor()
        .asCoroutineDispatcher()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, t ->
        onCoroutineException(t)
    }

    private val blockContext: CoroutineContext =
        dispatcher + SupervisorJob() + CoroutineName(name) + coroutineExceptionHandler

    /**
     * Special coroutine scope for the latex block that should be used to
     * update the content of the block.
     */
    protected val blockScope: CoroutineScope = CoroutineScope(
        context = blockContext
    )

    init {
        registerDocument(name, path)
    }

    abstract fun append(
        content: @LaTeX suspend () -> Unit
    )

    abstract fun append(
        tag: String,
        declareImmediately: Boolean = true,
        content: @LaTeX suspend () -> Unit
    )

    abstract fun declare(
        tag: String
    )

    abstract suspend fun getContent(): C

    abstract fun getContent(onComplete: (C) -> Unit)

    override fun close() {
        removeDocument(name)
    }

    protected open fun onCoroutineException(t: Throwable) {}
}
