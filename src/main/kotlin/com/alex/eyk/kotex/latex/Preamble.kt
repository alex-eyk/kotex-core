package com.alex.eyk.kotex.latex

@Retention(AnnotationRetention.SOURCE)
@Target(
    allowedTargets = [
        AnnotationTarget.FUNCTION, AnnotationTarget.TYPE
    ]
)
annotation class Preamble
