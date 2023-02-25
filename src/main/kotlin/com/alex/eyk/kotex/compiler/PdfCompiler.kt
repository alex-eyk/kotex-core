package com.alex.eyk.kotex.compiler

import com.alex.eyk.kotex.document.AbstractDocument
import java.io.File
import java.nio.file.Path

interface PdfCompiler {

    suspend fun compile(document: AbstractDocument<Iterable<File>>): Path
}
