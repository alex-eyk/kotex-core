package com.alex.eyk.kotex.example.task

import com.alex.eyk.kotex.example.copy
import com.alex.eyk.kotex.example.deepcopy
import com.alex.eyk.kotex.example.matrixOf
import com.alex.eyk.kotex.example.sum
import kotlin.math.abs
import kotlin.math.min

class PotentialsTransportTaskSolver(
    private val condition: TransportTask,
    private val eventListener: TransportTaskPotentialsEventListener
) : TransportTaskSolver() {

    private var costs: MutableList<MutableList<Int>> = condition.costs.deepcopy()
    private var resources: MutableList<Int> = condition.resources.copy()
    private var needs: MutableList<Int> = condition.needs.copy()

    private lateinit var plan: MutableList<MutableList<Int>>
    private var firstPass = true
    private var totalCosts: Int = Int.MAX_VALUE

    private lateinit var ui: List<Int>
    private lateinit var vj: List<Int>

    private lateinit var cycle: List<Pair<Int, Int>>

    private lateinit var potentialsMatrixSubtractCosts: List<List<Int>>

    override fun solve() {
        eventListener.onStart(condition)
        if (isOpenTask()) {
            toClosedTask()
        }
        calculateStartPlan()
        while (true) {
            calculatePotentials()
            calculatePotentialCosts()
            if (isOptimal()) {
                eventListener.onFoundOptimalSolve(makeSnapshot())
                break
            } else {
                val notOptimal = findNotOptimalIndex()
                findCycle(notOptimal)
                val oldPlan = this.plan
                rebuildPlan()
                eventListener.onPlanRebuilt(makeSnapshot(), cycle, oldPlan, ui, vj)
            }
            if (firstPass) {
                firstPass = false
            }
        }
    }

    private fun isOpenTask(): Boolean {
        return resources.sum() != needs.sum()
    }

    private fun toClosedTask() {
        val addedColumn: Boolean
        val totalResources = resources.sum()
        val totalNeeds = needs.sum()

        val diff = totalResources - totalNeeds
        if (diff > 0) {
            addedColumn = true
            needs.add(diff)
            costs.forEach { it.add(0) }
        } else {
            addedColumn = false
            resources.add(abs(diff))
            costs.add(com.alex.eyk.kotex.example.listOf(size = needs.size) { 0 })
        }
        eventListener.onConvertedToClosed(
            makeSnapshot(),
            addedColumn,
            oldResources = totalResources,
            oldNeeds = totalNeeds
        )
    }

    private fun calculateStartPlan() {
        val remainingResources = resources.copy()
        val remainingNeeds = needs.copy()
        val startPlan = matrixOf(
            height = costs.size,
            width = costs[0].size,
            init = { _, _ -> 0 }
        )
        for ((i, j) in getSortedIndexes()) {
            startPlan[i][j] = min(remainingResources[i], remainingNeeds[j])
            remainingResources[i] -= startPlan[i][j]
            remainingNeeds[j] -= startPlan[i][j]

            if (remainingNeeds[j] == 0) {
                for (k in startPlan.indices) {
                    if (startPlan[k][j] == 0) {
                        startPlan[k][j] = Int.MAX_VALUE
                    }
                }
            }
            if (remainingResources[i] == 0) {
                for (k in startPlan[0].indices) {
                    if (startPlan[i][k] == 0) {
                        startPlan[i][k] = Int.MAX_VALUE
                    }
                }
            }
            if (remainingNeeds.sum() == 0 && remainingResources.sum() == 0) {
                break
            }
        }
        plan = startPlan
        calculateTotalCosts()
        eventListener.onStartPlanBuilt(makeSnapshot())
    }

    private fun getSortedIndexes(): Iterable<Pair<Int, Int>> {
        if (costs.isEmpty()) {
            throw IllegalArgumentException("Costs matrix should not be empty")
        }
        return ArrayList<Pair<Int, Int>>().apply {
            for (i in costs.indices) {
                for (j in costs[0].indices) {
                    add(Pair(i, j))
                }
            }
            sortBy { costs[it.first][it.second] }
        }
    }

    private fun calculatePotentials() {
        val u = com.alex.eyk.kotex.example.listOf(size = resources.size) { Int.MAX_VALUE }
        val v = com.alex.eyk.kotex.example.listOf(size = needs.size) { Int.MAX_VALUE }
        u[0] = 1
        val fixedPlan = plan.deepcopy()
        val fixes = mutableListOf<Pair<Int, Int>>()
        var anyChanges = false

        while (u.count { it == Int.MAX_VALUE } != 0 || v.count { it == Int.MAX_VALUE } != 0) {
            for (i in resources.indices) {
                for (j in needs.indices) {
                    if (fixedPlan[i][j] != Int.MAX_VALUE) {
                        if (u[i] != Int.MAX_VALUE && v[j] == Int.MAX_VALUE) {
                            v[j] = costs[i][j] - u[i]
                            anyChanges = true
                        }
                        if (u[i] == Int.MAX_VALUE && v[j] != Int.MAX_VALUE) {
                            u[i] = costs[i][j] - v[j]
                            anyChanges = true
                        }
                    }
                }
            }
            if (!anyChanges) {
                res@ for (i in resources.indices) {
                    for (j in needs.indices) {
                        if (
                            fixedPlan[i][j] == Int.MAX_VALUE && (u[i] == Int.MAX_VALUE && v[j] != Int.MAX_VALUE) ||
                            u[i] != Int.MAX_VALUE && v[j] == Int.MAX_VALUE
                        ) {
                            fixedPlan[i][j] = 0
                            fixes.add(Pair(i, j))
                            break@res
                        }
                    }
                }
            }
            anyChanges = false
        }
        if (fixes.isNotEmpty()) {
            plan = fixedPlan
            calculateTotalCosts()
            eventListener.onDegenerateTaskFixed(makeSnapshot())
        }
        ui = u
        vj = v
        eventListener.onPotentialsCalculated(makeSnapshot(), u, v)
    }

    private fun calculatePotentialCosts() {
        val potentialMatrix = matrixOf(
            height = costs.size,
            width = costs[0].size,
        ) { _, _ -> 0 }
        for (i in potentialMatrix.indices) {
            for (j in potentialMatrix[0].indices) {
                potentialMatrix[i][j] = ui[i] + vj[j]
            }
        }
        val potentialsMatrixSubtractCosts = matrixOf(
            height = costs.size,
            width = costs[0].size,
        ) { _, _ -> 0 }
        for (i in potentialsMatrixSubtractCosts.indices) {
            for (j in potentialsMatrixSubtractCosts[0].indices) {
                potentialsMatrixSubtractCosts[i][j] = costs[i][j] - potentialMatrix[i][j]
            }
        }
        this.potentialsMatrixSubtractCosts = potentialsMatrixSubtractCosts
        eventListener.onValidatePotentials(
            makeSnapshot(),
            potentialMatrix,
            potentialsMatrixSubtractCosts
        )
    }

    private fun isOptimal(): Boolean {
        potentialsMatrixSubtractCosts.forEach { row ->
            row.forEach {
                if (it < 0) {
                    return false
                }
            }
        }
        return true
    }

    private fun findNotOptimalIndex(): Pair<Int, Int> {
        val matrix = potentialsMatrixSubtractCosts
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                if (matrix[i][j] < 0) {
                    return Pair(i, j)
                }
            }
        }
        throw IllegalArgumentException("No one negative value found.")
    }

    private fun findCycle(
        start: Pair<Int, Int>,
    ) {
        val plan = this.plan
        val (startI, startJ) = start
        val nextSteps = mutableListOf<Pair<Int, Int>>()
        for (i in plan.indices) {
            if (plan[i][startJ] != Int.MAX_VALUE && i != startI) {
                nextSteps.add(Pair(i, startJ))
            }
        }
        for (j in plan[0].indices) {
            if (plan[startI][j] != Int.MAX_VALUE && j != startJ) {
                nextSteps.add(Pair(startI, j))
            }
        }
        var result: List<Pair<Int, Int>>? = null
        for (step in nextSteps) {
            result = findCycle(
                start, from = start, to = step, path = mutableListOf(start)
            )
            if (result.isNotEmpty()) {
                break
            }
        }
        if (result == null) {
            result = listOf()
        }
        this.cycle = result
    }

    private fun findCycle(
        start: Pair<Int, Int>,
        from: Pair<Int, Int>,
        to: Pair<Int, Int>,
        path: MutableList<Pair<Int, Int>> = mutableListOf()
    ): List<Pair<Int, Int>> {
        val (startI, startJ) = start
        val (toI, toJ) = to
        val (fromI, fromJ) = from
        if (to == start) {
            return if (path.size % 2 == 0) {
                path
            } else {
                listOf()
            }
        }
        path.add(to)

        val iDiff = toI - fromI
        val jDiff = toJ - fromJ

        val nextSteps = mutableListOf<Pair<Int, Int>>()
        for (i in plan.indices) {
            if (iDiff != 0) {
                continue
            }
            nextSteps.add(Pair(i, toJ))
        }
        for (j in plan[0].indices) {
            if (jDiff != 0) {
                continue
            }
            nextSteps.add(Pair(toI, j))
        }
        val validatedSteps = mutableListOf<Pair<Int, Int>>()
        for (step in nextSteps) {
            val (stepI, stepJ) = step
            if (stepI == toI && stepJ == toJ ||
                (plan[stepI][stepJ] == Int.MAX_VALUE && !(stepI == startI && stepJ == startJ))
            ) {
                continue
            }
            validatedSteps.add(step)
        }
        validatedSteps.forEach {
            val result = findCycle(start, from = to, to = it, path = ArrayList(path))
            if (result.isNotEmpty()) {
                return result
            }
        }
        return listOf()
    }

    private fun rebuildPlan() {
        val cycle = cycle
        var min = Int.MAX_VALUE
        for (k in cycle.indices) {
            val (i, j) = cycle[k]
            if (k % 2 != 0) {
                min = min(plan[i][j], min)
            }
        }
        val rebuiltPlan = plan.deepcopy()
        var replaced = 0
        for (k in cycle.indices) {
            val (i, j) = cycle[k]
            if (k % 2 == 0) {
                if (rebuiltPlan[i][j] == Int.MAX_VALUE) {
                    rebuiltPlan[i][j] = 0
                }
                rebuiltPlan[i][j] += min
            } else {
                rebuiltPlan[i][j] -= min
                if (rebuiltPlan[i][j] == 0) {
                    rebuiltPlan[i][j] = Int.MAX_VALUE
                    replaced += 1
                    if (replaced > 1) {
                        rebuiltPlan[i][j] = 0
                    }
                }
            }
        }
        this.plan = rebuiltPlan
        calculateTotalCosts()
    }

    private fun calculateTotalCosts() {
        var sum = 0
        for (i in plan.indices) {
            for (j in plan[0].indices) {
                val cost = if (plan[i][j] != Int.MAX_VALUE) plan[i][j] else 0
                sum += cost * costs[i][j]
            }
        }
        totalCosts = sum
    }

    private fun makeSnapshot(): TransportTaskSnapshot {
        return TransportTaskSnapshot(
            condition,
            firstPass,
            costs.deepcopy(),
            resources.copy(),
            needs.copy(),
            if (this::plan.isInitialized) plan.deepcopy() else null,
            totalCosts
        )
    }
}
