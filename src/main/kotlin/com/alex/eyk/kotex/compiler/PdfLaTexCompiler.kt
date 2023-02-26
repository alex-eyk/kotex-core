package com.alex.eyk.kotex.compiler

import com.alex.eyk.kotex.document.AbstractDocument
import com.alex.eyk.kotex.util.PathUtils.getJarDirectoryPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import kotlin.io.path.createFile
import kotlin.io.path.exists

class PdfLaTexCompiler(
    private val tempPath: String = getJarDirectoryPath(),
    private val outputPath: String = "$tempPath/",
) : PdfCompiler {

    private val outputDirectory: File = File(outputPath)

    init {
        if (!outputDirectory.isDirectory || !outputDirectory.exists()) {
            throw IllegalArgumentException(
                "Argument `outputPath`: {$outputPath} should be directory " +
                        "or not exists"
            )
        }
    }

    override suspend fun compile(
        document: AbstractDocument<Iterable<File>>
    ): Path {
        val primary = getPrimaryFile(document)
        when (
            val result = "pdflatex -halt-on-error -file-line-error ${primary.name}"
                .execute(primary.parentFile)
        ) {
            is CommandResult.Completed -> {
                if (!isResultSuccess(result.output)) {
                    throw IllegalStateException("Unable to compile document, output: ${result.output}")
                }
                val source = Paths.get("${tempPath}/${document.name}/temp/${document.name}.pdf")
                val out = Paths.get("$outputPath/${document.name}/${document.name}.pdf")
                return withContext(Dispatchers.IO) {
                    if (!out.exists()) {
                        out.createFile()
                    }
                    Files.move(source, out, StandardCopyOption.REPLACE_EXISTING)
                    return@withContext out
                }
            }

            is CommandResult.Failure -> {
                throw IllegalStateException("Unable to compile pdf", result.e)
            }
        }
    }

    private fun isResultSuccess(
        output: String
    ): Boolean {
        return output.contains("Output written")
    }

    private suspend fun getPrimaryFile(
        document: AbstractDocument<Iterable<File>>,
    ): File {
        document.getContent().forEach {
            if (it.name == "${document.name}.tex") {
                return it
            }
        }
        throw IllegalStateException(
            "No one file with name ${document.name}.tex found."
        )
    }
}
