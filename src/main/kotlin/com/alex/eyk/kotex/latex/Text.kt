@file:Suppress("REDUNDANT_INLINE_SUSPEND_FUNCTION_TYPE")

package com.alex.eyk.kotex.latex

import com.alex.eyk.kotex.util.plus

/**
 * Modifiers to change the style of the text.
 */
enum class Modifier(
    val modifierName: String
) {

    /**
     * Modifier used to changes the font style to bold.
     */
    BOLD("textbf"),

    /**
     * Modifier used to changes the font style to italic.
     */
    ITALIC("textit"),

    /**
     * The behavior of the modifier is similar to the [ITALIC] modifier, but
     * is not exactly the same. Text modification is context-sensitive -
     * within normal text, italicized selected text, but this behavior is
     * reversed when used within italicized text.
     */
    EMPHASISING("emph"),

    /**
     * Modifier used to changes the font style to underlined.
     */
    UNDERLINE("underline"),

    /**
     * Modifier used to produce text in roman font.
     */
    ROMAN("textrm"),

    /**
     * Modifier used to produce text in sans serif font.
     */
    SANS_SERIF("textsf"),

    /**
     * Modifier used to produce text in typewriter font.
     */
    TYPEWRITER("texttt"),

    /**
     * Modifier used to produce text in font with medium weight.
     */
    MEDIUM("textmd"),

    /**
     * Modifier used to produce text in upright font.
     */
    UPRIGHT("textup"),

    /**
     * Modifier used to produce text in a slanted style. Slanted is an oblique
     * version of the roman font. The shape is basically the same but "sloped".
     * Italics, on the other hand, have different letter shapes.
     */
    SLANTED("textsl"),

    /**
     * Modifier used to produce text in lowercase with an uppercase style.
     */
    SMALL_CAPITAL("textsc"),

    /**
     * Modifier set the font encoding back to its default, and also resets to
     * their respective defaults the font family and the font shape.
     */
    NORMAL("textnormal")
}

/**
 * A set of spaces of various sizes. Here are the most used options, but not
 * all. If there is a need to use a different size, you can create your own
 * latex function:
 * ```
 *  @LaTeX
 *  fun CustomSpace() {
 *      Command(
 *          name = ","
 *      )
 *  }
 * ```
 */
enum class SpaceSize(
    val code: String
) {

    /**
     * Equivalent of space size in normal text.
     */
    AS_SPACE(" "),

    /**
     * Space size equal to the current font size.
     */
    QUAD("quad"),

    /**
     * Size of [QUAD] twice.
     */
    QUAD_TWICE("qquad"),

    /**
     * 3/18 of [QUAD] size.
     */
    MINIMAL(","),


    /**
     * 4/18 of [QUAD] size.
     */
    SMALL(":"),

    /**
     * 5/18 of [QUAD] size.
     */
    MEDIUM(";")
}

/**
 * [Paragraph] function with name like in latex (`\par`).
 */
suspend inline fun Par() {
    Paragraph()
}

/**
 * Command using to start a new paragraph. An analogue of using an empty
 * string. Since this is the end of a paragraph, a line break occurs after
 * the command.
 *
 * Code `Text("text") + Br() + Br()` is equal to `Text("text") + Paragraph()`
 */
@LaTeX
suspend fun Paragraph() {
    Command(
        name = "par",
        endLineBreak = true
    )
}

/**
 * Spaces with various lengths, including spaces in math mode.
 *
 * @param size size of space.
 */
@LaTeX
suspend fun Space(
    size: SpaceSize
) {
    Command(
        name = size.code
    )
}

/**
 * An analogue of the function [Space] with a short name. It is more
 * convenient to use such a function with the [Unit.plus] operator.
 */
@LaTeX
suspend inline fun ` `() {
    Space()
}

/**
 * Adds a space character to the document. To use multiple spaces or a space
 * of a different size, or to use a space in math mode, use the `Space`
 * function with the `size: SpaceSize` parameter.
 */
@LaTeX
suspend fun Space() {
    Content(raw = " ")
}

/**
 * Used to change the style of all text inside to [Modifier.BOLD].
 *
 * @param content Content whose text style will be changed.
 */
@LaTeX
suspend inline fun Bold(
    content: @LaTeX suspend () -> Unit
) {
    Modified(Modifier.BOLD, content)
}

/**
 * Used to change the style of all text inside to [Modifier.ITALIC].
 *
 * @param content Content whose text style will be changed.
 */
@LaTeX
suspend inline fun Italic(
    content: @LaTeX suspend () -> Unit
) {
    Modified(Modifier.ITALIC, content)
}

/**
 * Used to change the style of all text inside to [Modifier.UNDERLINE].
 *
 * @param content Content whose text style will be changed.
 */
