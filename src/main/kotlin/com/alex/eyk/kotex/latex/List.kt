@file:Suppress("REDUNDANT_INLINE_SUSPEND_FUNCTION_TYPE")

package com.alex.eyk.kotex.latex

import com.alex.eyk.kotex.latex.ListType.DESCRIPTION
import com.alex.eyk.kotex.latex.ListType.ENUMERATE
import com.alex.eyk.kotex.latex.ListType.ITEMIZE
import com.alex.eyk.kotex.util.plus

enum class ListType(
    val environment: String
) {

    /**
     * Unordered list. Elements are highlighted with bold dots if no label is
     * specified for the element.
     */
    ITEMIZE("itemize"),

    /**
     * Ordered list. Elements are identified using serial numbers. The element
     * with the label is not included in the count.
     */
    ENUMERATE("enumerate"),

    /**
     * Unordered list. Elements are highlighted with labels.
     */
    DESCRIPTION("description")
}

/**
 * An alternative way to create a list, calls the [List] function with a
 * parameter `type = DESCRIPTION`.
 *
 * @param elements Elements of the original iterable structure.
 * @param map A function that map an element of an iterable structure to
 * [LaTeX].
 */
@LaTeX
suspend inline fun <E> Description(
    elements: Iterable<E>,
    map: @LaTeX (element: E) -> Unit
) {
    List(
        type = DESCRIPTION,
        elements,
        map
    )
}

/**
 * An alternative way to create a list, calls the [List] function with a
 * parameter `type = ENUMERATE`.
 *
 * @param elements Elements of the original iterable structure.
 * @param map A function that map an element of an iterable structure to
 * [LaTeX].
 */
@LaTeX
suspend inline fun <E> Enumerate(
    elements: Iterable<E>,
    map: @LaTeX (element: E) -> Unit
) {
    List(
        type = ENUMERATE,
        elements,
        map
    )
}

/**
 * An alternative way to create a list, calls the [List] function with a
 * parameter `type = ITEMIZE`.
 *
 * @param elements Elements of the original iterable structure.
 * @param map A function that map an element of an iterable structure to
 * [LaTeX].
 */
@LaTeX
suspend inline fun <E> Itemize(
    elements: Iterable<E>,
    map: @LaTeX (element: E) -> Unit
) {
    List(
        type = ITEMIZE,
        elements,
        map
    )
}

/**
 * An alternative way to create a list. If the list must be created from some
 * iterable data structure, then it is enough to pass it and the function of
 * converting the elements of the iterable data structure to [LaTeX].
 *
 * For example, two equal cases:
 * ```
 *  Itemize {
 *      Item {
 *          Text("First item")
 *      }
 *      Item {
 *          Text("Second item")
 *      }
 *  }
 * ```
 * and
 * ```
 *  val list = listOf("First item", "Second item")
 *  List(
 *      type = ListType.ITEMIZE,
 *      elements = list,
 *      map = { Text(it) }
 *  )
 * ```
 *
 * @param type Type of the list.
 * @param elements Elements of the original iterable structure.
 * @param map A function that map an element of an iterable structure to
 * [LaTeX].
 */
suspend inline fun <E> List(
    type: ListType,
    elements: Iterable<E>,
    map: @LaTeX suspend (element: E) -> Unit
) {
    Environment(
        name = type.environment,
    ) {
        elements.forEach {
            map(it)
        }
    }
}

/**
 * Unordered list environment, but labels are used instead of dots if they
 * are specified. List items are added using the [Item] function.
 *
 * @param content Content of the list.
 */
@LaTeX
suspend inline fun Description(
    content: @LaTeX suspend () -> Unit
) {
    Environment(
        name = DESCRIPTION.environment,
        content = content
    )
}

/**
 * Ordered list environment. Elements are identified using serial numbers. The
 * element with the label is not included in the count.The element List items
 * are added using the [Item] function.
 *
 * @param content Content of the list.
 */
@LaTeX
suspend inline fun Enumerate(
    content: @LaTeX suspend () -> Unit
) {
    Environment(
        name = ENUMERATE.environment,
        content = content
    )
}

/**
 * Unordered list environment. Elements are highlighted with bold dots if no
 * label is specified for the element. List items are added using the [Item]
 * function.
 *
 * @param content Content of the list.
 */
@LaTeX
suspend inline fun Itemize(
    content: @LaTeX suspend () -> Unit
) {
    Environment(
        name = ITEMIZE.environment,
        content = content
    )
}

/**
 * An element of an ordered or unordered list, can contain any content. A list
 * element can also have a label, this works for all types of lists, but it's
 * better to use it with a description list.
 *
 * @param label Text label for the item.
 * @param content Content of the list element.
 */
@LaTeX
suspend inline fun Item(
    label: String,
    content: @LaTeX suspend () -> Unit
) {
    Item(label = { Text(label) }, content)
}

/**
 * An element of an ordered or unordered list with with any possible label.
 * The label can also be empty.
 *
 * @param label Label for the item.
 * @param content Content of the list element.
 */
@LaTeX
suspend inline fun Item(
    label: @LaTeX suspend () -> Unit,
    content: @LaTeX suspend () -> Unit
) {
    Command(name = "item")
    BracketsWrapped(label)
    Space()
    content()
    LineBreak()
}

/**
 * An element of an ordered or unordered list without label.
 *
 * @param content Content of the list element.
 */
@LaTeX
suspend inline fun Item(
    content: @LaTeX suspend () -> Unit
) {
    Command(name = "item") + ` `() + content() + LineBreak()
}
