package com.alex.eyk.kotex.example

import com.alex.eyk.kotex.document.BaseDocument
import com.alex.eyk.kotex.entity.Package
import com.alex.eyk.kotex.example.task.PotentialsTransportTaskSolver
import com.alex.eyk.kotex.example.task.TransportTask
import com.alex.eyk.kotex.example.task.TransportTaskPotentialsEventListener
import com.alex.eyk.kotex.example.task.TransportTaskSnapshot
import com.alex.eyk.kotex.latex.Bold
import com.alex.eyk.kotex.latex.Br
import com.alex.eyk.kotex.latex.Caption
import com.alex.eyk.kotex.latex.Center
import com.alex.eyk.kotex.latex.CenteredFigure
import com.alex.eyk.kotex.latex.Command
import com.alex.eyk.kotex.latex.Content
import com.alex.eyk.kotex.latex.DeclareExternalPackage
import com.alex.eyk.kotex.latex.DeclareExternalPackages
import com.alex.eyk.kotex.latex.DocumentBegin
import com.alex.eyk.kotex.latex.DocumentEnd
import com.alex.eyk.kotex.latex.LaTeX
import com.alex.eyk.kotex.latex.Space
import com.alex.eyk.kotex.latex.SpaceSize.AS_SPACE
import com.alex.eyk.kotex.latex.SpaceSize.MEDIUM
import com.alex.eyk.kotex.latex.asContent
import com.alex.eyk.kotex.latex.asText
import com.alex.eyk.kotex.latex.asTextln
import com.alex.eyk.kotex.latex.math.`!=`
import com.alex.eyk.kotex.latex.math.Expression
import com.alex.eyk.kotex.latex.math.InlineExpression
import com.alex.eyk.kotex.latex.math.InlineMath
import com.alex.eyk.kotex.latex.math.IntMatrix
import com.alex.eyk.kotex.latex.math.Matrix
import com.alex.eyk.kotex.latex.math.Superscript
import com.alex.eyk.kotex.latex.math.Tilde
import com.alex.eyk.kotex.latex.math.`_`
import com.alex.eyk.kotex.latex.math.asInlineExpr
import com.alex.eyk.kotex.latex.table.TableBuilder
import com.alex.eyk.kotex.latex.table.asTable
import com.alex.eyk.kotex.util.plus

