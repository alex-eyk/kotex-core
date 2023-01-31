package com.alex.eyk.kotex.latex

import com.alex.eyk.kotex.ext.minimize

/**
 * Adds a vertical space of some size.
 *
 * @param length Space size in the given dimension.
 * @param dimension The dimension in which the size of the space is specified.
 * @param removeSpaceOnPageEnd By default LaTeX removes vertical space at the
 * end of a page. This parameter overrides this rule.
 */
@LaTeX
suspend fun VerticalSpace(
    length: Float,
    dimension: Dimension = Dimension.PT,
    removeSpaceOnPageEnd: Boolean = true
) {
    Command(
        name = "vspace",
        optional = if (removeSpaceOnPageEnd) emptyList() else listOf("*"),
        value = "${length.minimize()}${dimension.abbreviation}",
        endLineBreak = true
    )
}

/**
 * Set the size of the indent before the paragraph. By default, the first
 * paragraph indent size is zero despite this setting.
 *
 * @param length Indent size in the given dimension.
 * @param dimension The dimension in which the size of the indent is
 * specified.
 */
@LaTeX
suspend fun SetParagraphIndent(
    length: Float,
    dimension: Dimension = Dimension.PT
) {
    Command(
        name = "setlength",
        arguments = listOf("\\parindent"),
        value = "${length.minimize()}${dimension.abbreviation}",
        endLineBreak = true
    )
}

/**
 * In a vertical mode this command triggers a new non-indented paragraph.
 *
 * In a horizontal mode or math mode it has no effect.
 */
@LaTeX
suspend fun NoIndent() {
    Command(name = "noindent")
}

/**
 * In a horizontal mode or math mode [Indent] inserts a space of width
 * `\parindent` (set using [SetParagraphIndent]).
 *
 * In a vertical mode command triggers the start a new indented paragraph.
 */
@LaTeX
suspend fun Indent() {
    Command(name = "indent")
}

/**
 * An alternative for [com.alex.eyk.kotex.latex.Center] environment.
 * This command switch text alignment from the point they are inserted down
 * to another switch command, end of group or document.
 */
@LaTeX
suspend fun Centering() {
    Command(
        name = "centering",
        endLineBreak = true
    )
}

/**
 * An alternative for [com.alex.eyk.kotex.latex.FlushLeft] environment.
 * This command switch text alignment from the point they are inserted down
 * to another switch command, end of group or document.
 */
@LaTeX
suspend fun RaggedLeft() {
    Command(
        name = "raggedleft",
        endLineBreak = true
    )
}

/**
 * An alternative for [com.alex.eyk.kotex.latex.FlushRight] environment.
 * This command switch text alignment from the point they are inserted down
 * to another switch command, end of group or document.
 */
@LaTeX
suspend fun RaggedRight() {
    Command(
        name = "raggedright",
        endLineBreak = true
    )
}

/**
 * Wraps content with a group. Any settings set in this group will only apply
 * to this group. Similar to using [BeginGroup] and [EndGroup].
 *
 * @param content Content to be added to the group.
 */
@LaTeX
suspend inline fun Group(
    content: @LaTeX () -> Unit
) {
    BeginGroup()
    content()
    EndGroup()
}

/**
 * Starts a new group. Any settings set in this group will only apply to
 * this group. The group must end with [EndGroup].
 */
@LaTeX
suspend fun BeginGroup() {
    Command(
        name = "begingroup",
        endLineBreak = true
    )
}

/**
 * End of current group.
 */
@LaTeX
suspend fun EndGroup() {
    Command(
        name = "endgroup",
        endLineBreak = true
    )
}

@LaTeX
suspend inline fun NewEnvironment(
    name: String,
    arguments: Int = 0,
    optionalDefault: String = "",
    begin: @LaTeX () -> Unit,
    end: @LaTeX () -> Unit
) {
    val argsOption = if (arguments == 0) "" else arguments.asOption()
    Content(
        raw = "\\newenvironment{$name}" + argsOption + optionalDefault.asOption()
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
suspend inline fun NewCommand(
    name: String,
    arguments: Int = 0,
    optionalDefault: String = "",
    definition: @LaTeX () -> Unit
) {
    val argsOption = if (arguments == 0) "" else arguments.asOption()
    Content(
        raw = "\\newcommand{$name}" + argsOption + optionalDefault.asOption()
    )
    LineBreak()
    BraceWrapped(
        content = definition
    )
    LineBreak()
}

@LaTeX
suspend fun Command(
    name: String,
    value: String = "",
    optional: List<String> = emptyList(),
    arguments: List<String> = emptyList(),
    endLineBreak: Boolean = false
) {
    Content(
        raw = "\\$name" +
                optional.asAdditionalOptionsString() +
                arguments.asAdditionalArgumentsString() +
                value.withBracesOrEmpty()
    )
    if (endLineBreak) {
        LineBreak()
    }
}
