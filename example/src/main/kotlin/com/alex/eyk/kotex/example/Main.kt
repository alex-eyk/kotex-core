package com.alex.eyk.kotex.example

import com.alex.eyk.kotex.compiler.PdfLaTexCompiler
import com.alex.eyk.kotex.example.task.TransportTask
import com.alex.eyk.kotex.util.PathUtils

suspend fun main() {
    val doc = TransportTaskDocument(
        name = "transport-task",
        path = PathUtils.getJarDirectoryPath(TransportTaskDocument::class.java),
        task = TransportTask(
            costs = listOf(
                listOf(3, 3, 1),
                listOf(9, 2, 2),
                listOf(5, 7, 6)
            ),
            resources = listOf(40, 60, 50),
            needs = listOf(30, 30, 40)
        )
    ).apply {
        solveTask()
    }
    val compiler = PdfLaTexCompiler(
        tempPath = PathUtils.getJarDirectoryPath(TransportTaskDocument::class.java)
    )
    compiler.compile(doc)
    doc.close()
}