class TransportTaskDocument(
    name: String,
    path: String,
    task: TransportTask
) : BaseDocument(name, path), TransportTaskPotentialsEventListener {

    private val solver = PotentialsTransportTaskSolver(
        condition = task,
        eventListener = this
    )

    fun solveTask() {
        setDocumentClass(
            name = "article",
            options = listOf("a4paper")
        )
        beforeSolve()
        solver.solve()
    }

    private fun beforeSolve() = append {
        DeclareExternalPackages(
            Package(
                name = "fontenc",
                options = listOf("T2A")
            ),
            Package(
                name = "geometry",
                options = listOf("left=2cm", "right=1cm", "top=2cm", "bottom=2cm")
            ),
            Package(
                name = "inputenc",
                options = listOf("utf8")
            ),
            Package(
                name = "babel",
                options = listOf("english", "russian")
            )
        )
        Br()
        Content(
            raw = """
                \setlength{\parskip}{0.27cm}
                \renewcommand{\arraystretch}{1.5}
                
            """.trimIndent()
        )
        Br()
    }

    override fun onStart(
        condition: TransportTask
    ) = append {
        DocumentBegin()
        Center {
            "Транспортная задача".asTextln()
        }
        Br()
        """
            Транспортная задача задана таблицей. Необходимо найти перевозку методом
            минимального элемента, а затем найти оптимальное решение методом потенциалов.
        """.trimIndent()
            .asTextln()
        Br()
        CenteredFigure {
            TransportTaskTable(condition.costs, condition.resources, condition.needs)
            Caption("Условие задачи")
        }
        Br()
        Bold { "Решение".asText() } + Br()
        Br()
    }

    override fun onConvertedToClosed(
        snapshot: TransportTaskSnapshot,
        addedColumn: Boolean,
        oldResources: Int,
        oldNeeds: Int
    ) = append {
        "Задача является открытой, так как сумма всех ресурсов не равна сумме потребностей: ".asTextln()
        InlineMath { oldResources `!=` oldNeeds } + ".".asTextln()
        if (addedColumn) {
            "Для преобразования к закрытой добавили дополнительный столбец.".asTextln()
        } else {
            "Для преобразования к закрытой добавили дополнительный ряд.".asTextln()
        }
        Br()
        CenteredFigure {
            TransportTaskTable(snapshot.costs!!, snapshot.resources, snapshot.needs)
            Caption("Новая закрытая задача")
        }
        Br()
    }

    override fun onStartPlanBuilt(
        snapshot: TransportTaskSnapshot
    ) = append {
        "Составим первоначальный план перевозок методом минимального элемента".asTextln()
        Br()
        CenteredFigure {
            PlanTable(snapshot)
            Caption("Начальный план перевозок")
        }
        Br()
    }

    override fun onDegenerateTaskFixed(
        snapshot: TransportTaskSnapshot
    ) = append {
        "Задача является вырожденной (невозможно рассчитать потенциалы). Исправленный план:".asTextln()
        Br()
        CenteredFigure {
            PlanTable(snapshot)
            Caption("План перевозок с дополнительным нулем.")
        }
        Br()
    }

    override fun onPotentialsCalculated(
        snapshot: TransportTaskSnapshot,
        ui: List<Int>,
        vj: List<Int>
    ) = append {
        if (snapshot.firstPass) {
            "Рассчитаем потенциалы текущего плана:".asTextln()
        }
        Br()
        CenteredFigure {
            PotentialsTable(snapshot, ui, vj)
            Caption("Таблица потенциалов")
        }
    }

    override fun onValidatePotentials(
        snapshot: TransportTaskSnapshot,
        potentialsSumMatrix: List<List<Int>>,
        potentialsSumSubtractCostsMatrix: List<List<Int>>
    ) = append {
        Expression {
            Tilde { "C".asContent() } + " = ".asContent() + IntMatrix(potentialsSumMatrix) +
                    ";".asContent() + Space(AS_SPACE) + Br()
            "C - ".asContent() + Tilde { "C".asContent() } + " = ".asContent() +
                    IntMatrix(potentialsSumSubtractCostsMatrix)
        }
        Br()
    }

    override fun onFoundOptimalSolve(
        snapshot: TransportTaskSnapshot
    ) = append {
        Bold { "Ответ: ".asText() } + Br() + InlineExpression {
            Matrix(
                content = snapshot.plan!!,
                map = { item, _, _ ->
                    if (item == Int.MAX_VALUE) {
                        "0".asContent()
                    } else {
                        item.asContent()
                    }
                }
            )
        } + ", стоимость: ${snapshot.totalCosts}".asTextln()
        DocumentEnd()
    }

    override fun onPlanRebuilt(
        snapshot: TransportTaskSnapshot,
        rebuildCycle: List<Pair<Int, Int>>,
        oldPlan: List<List<Int>>,
        ui: List<Int>,
        vj: List<Int>
    ) = append {
        CenteredFigure {
            CycleTable(snapshot, rebuildCycle, ui, vj)
            Caption("Цикл для перестроения плана")
        }
    }

    @LaTeX
    private suspend fun TransportTaskTable(
        costs: List<List<Int>>,
        resources: List<Int>,
        needs: List<Int>
    ) {
        TableBuilder()
            .content(content = costs) { item, _, _ ->
                item.asContent()
            }
            .expandLeft { i -> InlineMath { "A" `_` (i + 1) } }
            .expandRight { i -> resources[i].asContent() }
            .expandTop(needsHead(listOf(size = needs.size) { it + 1 }))
            .expandBottom(needsTotalBottom(needs))
            .build()
            .asTable()
    }

    @LaTeX
    private suspend fun PlanTable(
        snapshot: TransportTaskSnapshot
    ) {
        TableBuilder()
            .content(content = snapshot.plan!!) { selected, i, j ->
                InlineExpression {
                    PlanEntry(selected, snapshot.costs!![i][j])
                }
            }
            .expandLeft { i -> InlineMath { "A" `_` (i + 1) } }
            .expandRight { i -> snapshot.resources[i].asContent() }
            .expandTop(needsHead(snapshot.needs))
            .expandBottom(needsTotalBottom(snapshot.needs))
            .build()
            .asTable()
    }

    @LaTeX
    private suspend fun PotentialsTable(
        snapshot: TransportTaskSnapshot,
        ui: List<Int>,
        vj: List<Int>
    ) {
        TableBuilder()
            .content(content = snapshot.plan!!) { selected, i, j ->
                InlineExpression {
                    PlanEntry(selected, snapshot.costs!![i][j])
                }
            }
            .expandLeft { i -> snapshot.resources[i].asContent() }
            .expandRight { i -> ui[i].asContent() }
            .expandTop(needsTotalTop(snapshot.needs))
            .expandBottom(vBottom(vj))
            .build()
            .asTable()
    }

    @LaTeX
    private suspend fun CycleTable(
        snapshot: TransportTaskSnapshot,
        cycle: List<Pair<Int, Int>>,
        ui: List<Int>,
        vj: List<Int>
    ) {
        TableBuilder()
            .content(snapshot.plan!!) { item, i, j ->
                val index = Pair(i, j)
                if (index in cycle) {
                    val sign = if (cycle.indexOf(index) % 2 == 0) "+" else "-"
                    InlineExpression {
                        Bold { "($sign)".asContent() } + Space(AS_SPACE) +
                                PlanEntry(item, snapshot.costs!![i][j])
                    }
                } else {
                    InlineExpression { PlanEntry(item, snapshot.costs!![i][j]) }
                }
            }
            .expandLeft { i -> snapshot.resources[i].asContent() }
            .expandRight { i -> ui[i].asContent() }
            .expandTop(needsTotalTop(snapshot.needs))
            .expandBottom(vBottom(vj))
            .build()
            .asTable()
    }

    @LaTeX
    private suspend fun PlanEntry(
        selected: Int,
        cost: Int
    ) {
        Selected(selected) + Space(size = MEDIUM) + Superscript { cost.asContent() }
    }

    @LaTeX
    private suspend fun Selected(
        value: Int
    ) {
        if (value == Int.MAX_VALUE) {
            "-".asContent()
        } else {
            value.asContent()
        }
    }

    private fun needsHead(
        needs: List<Int>
    ): @LaTeX suspend (j: Int) -> Unit = listRow(
        list = needs,
        map = {
            InlineMath { "B" `_` it }
        },
        first = { LightGrayCell() },
        last = { "Запасы".asText() }
    )

    private fun needsTotalBottom(
        needs: List<Int>
    ): @LaTeX suspend (j: Int) -> Unit = { j ->
        if (j == 0 || j == needs.size + 1) {
            LightGrayCell()
        } else {
            needs[j - 1].asInlineExpr()
        }
    }

    private fun needsTotalTop(
        needs: List<Int>
    ): @LaTeX suspend (j: Int) -> Unit = listRow(
        list = needs,
        map = { it.asContent() },
        first = { LightGrayCell() },
        last = { InlineExpression { "u_i".asContent() } }
    )

    private fun vBottom(
        vj: List<Int>
    ): @LaTeX suspend (j: Int) -> Unit = listRow(
        list = vj,
        map = { it.asContent() },
        first = { InlineExpression { "v_j".asContent() } },
        last = { LightGrayCell() }
    )

    private fun <E> listRow(
        list: List<E>,
        map: suspend (item: E) -> Unit,
        first: @LaTeX suspend () -> Unit,
        last: @LaTeX suspend () -> Unit
    ): (@LaTeX suspend (j: Int) -> Unit) = { j ->
        when (j) {
            0 -> {
                first()
            }

            list.size + 1 -> {
                last()
            }

            else -> {
                map(list[j - 1])
            }
        }
    }

    @LaTeX
    suspend fun LightGrayCell() {
        CellColor(
            color = "black",
            opacity = 15
        )
    }

    @LaTeX
    suspend fun CellColor(
        color: String,
        opacity: Int = 100
    ) {
        if (opacity < 0 || opacity > 100) {
            throw IllegalArgumentException(
                "Wrong opacity. Should be 0 <= `opacity` <= 100"
            )
        }

        DeclareExternalPackage(
            Package(
                name = "xcolor",
                options = listOf("table")
            )
        )
        val opacityArg = if (opacity == 100) "" else "!$opacity"
        Command(
            name = "cellcolor",
            value = color + opacityArg,
            endLineBreak = false
        )
    }
}
