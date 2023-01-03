package com.alex.eyk.kotex.latex

import com.alex.eyk.kotex.assertEquals
import org.junit.jupiter.api.Test

internal class BaseTest {

    @Test
    fun rawContentTest() {
        assertLaTeX(
            content = {
                RawContent(
                    content = "Test text"
                )
            },
            assert = {
                assertEquals(
                    expected = 2,
                    actual = it.size
                )
                assertEquals(
                    expected = "Test text",
                    actual = it["document"]
                )
            }
        )
    }
}
