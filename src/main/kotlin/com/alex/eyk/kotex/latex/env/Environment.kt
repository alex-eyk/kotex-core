@file:Suppress("REDUNDANT_INLINE_SUSPEND_FUNCTION_TYPE")

package com.alex.eyk.kotex.latex.env

import com.alex.eyk.kotex.latex.LaTeX
import com.alex.eyk.kotex.latex.LineBreak
import com.alex.eyk.kotex.latex.RawContent
import com.alex.eyk.kotex.latex.asAdditionalArgumentsString
import com.alex.eyk.kotex.latex.asAdditionalOptionsString
import com.alex.eyk.kotex.latex.asOptionsString

private const val OPEN_BRACE = "{"
private const val CLOSE_BRACE = "}"

@LaTeX
suspend fun Environment(
    name: String,
    options: List<String> = emptyList(),
    additionalOptions: List<String> = emptyList(),
    arguments: List<String> = emptyList(),
    content: @LaTeX suspend () -> Unit
) {
    EnvironmentBegin(
        name,
        options,
        additionalOptions,
        arguments
    )
    content()
    EnvironmentEnd(name)
}

/**
 * Used to wrap the content passed as a parameter with braces. An analogue of
 * the Wrapped function with start parameter is `{`, end is `}`.
 *
 * @param content Content to be wrapped.
 */
@LaTeX
suspend fun BraceWrapped(
    content: suspend () -> Unit
) {
    Wrapped(
        start = OPEN_BRACE,
        end = CLOSE_BRACE,
        content = content
    )
}

/**
 * Used to wrap the content passed as a parameter with passed text. An
 * analogue of the Wrapped function with three parameters, but the start and
 * end text are the same.
 *
 * @param with Text that will wrap the content on both sides.
 * @param content Content to be wrapped.
 */
@LaTeX
suspend inline fun Wrapped(
    with: String,
    content: @LaTeX suspend () -> Unit
) {
    Wrapped(
        start = with,
        end = with,
        content = content
    )
}

/**
 * Used to wrap the content passed as a parameter with passed start and end.
 * For example, after executing this code, `{content}` will be added to the
 * document:
 * ```
 *  Wrapped(
 *      start = "{",
 *      end = "}"
 *  ) {
 *      Text {
 *          "content"
 *      }
 *  }
 * ```
 *
 * @param start Text to be added at the beginning.
 * @param end Text to be added at the end.
 * @param content Content to be wrapped.
 */
@LaTeX
suspend inline fun Wrapped(
    start: String,
    end: String,
    content: @LaTeX suspend () -> Unit
) {
    RawContent(
        content = start
    )
    content()
    RawContent(
        content = end
    )
}

@LaTeX
suspend fun EnvironmentBegin(
    name: String,
    options: List<String> = emptyList(),
    additionalOptions: List<String> = emptyList(),
    arguments: List<String> = emptyList()
) {
    RawContent(
        content = blockBegin(name) +
                options.asOptionsString() +
                additionalOptions.asAdditionalOptionsString() +
                arguments.asAdditionalArgumentsString()
    )
    LineBreak()
}

@LaTeX
suspend fun EnvironmentEnd(
    name: String
) {
    LineBreak()
    RawContent(
        content = blockEnd(name)
    )
    LineBreak()
}

private fun blockBegin(
    name: String
): String {
    return "\\begin{$name}"
}

private fun blockEnd(
    name: String
): String {
    return "\\end{$name}"
}
