package com.alex.eyk.kotex.latex

import com.alex.eyk.kotex.entity.Package
import com.alex.eyk.kotex.state.MultiState
import com.alex.eyk.kotex.state.TempFilesState
import com.alex.eyk.kotex.state.factory.CacheStateFactory
import com.alex.eyk.kotex.state.factory.StateFactory
import kotlinx.coroutines.CoroutineName.Key
import kotlinx.coroutines.currentCoroutineContext
import java.io.File

private val multiStateFactory: StateFactory<CharSequence, MultiState<Iterable<File>>> =
    CacheStateFactory()

/**
 * LaTeX processes the file as if its contents were inserted in the current
 * file.
 *
 * If filename does not end in ‘.tex’ then LaTeX first tries the filename
 * with that extension; this is the usual case. If filename ends with ‘.tex’
 * then LaTeX looks for the filename as it is.
 *
 * @param filename name of the file whose contents will be included in the
 * document.
 */
@LaTeX
suspend fun Input(
    filename: CharSequence
) {
    Content(
        raw = "\\input{$filename}" + System.lineSeparator()
    )
}

/**
 * A function that adds a declaration of the use of an external packages to
 * the beginning of the [LaTeX] document.
 *
 * @param packages list of external packages to be used in the document.
 */
@LaTeX
suspend fun DeclareExternalPackages(
    vararg packages: Package
) {
    packages.forEach {
        DeclareExternalPackage(it)
    }
}

/**
 * A function that adds a declaration of the use of an external package to
 * the beginning of the [LaTeX] document.
 *
 * @param package external package to be used in the document.
 */
@LaTeX
suspend fun DeclareExternalPackage(
    `package`: Package
) {
    if (documentState().isPackageUsed(`package`)) {
        return
    }
    forTag(TempFilesState.PREAMBLE) {
        UsePackage(`package`)
    }
}

/**
 * Command that allows to use external packages that extend the capabilities
 * of LaTeX. Should be in the preamble of the document.
 *
 * @param packages list of external packages to be used in the document.
 */
@LaTeX
suspend fun UsePackages(
    packages: List<Package>
) {
    packages.forEach {
        UsePackage(it)
    }
}

/**
 * Command that allows to use external package that extend the capabilities
 * of LaTeX. Should be in the preamble of the document.
 *
 * @param package external package to be used in the document.
 */
@LaTeX
suspend fun UsePackage(
    `package`: Package
) {
    documentState()
        .addPackage(`package`)
    Content(
        raw = "\\usepackage${`package`.options.asOptionsString()}{${`package`.name}}" +
                System.lineSeparator()
    )
}

/**
 * Extension for objects, used to add content from `toString()` method to the
 * document unchanged.
 */
@LaTeX
suspend fun <E> E.asContent() {
    Content(raw = this.toString())
}

/**
 * Every [LaTeX] function eventually necessarily calls this function to add
 * relevant content to the document. Thus, a function annotated with [LaTeX]
 * annotation converts input data into LaTeX content and calls that function.
 *
 * When the function is called, the content is added to some part of the
 * final document. This part is set using a tag.
 *
 * [LaTeX] function must be called from a coroutine whose name is equal to
 * the name of the currently edited document. Accordingly, each [LaTeX]
 * function must contain a `suspend` modifier.
 *
 * @param raw [LaTeX] raw content to be added to the current document.
 */
@LaTeX
suspend fun Content(
    raw: CharSequence
) {
    documentState()
        .append(raw)
}

/**
 * Function that adds content to the [LaTeX] document for the specified tag.
 * After adding this content, the tag will become the same as it was.
 *
 * @param tag tag that defines where the content will be added.
 * @param content content to be added for the specified tag.
 */
internal suspend inline fun forTag(
    tag: CharSequence,
    content: @LaTeX () -> Unit
) {
    val was = currentTag()
    setTag(tag)
    content()
    setTag(was)
}

/**
 * Setting the tag allows you to set the place in the document where the
 * content will be added: when [LaTeX] function is called, the content is
 * added to some part of the final document, which is set using a tag.
 *
 * Content related to a specific tag will be located in the place where the
 * tag first appeared.
 *
 * This approach allows, for example, to add the import of external packages
 * to the preamble of the document, after declaring the corresponding tag at
 * the beginning of the document.
 *
 * @param tag tag that defines where the content will be added.
 */
internal suspend fun setTag(
    tag: CharSequence
) {
    documentState()
        .setTag(tag)
}

/**
 * Function creates a new state of the document with the given name, which
 * will later be changed and supplemented with content.
 *
 * @param name name of the document without extensions. It must be unique.
 */
internal fun registerDocument(
    name: String
) {
    assertName(name)
    multiStateFactory.put(
        name, TempFilesState(name)
    )
}

/**
 * A function that removes the document's state.
 *
 * @param name of the document without extensions.
 */
internal fun removeDocument(
    name: String
) {
    if (multiStateFactory.contains(key = name)) {
        multiStateFactory.remove(name)
    }
}

internal suspend fun currentTag(): String {
    return documentState()
        .getTag()
        .toString()
}

internal suspend fun documentContent(): Iterable<File> {
    return documentState()
        .getContent()
}

internal suspend fun documentState(): MultiState<Iterable<File>> {
    val key = currentCoroutineContext()[Key]
    key?.let {
        try {
            return multiStateFactory.get(it.name)
        } catch (e: NoSuchElementException) {
            throw IllegalStateException(
                "The content state does not exist, it was probably deleted after the document was closed.",
                e
            )
        }
    } ?: throw IllegalStateException(
        """
            Unable to get document name from coroutine context. Check that @LaTeX functions
            are called only in the correct context and `registerDocument` function was called
        """.trimIndent()
    )
}

private fun assertName(
    name: String
) {
    if (multiStateFactory.contains(key = name)) {
        throw IllegalArgumentException(
            "Document or block with name: $name is already exists."
        )
    }
}
