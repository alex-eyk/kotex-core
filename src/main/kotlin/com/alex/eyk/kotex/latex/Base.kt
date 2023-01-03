package com.alex.eyk.kotex.latex

import com.alex.eyk.kotex.state.MultiState
import com.alex.eyk.kotex.state.TempFilesState
import com.alex.eyk.kotex.state.factory.CacheStateFactory
import com.alex.eyk.kotex.state.factory.StateFactory
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.currentCoroutineContext
import java.io.File

private val multiStateFactory: StateFactory<CharSequence, MultiState<Iterable<File>>> =
    CacheStateFactory()

/**
 * Basic LaTeX block content state management function. Every LaTeX function
 * eventually necessarily calls this function to add 'raw' content.
 *
 * Thus, a function annotated with @LaTeX annotation converts some input data
 * into LaTeX content and calls that function.
 *
 * @param content LaTeX code to be added to the current block.
 */
@LaTeX
suspend fun RawContent(
    content: CharSequence
) {
    coroutineState()
        .append(rawContent = content)
}

/**
 * LaTeX processes the file as if its contents were inserted in the current
 * file.
 * If filename does not end in ‘.tex’ then LaTeX first tries the filename
 * with that extension; this is the usual case. If filename ends with ‘.tex’
 * then LaTeX looks for the filename as it is.
 */
@LaTeX
suspend inline fun Input(
    filename: CharSequence
) {
    RawContent(
        content = "\\input{$filename}" + System.lineSeparator()
    )
}

@LaTeX
internal suspend inline fun Tag(
    tag: String
) {
    coroutineState()
        .setTag(tag)
}

internal fun registerDocument(
    name: String
) {
    assertName(name)
    multiStateFactory.put(
        name, TempFilesState(name)
    )
}

internal fun removeDocument(
    name: String
) {
    if (multiStateFactory.contains(key = name)) {
        multiStateFactory.remove(name)
    }
}

internal suspend fun currentTag(): String {
    return coroutineState()
        .getTag()
        .toString()
}

internal suspend fun currentContent(): Iterable<File> {
    return coroutineState()
        .getContent()
}

internal suspend fun coroutineState(): MultiState<Iterable<File>> {
    currentCoroutineContext()[CoroutineName.Key]?.let {
        return multiStateFactory.get(it.name)
    } ?: throw IllegalStateException(
        "Unable to get document name from coroutine context. Check that @LaTeX functions " +
                "are called only in the correct context."
    )
}

private fun assertName(
    name: String
) {
    if (multiStateFactory.contains(key = name)) {
        throw IllegalArgumentException(
            "Document or block with name: $name is already exists."
        )
    }
}