@LaTeX
suspend inline fun Underline(
    content: @LaTeX suspend () -> Unit
) {
    Modified(Modifier.UNDERLINE, content)
}

/**
 * Used to change the style of all text inside to [Modifier.EMPHASISING].
 *
 * @param content Content whose text style will be changed.
 */
@LaTeX
suspend inline fun Emph(
    content: @LaTeX suspend () -> Unit
) {
    Modified(Modifier.EMPHASISING, content)
}

/**
 * Used to create text with a single style modification with line break.
 * For this example, the resulting text will be bold:
 * ```
 *  Textln(
 *      modifier = Modifier.BOLD
 *  ) {
 *      "Text"
 *  }
 * ```
 *
 * @param modifier Modifier that changes the text style.
 * @param text Text to be added to the document.
 */
@LaTeX
suspend inline fun Textln(
    modifier: Modifier,
    text: () -> String
) {
    Text(modifier, text()) + LineBreak()
}

/**
 * Used to create text with a single style modification with line break.
 *
 * @param modifier Modifier that changes the text style.
 * @param text Text to be added to the document.
 */
@LaTeX
suspend fun Textln(
    modifier: Modifier,
    text: String
) {
    Text(modifier, text) + LineBreak()
}

/**
 * Used to create text with a single style modification. For this example,
 * the resulting text will be bold:
 * ```
 *  Text(
 *      modifier = Modifier.BOLD
 *  ) {
 *      "Text"
 *  }
 * ```
 *
 * @param modifier Modifier that changes the text style.
 * @param text Text to be added to the document.
 */
@LaTeX
suspend inline fun Text(
    modifier: Modifier,
    text: () -> String
) {
    Modified(modifier) {
        Text(text())
    }
}

/**
 * Used to create text with a single style modification.
 *
 * @param modifier Modifier that changes the text style.
 * @param text Text to be added to the document.
 */
@LaTeX
suspend fun Text(
    modifier: Modifier,
    text: String
) {
    Modified(modifier) {
        Text(text)
    }
}

/**
 * Used to modify the style of text that is inside the environment. Example:
 * ```
 *  TextModifiedEnvironment(
 *      modifier = Modifier.BOLD
 *  ) {
 *      Text {
 *          "Example of bold text"
 *      }
 *  }
 * ```
 * There are functions that make it a little easier to work with styles, such
 * as [Bold], [Italic], [Underline], [Emph].
 *
 * @param modifier Modifier that changes the text style.
 * @param content Content whose text style will be changed.
 */
@LaTeX
suspend inline fun Modified(
    modifier: Modifier,
    content: @LaTeX suspend () -> Unit
) {
    Content(
        raw = "\\${modifier.modifierName}"
    )
    BraceWrapped(content)
}

/**
 * Extension for String, used to add text to the document unchanged with a
 * line break.
 *
 * @see asText
 */
suspend fun String.asTextln() {
    Textln(this)
}

/**
 * Extension for objects, used to add text to the document unchanged. The
 * result of the function call is the same as the result of [asContent].
 * Should use [asText] in case text will be added to the document and not,
 * for example, a mathematical expression.
 */
@LaTeX
suspend fun <E> E.asText() {
    Text(this.toString())
}

/**
 * `Text(text: () -> String)` function, completed with a line break.
 *
 * @param text Text to be added to the document.
 */
@LaTeX
suspend inline fun Textln(
    text: () -> String
) {
    Text(text) + LineBreak()
}

/**
 * `Text(text: String)` function, completed with a line break.
 *
 * @param text Text to be added to the document.
 */
@LaTeX
suspend fun Textln(
    text: String
) {
    Text(text) + LineBreak()
}

/**
 * An alternative to the Text function, which allows instead of passing text
 * as a parameter:
 * ```
 *  Text(
 *      text = "content"
 *  )
 * ```
 * use function:
 * ```
 *  Text {
 *      "content"
 *  }
 * ```
 *
 * @param text Text to be added to the document.
 */
@LaTeX
suspend inline fun Text(
    text: () -> String
) {
    Text(text())
}

/**
 * Basic function that adds text to a document.
 *
 * @param text Text to be added to the document.
 */
@LaTeX
suspend fun Text(
    text: String
) {
    Content(
        raw = text
    )
}

/**
 * [LineBreak] function with a shorter name.
 */
@LaTeX
suspend inline fun Br() {
    LineBreak()
}

/**
 * Used to wrap all new content to the next line.
 */
@LaTeX
suspend fun LineBreak() {
    Content(
        raw = System.lineSeparator()
    )
}

/**
 * Empty function, does not add anything to the document.
 */
@LaTeX
suspend fun Empty() {
}
