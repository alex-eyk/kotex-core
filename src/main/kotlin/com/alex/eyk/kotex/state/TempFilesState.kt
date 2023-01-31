package com.alex.eyk.kotex.state

import com.alex.eyk.kotex.util.FileUtils
import com.alex.eyk.kotex.util.PathUtils.getJarDirectoryPath
import com.alex.eyk.kotex.ext.assertSuccess
import java.io.File
import java.io.FileWriter
import java.io.Writer

internal class TempFilesState(
    name: String,
    path: String = getJarDirectoryPath(),
    private val deleteSourceOnClose: Boolean = false
) : MultiState<Iterable<File>>, FileState<Iterable<File>> {

    companion object {

        private const val TEMP_DIRECTORY = "temp"

        internal const val PREAMBLE = "preamble"
    }

    private val tempPath = "$path/$name/$TEMP_DIRECTORY"

    private val writersCache: MutableMap<CharSequence, Writer> = HashMap()
    private val files: MutableMap<CharSequence, File> = HashMap()

    private var tag: CharSequence = name

    init {
        val directory = File(tempPath)
        if (!directory.exists()) {
            directory.mkdirs().assertSuccess()
        }
        addTagIfNecessary(PREAMBLE)
        addTagIfNecessary(name)
    }

    override fun append(
        rawContent: CharSequence,
    ) {
        writersCache[tag]?.let {
            it.write(rawContent.toString())
            it.flush()
        }
    }

    override fun getTag() = tag

    override fun setTag(
        tag: CharSequence
    ) {
        addTagIfNecessary(tag)
        this.tag = tag
    }

    override fun getContent(): Iterable<File> {
        return files.values
    }

    override fun close() {
        if (deleteSourceOnClose) {
            files.values.forEach {
                it.delete()
            }
        }
        writersCache.values.forEach {
            it.close()
        }
    }

    private fun addTagIfNecessary(
        tag: CharSequence
    ) {
        if (!files.contains(tag)) {
            createFile(tag)
        }
        if (!writersCache.contains(tag)) {
            val file = files[tag] ?: throw NoSuchElementException()
            writersCache[tag] = FileWriter(file)
        }
    }

    private fun createFile(
        tag: CharSequence
    ) {
        val path = "$tempPath/$tag.tex"
        val file = File(path)
        if (file.exists()) {
            FileUtils.deleteWithBackup(file)
        }
        file.createNewFile().assertSuccess()
        this.files[tag] = file
    }
}
