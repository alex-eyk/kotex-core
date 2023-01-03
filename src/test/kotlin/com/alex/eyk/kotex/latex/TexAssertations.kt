package com.alex.eyk.kotex.latex

import com.alex.eyk.kotex.document.BaseDocument
import kotlinx.coroutines.runBlocking
import java.io.File

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

private fun readContent(
    files: Iterable<File>
): Map<String, String> {
    val contentMap = HashMap<String, String>()
    files.forEach { file ->
        val contentBuilder = StringBuilder()
        file.inputStream()
            .bufferedReader()
            .use { reader ->
                contentBuilder.append(reader.readLine())
            }
        val tag = file.name.removeSuffix(".tex")
        contentMap[tag] = contentBuilder.toString()
    }
    return contentMap
}
