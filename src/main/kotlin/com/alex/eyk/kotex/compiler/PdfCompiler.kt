package com.alex.eyk.kotex.compiler

import com.alex.eyk.kotex.document.AbstractDocument
import java.io.File

interface PdfCompiler {

    suspend fun compile(document: AbstractDocument<Iterable<File>>): File
}
