package com.alex.eyk.kotex.latex

import com.alex.eyk.kotex.entity.Package

/**
 * [Position] defines the vertical position of some element.
 */
enum class Position(
    val parameter: String,
    val requiresPackage: Package? = null
) {

    /**
     * Position is *here*, i.e., approximately at the same point it occurs
     * in the source text.
     */
    HERE(parameter = "h"),

    /**
     * Position at the top of the page.
     */
    TOP(parameter = "t"),

    /**
     * Position at the bottom of the page.
     */
    BOTTOM(parameter = "b"),

    /**
     * Put on a special page for floats only.
     */
    PAGE(parameter = "p"),

    /**
     * Position at precisely the location in the LATEX code. Requires the
     * `float` package.
     */
    HERE_STRICT(
        parameter = "H",
        requiresPackage = Package(
            name = "float"
        )
    )
}
