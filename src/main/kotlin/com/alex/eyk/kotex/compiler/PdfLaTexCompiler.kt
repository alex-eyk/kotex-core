package com.alex.eyk.kotex.compiler

import com.alex.eyk.kotex.document.AbstractDocument
import com.alex.eyk.kotex.util.FileUtils
import com.alex.eyk.kotex.util.PathUtils.getJarDirectoryPath
import com.alex.eyk.kotex.util.assertSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

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
    ): File {
        val primary = getPrimaryFile(document)
        when (
            val result = "pdflatex ${primary.name}".execute(primary.parentFile)
        ) {
            is CommandResult.Success -> {
                val source = File("${tempPath}/${document.name}/temp/${document.name}.pdf")
                val out = File("$outputPath/${document.name}/${document.name}.pdf")
                withContext(Dispatchers.IO) {
                    if (out.exists()) {
                        FileUtils.deleteWithBackup(out)
                    }
                    out.createNewFile().assertSuccess()
                    source.copyTo(out, overwrite = true)
                    source.delete()
                }
                return out
            }
            is CommandResult.Failure -> {
                throw IllegalStateException("Unable to compile pdf", result.e)
            }
        }
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
