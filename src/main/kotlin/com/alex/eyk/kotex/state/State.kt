package com.alex.eyk.kotex.state

import com.alex.eyk.kotex.entity.Package
import java.io.Closeable

interface State<T> : Closeable {

    fun addPackage(
        `package`: Package
    )

    fun isPackageUsed(
        `package`: Package
    ): Boolean

    fun append(
        rawContent: CharSequence
    )

    fun getContent(): T
}
