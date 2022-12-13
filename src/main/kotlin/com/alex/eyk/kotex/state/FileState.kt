package com.alex.eyk.kotex.state

import java.io.Closeable

interface FileState<T> : State<T>, Closeable
