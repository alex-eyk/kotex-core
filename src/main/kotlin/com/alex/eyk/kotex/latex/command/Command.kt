package com.alex.eyk.kotex.latex.command

import com.alex.eyk.kotex.latex.env.BraceWrapped
import com.alex.eyk.kotex.latex.LaTeX
import com.alex.eyk.kotex.latex.LineBreak
import com.alex.eyk.kotex.latex.Content
import com.alex.eyk.kotex.latex.asOption
import com.alex.eyk.kotex.latex.asOptionsString
import com.alex.eyk.kotex.latex.withBracesOrEmpty

private const val NEW_ENVIRONMENT = "newenvironment"
private const val NEW_COMMAND = "newcommand"

@LaTeX
suspend fun NewEnvironment(
    name: String,
    arguments: Int = 0,
    optionalDefault: String = "",
    begin: @LaTeX suspend () -> Unit,
    end: @LaTeX suspend () -> Unit
) {
    Content(
        raw = definiteCommand(
            command = NEW_ENVIRONMENT,
            name,
            arguments,
            optionalDefault
        )
    )
    LineBreak()
    BraceWrapped(
        content = begin
    )
    LineBreak()
    BraceWrapped(
        content = end
    )
    LineBreak()
}

@LaTeX
suspend fun NewCommand(
    name: String,
    arguments: Int = 0,
    optionalDefault: String = "",
    definition: @LaTeX suspend () -> Unit
) {
    Content(
        raw = definiteCommand(
            command = NEW_COMMAND,
            name,
            arguments,
            optionalDefault
        )
    )
    LineBreak()
    BraceWrapped(
        content = definition
    )
    LineBreak()
}

@LaTeX
suspend inline fun Command(
    name: String,
    value: String = "",
    starred: Boolean = false,
    options: List<String> = emptyList(),
    optional: List<String> = emptyList(),
    endLineBreak: Boolean = true
) {
    Content(
        raw = command(name, starred, value, options, optional)
    )
    if (endLineBreak) {
        LineBreak()
    }
}

fun command(
    name: String,
    starred: Boolean,
    value: String,
    options: List<String>,
    optional: List<String>
): String {
    val star = if (starred) "*" else ""
    return "\\$name$star" +
            options.asOptionsString() +
            value.withBracesOrEmpty() +
            optional.asOptionsString()
}

fun definiteCommand(
    command: String,
    name: String,
    arguments: Int = 0,
    optionalDefault: String = ""
): String {
    val argsOption = if (arguments == 0) "" else arguments.asOption()
    return "\\$command{$name}" + argsOption + optionalDefault.asOption()
}
