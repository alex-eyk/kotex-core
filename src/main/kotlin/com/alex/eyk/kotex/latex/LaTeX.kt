package com.alex.eyk.kotex.latex

/**
 * [LaTeX] annotation should apply to a function or lambda that converts
 * the input and adds result to the LaTeX document.
 */
@Retention(AnnotationRetention.SOURCE)
@Target(
    allowedTargets = [
        AnnotationTarget.FUNCTION,
        AnnotationTarget.TYPE
    ]
)
annotation class LaTeX
