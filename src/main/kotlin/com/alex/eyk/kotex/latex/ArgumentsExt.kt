package com.alex.eyk.kotex.latex

import com.alex.eyk.kotex.ext.removeLast

internal fun String.withBracesOrEmpty(): String {
    return if (this.isEmpty()) "" else "{$this}"
}

internal fun String.asOption(): String {
    return if (this.isEmpty()) "" else "[$this]"
}

internal fun Int.asOption(): String {
    return "[$this]"
}

internal fun List<String>.asOptionsString(): String {
    if (this.isEmpty()) {
        return ""
    }
    val optionsBuilder = StringBuilder("[")
    this.forEach {
        optionsBuilder.append(it)
            .append(", ")
    }
    optionsBuilder.removeLast(offset = 2)
    return "$optionsBuilder]"
}

internal fun List<String>.asAdditionalOptionsString(): String {
    return this.wrapEachWith(
        start = "[",
        end = "]"
    )
}

internal fun List<String>.asAdditionalArgumentsString(): String {
    return this.wrapEachWith(
        start = "{",
        end = "}"
    )
}

private fun List<String>.wrapEachWith(
    start: String,
    end: String
): String {
    if (this.isEmpty()) {
        return ""
    }
    val wrappedBuilder = StringBuilder()
    this.forEach {
        wrappedBuilder.append("$start$it$end")
    }
    return wrappedBuilder.toString()
}
