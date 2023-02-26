package com.alex.eyk.kotex.example.task

data class TransportTaskSnapshot(
    val condition: TransportTask,
    val firstPass: Boolean = false,
    val costs: List<List<Int>>?,
    val resources: List<Int>,
    val needs: List<Int>,
    val plan: List<List<Int>>?,
    val totalCosts: Int?
)
