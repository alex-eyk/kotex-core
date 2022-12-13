package com.alex.eyk.kotex.entity

data class Package(
    val name: CharSequence,
    val options: List<String> = emptyList()
) {

    companion object {

        /**
         * Advanced mathematical typesetting from the American Mathematical
         * Society. This includes the amsmath package; it provides many
         * commands for typesetting mathematical formulas of higher complexity.
         */
        const val AMSLATEX = "amslatex"

        /**
         * Package and related files support typesetting in many languages.
         */
        const val BABEL = "babel"

        /**
         * The package provides tools for typesetting with Cyrillic fonts
         * (except the fonts themselves).
         */
        const val CYRILLIC = "cyrillic"

        /**
         * Package provides support for the inclusion and transformation of
         * graphics, including files produced by other software. Also
         * included, is the color package which provides support for
         * typesetting in colour.
         */
        const val GRAPHICS = "graphics"

        /**
         * The package provides tools for typesetting with a large range of
         * Type 1 (PostScript) fonts.
         */
        const val PSNFSS = "psnfss"
    }
}
