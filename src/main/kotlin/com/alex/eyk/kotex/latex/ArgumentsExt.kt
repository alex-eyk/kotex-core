package com.alex.eyk.kotex.latex

import com.alex.eyk.kotex.ext.removeLast

fun String.withBracesOrEmpty(): String {
    return if (this.isEmpty()) "" else "{$this}"
}

fun String.asOption(): String {
    return if (this.isEmpty()) "" else "[$this]"
}

fun Int.asOption(): String {
    return "[$this]"
}

fun List<String>.asOptionsString(): String {
    if (this.isEmpty()) {
        return ""
    }
    val optionsBuilder = StringBuilder("[")
    this.forEach {
        optionsBuilder.append(it)
            .append(",")
    }
    optionsBuilder.removeLast(offset = 1)
    return "$optionsBuilder]"
}

fun List<String>.asAdditionalOptionsString(): String {
    return this.wrapEachWith(
        start = "[",
        end = "]"
    )
}

fun List<String>.asAdditionalArgumentsString(): String {
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
