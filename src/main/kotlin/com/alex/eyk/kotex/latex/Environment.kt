@file:Suppress("REDUNDANT_INLINE_SUSPEND_FUNCTION_TYPE")

package com.alex.eyk.kotex.latex

import com.alex.eyk.kotex.util.plus

/**
 * [Environment] that formats the content within it like a quote by indenting
 * it.
 *
 * @param content Content of quote.
 */
@LaTeX
suspend inline fun Quote(
    content: @LaTeX () -> Unit
) {
    Environment(
        name = "quote",
        content = content
    )
}

/**
 * [Environment] used to right-align content.
 *
 * @param content Content to be right aligned.
 */
@LaTeX
suspend inline fun FlushRight(
    content: @LaTeX () -> Unit
) {
    Environment(
        name = "flushright",
        content = content
    )
}

/**
 * [Environment] used to left-align content.
 *
 * @param content Content to be left aligned.
 */
@LaTeX
suspend inline fun FlushLeft(
    content: @LaTeX () -> Unit
) {
    Environment(
        name = "flushleft",
        content = content
    )
}

/**
 * [Environment] used to center content.
 *
 * @param content Content to be centered.
 */
@LaTeX
suspend inline fun Center(
    content: @LaTeX () -> Unit
) {
    Environment(
        name = "center",
        content = content
    )
}

@LaTeX
suspend inline fun Environment(
    name: String,
    options: List<String> = emptyList(),
    additionalOptions: List<String> = emptyList(),
    arguments: List<String> = emptyList(),
    content: @LaTeX () -> Unit
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
 * Used to wrap the content passed as a parameter with angle braces. An
 * analogue of the Wrapped function with start parameter is `[`, end is
 * `]`.
 *
 * @param content Content to be wrapped.
 */
@LaTeX
suspend inline fun AngleBraceWrapped(
    content: @LaTeX () -> Unit
) {
    Wrapped(
        start = "[",
        end = "]",
        content = content
    )
}

/**
 * Used to wrap the content passed as a parameter with braces. An analogue of
 * the Wrapped function with start parameter is `{`, end is `}`.
 *
 * @param content Content to be wrapped.
 */
@LaTeX
suspend inline fun BraceWrapped(
    content: @LaTeX () -> Unit
) {
    Wrapped(
        start = "{",
        end = "}",
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
    Content(
        raw = start
    )
    content()
    Content(
        raw = end
    )
}

/**
 * Environment is a certain container, all content inside of which is modified
 * in some way. For example, for the `center` environment, all text will be
 * centered.
 *
 * The beginning of the environment is declared using this function. Every
 * environment also needs an end, i.e. it is necessary to call function
 * [EnvironmentEnd].
 *
 * @param name Name of environment.
 */
@LaTeX
suspend fun EnvironmentBegin(
    name: String,
    options: List<String> = emptyList(),
    additionalOptions: List<String> = emptyList(),
    arguments: List<String> = emptyList()
) {
    Content(
        raw = "\\begin{$name}" +
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
    Content(raw = "\\end{$name}") + LineBreak()
}
