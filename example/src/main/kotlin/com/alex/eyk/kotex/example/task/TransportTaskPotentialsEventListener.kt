package com.alex.eyk.kotex.example.task

interface TransportTaskPotentialsEventListener {

    fun onStart(
        condition: TransportTask
    )

    fun onConvertedToClosed(
        snapshot: TransportTaskSnapshot,
        addedColumn: Boolean,
        oldResources: Int,
        oldNeeds: Int
    )

    fun onStartPlanBuilt(
        snapshot: TransportTaskSnapshot
    )

    fun onDegenerateTaskFixed(
        snapshot: TransportTaskSnapshot
    )

    fun onPotentialsCalculated(
        snapshot: TransportTaskSnapshot,
        ui: List<Int>,
        vj: List<Int>
    )

    fun onValidatePotentials(
        snapshot: TransportTaskSnapshot,
        potentialsSumMatrix: List<List<Int>>,
        potentialsSumSubtractCostsMatrix: List<List<Int>>
    )

    fun onFoundOptimalSolve(
        snapshot: TransportTaskSnapshot
    )

    fun onPlanRebuilt(
        snapshot: TransportTaskSnapshot,
        rebuildCycle: List<Pair<Int, Int>>,
        oldPlan: List<List<Int>>,
        ui: List<Int>,
        vj: List<Int>
    )
}