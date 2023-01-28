package com.alex.eyk.kotex.ext

private const val START_INDEX = 0

private const val OPEN_BRACE = "{"
private const val CLOSE_BRACE = "}"

internal fun StringBuilder.removeLast(
    offset: Int
): StringBuilder = apply {
    this.delete(
        this.length - offset,
        this.length
    )
}

internal fun StringBuilder.insert(
    content: String
): StringBuilder = apply {
    this.insert(
        START_INDEX,
        content
    )
}

internal fun StringBuilder.wrapWithBrace(): StringBuilder = apply {
    this.wrapWith(
        start = OPEN_BRACE,
        end = CLOSE_BRACE
    )
}

internal fun StringBuilder.wrapWith(
    start: String,
    end: String
) = apply {
    this.insert(
        START_INDEX,
        start
    )
    this.append(end)
}

internal fun StringBuilder.breakLine(): StringBuilder = apply {
    this.append(
        System.lineSeparator()
    )
}
