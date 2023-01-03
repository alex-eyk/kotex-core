package com.alex.eyk.kotex.util

import java.io.File

object FileUtils {

    fun deleteWithBackup(file: File) {
        if (file.exists()) {
            val copy = File("${file.absolutePath}.bak")
            file.copyTo(
                target = copy,
                overwrite = true
            )
            file.delete().assertSuccess()
        }
    }
}
