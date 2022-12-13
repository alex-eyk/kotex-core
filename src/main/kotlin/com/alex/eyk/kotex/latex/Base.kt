package com.alex.eyk.kotex.latex

import com.alex.eyk.kotex.state.MultiState
import com.alex.eyk.kotex.state.factory.CacheStateFactory
import com.alex.eyk.kotex.state.factory.StateFactory
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.currentCoroutineContext
import java.io.File

private val stateFactory: StateFactory<CharSequence, MultiState<Iterable<File>>> =
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
    getCurrentState()
        .append(rawContent = content)
}

internal suspend fun getCurrentState(): MultiState<Iterable<File>> {
    val name = (currentCoroutineContext()[CoroutineName.Key]
            as CoroutineName).name
    return stateFactory.get(key = name)
}
