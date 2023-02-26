package com.alex.eyk.kotex.util

/**
 * The operator allows to create the following code:
 * ```
 *  fun a() { }
 *
 *  fun b() { }
 *
 *  a() + b()
 * ```
 * As a result of the code execution, the function a and b will be executed,
 * nothing else will happen.
 *
 * Such code will help when you need to write several small @LaTeX functions
 * in one line. For example, two equivalent options are given. First:
 * ```
 *  Text("2x + ") + Frac(3, 4) + Text(" = 7")
 * ```
 * Second:
 * ```
 *  Text("2x + ")
 *  Frac(3, 4)
 *  Text(" = 7")
 * ```
 */
operator fun Unit.plus(
    @Suppress("UNUSED_PARAMETER") unit: Unit
) {
    return
}
