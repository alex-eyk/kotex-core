package com.alex.eyk.kotex.document

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class BaseDocumentTest {

    private lateinit var document: BaseDocument

    @Test
    fun createTest() {
        BaseDocument("name").use { }
    }

    @Test
    fun createWithEqualNamesTest() {
        val create = { BaseDocument("document") }
        create()
        assertThrows<IllegalArgumentException> {
            create()
        }
    }
}
