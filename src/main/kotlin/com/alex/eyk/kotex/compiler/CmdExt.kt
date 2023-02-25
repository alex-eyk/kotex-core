package com.alex.eyk.kotex.compiler

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

internal suspend fun String.execute(
    workingDir: File,
    timeout: Long = -1L,
    timeUnit: TimeUnit = TimeUnit.MINUTES
): CommandResult {
    val parts = this.split(" ")
    val process: Process
    val builder = ProcessBuilder(*parts.toTypedArray())
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
    return withContext(Dispatchers.IO) {
        try {
            process = builder.start().apply {
                if (timeout != -1L) {
                    waitFor(timeout, timeUnit)
                }
            }
            return@withContext CommandResult.Completed(
                output = process.inputStream.bufferedReader().readText()
            )
        } catch (e: IOException) {
            return@withContext CommandResult.Failure(e)
        }
    }
}

internal sealed class CommandResult {

    data class Completed(
        val output: String
    ) : CommandResult()

    data class Failure(
        val e: Throwable
    ) : CommandResult()
}
