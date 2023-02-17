package com.alex.eyk.kotex.util

import com.alex.eyk.kotex.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MatrixFactoryTest {

    @Test
    fun mutableMatrixTest() {
        val expected = mutableListOf(
            mutableListOf(1, 2, 3),
            mutableListOf(4, 5, 6),
            mutableListOf(7, 8, 9)
        )
        val actual = MutableMatrix(
            height = 3,
            width = 3,
            initial = { i, j ->
                i * 3 + (j + 1)
            }
        )
        assertEquals(expected, actual)
    }

    @Test
    fun mutableMatrixWrongArgumentTest() {
        assertThrows<IllegalArgumentException> {
            MutableMatrix(height = 0, width = -1) { _, _ -> 0 }
        }
    }

    @Test
    fun mutableMatrixCopyTest() {
        val matrix = listOf(
            listOf(1, 2, 3),
            listOf(2, 3, 4)
        )
        val expected = mutableListOf(
            mutableListOf(1, 2, 3),
            mutableListOf(2, 3, 4)
        )
        val actual = MutableMatrix(matrix)
        assertEquals(expected, actual)
    }

    @Test
    fun mutableMatrixEmptyCopyTest() {
        val expected = mutableListOf<List<Int>>()
        val actual = MutableMatrix(emptyList<List<Int>>())
        assertEquals(expected, actual)
    }
}
