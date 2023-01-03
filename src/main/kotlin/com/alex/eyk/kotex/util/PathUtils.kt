package com.alex.eyk.kotex.util

import java.io.File
import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL

object PathUtils {

    private const val CLASS_EXTENSION = ".class"

    private val JAR_PATH_REGEX = Regex(pattern = "file:[A-Za-z]:.*")

    fun getJarDirectoryPath(
        class_: Class<*> = PathUtils.javaClass
    ): String {
        val jarLocation = getJarLocation(class_)
        val file = urlToFile(jarLocation)
        return if (file.name.endsWith("jar")) {
            file.parent
        } else {
            file.path
        }
    }

    private fun getJarLocation(
        class_: Class<*>
    ): URL {
        class_
            .protectionDomain
            .codeSource
            .location?.let {
                return it
            }
        val classResources = class_.getResource(
            class_.simpleName + CLASS_EXTENSION
        ) ?: throw IllegalStateException(
            "Unable to get jar path: class resource has null reference"
        )
        val pathWithoutSuffix = removeJarSuffix(
            path = classResources.toString(),
            canonicalName = class_.canonicalName
        )
        val path = removeJarPrefix(pathWithoutSuffix)
        try {
            return URL(path)
        } catch (e: MalformedURLException) {
            throw IllegalStateException("Unable to get .jar path", e)
        }
    }

    private fun removeJarSuffix(
        path: String,
        canonicalName: String
    ): String {
        val suffix = canonicalName.replace(".", "/") +
                CLASS_EXTENSION
        if (!path.endsWith(suffix)) {
            throw IllegalStateException(
                "Unable to get jar path: illegal path suffix, " +
                        "path: `$path`, suffix: `$suffix`."
            )
        }
        return path.substring(0, path.length - suffix.length)
    }

    private fun removeJarPrefix(
        path: String
    ): String {
        return if (path.startsWith("jar:")) {
            path.substring(4, path.length - 2)
        } else {
            path
        }
    }

    private fun urlToFile(
        url: URL
    ): File {
        return urlToFile(url.toString())
    }

    private fun urlToFile(
        url: String
    ): File {
        var path = url
        if (path.startsWith("jar:")) {
            val suffixIndex = path.indexOf("!/")
            if (suffixIndex == -1) {
                throw IllegalStateException("Suffix not found")
            }
            path = path.substring(4, suffixIndex)
        }
        try {
            if (isWindows() && path.matches(JAR_PATH_REGEX)) {
                path = "file:/" + path.substring(5)
            }
            return File(URL(path).toURI())
        } catch (e: Exception) {
            when (e) {
                is MalformedURLException,
                is URISyntaxException -> {
                    if (path.startsWith("file:")) {
                        path = path.substring(5)
                        return File(path)
                    }
                }
                else -> throw e
            }
        }
        throw IllegalArgumentException("Invalid URL: $url")
    }

    private fun isWindows(): Boolean {
        return System.getProperty("os.name")
            .startsWith("Windows")
    }
}
