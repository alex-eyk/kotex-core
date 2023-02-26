package com.alex.eyk.kotex.example.task

data class TransportTask(
    val costs: List<List<Int>>,
    val resources: List<Int>,
    val needs: List<Int>
)
