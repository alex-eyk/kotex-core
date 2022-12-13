package com.alex.eyk.kotex.latex

/**
 * Annotation, which should be noted the functions responsible for the
 * creation of latex content.
 */
@Retention(AnnotationRetention.SOURCE)
@Target(
    allowedTargets = [
        AnnotationTarget.FUNCTION, AnnotationTarget.TYPE
    ]
)
annotation class LaTeX
