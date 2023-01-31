package com.alex.eyk.kotex.latex

import com.alex.eyk.kotex.assertEquals
import com.alex.eyk.kotex.document.BaseDocument
import kotlinx.coroutines.runBlocking
import java.io.File

fun assertWithoutPreamble(
    expected: String,
    actual: String?
) {
    assertEquals(
        expected = "\\input{preamble}" +
                System.lineSeparator() +
                expected,
        actual = actual
    )
}

fun assertLaTeX(
    content: @LaTeX suspend () -> Unit,
    assert: (documentContent: Map<String, String>) -> Unit
) {
    val files: Iterable<File>
    BaseDocument("document").use {
        it.append(content)
        runBlocking {
            files = it.getContent()
        }
    }
    assert(readContent(files))
}

fun assertLaTeX(
    expected: String,
    content: @LaTeX suspend () -> Unit
) {
    assertLaTeX(content = content) {
        assertWithoutPreamble(
            expected = expected,
            actual = it["document"]
        )
    }
}

private fun readContent(
    files: Iterable<File>
): Map<String, String> {
    val contentMap = HashMap<String, String>()
    files.forEach { file ->
        file.inputStream()
            .bufferedReader()
            .use { reader ->
                val tag = file.name.removeSuffix(".tex")
                contentMap[tag] = reader.readText()
            }
    }
    return contentMap
}
