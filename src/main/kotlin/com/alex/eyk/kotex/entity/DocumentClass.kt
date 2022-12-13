package com.alex.eyk.kotex.entity

data class DocumentClass(
    val name: String,
    val options: Set<String> = emptySet()
) {

    companion object {

        const val ARTICLE = "article"
        const val BOOK = "book"
        const val REPORT = "report"
        const val LETTER = "letter"
        const val SLIDES = "slides"
        const val PROC = "proc"
        const val LTXDOC = "ltxdoc"
        const val LTXGUIDE = "ltxguide"
        const val LTNEWS = "ltnews"
        const val MINIMAL = "minimal"
    }
}
