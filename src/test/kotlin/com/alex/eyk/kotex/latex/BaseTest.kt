package com.alex.eyk.kotex.latex

import com.alex.eyk.kotex.assertEquals
import org.junit.jupiter.api.Test

internal class BaseTest {

    @Test
    fun rawContentTest() {
        assertLaTeX(
            content = {
                Content(
                    raw = "Test text"
                )
            },
            assert = {
                assertEquals(
                    expected = 2,
                    actual = it.size
                )
                assertEquals(
                    expected = "\\input{preamble}\n" +
                            "Test text",
                    actual = it["document"]
                )
            }
        )
    }
}
