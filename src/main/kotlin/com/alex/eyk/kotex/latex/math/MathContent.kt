package com.alex.eyk.kotex.latex.math

private typealias Expr = MathContent

class MathContent(
    private val value: String
) {

    override fun toString() = value
}

infix fun Any.`+`(any: Any): Expr {
    return Expr("$this + $any")
}

infix fun Any.`-`(any: Any): Expr {
    return Expr("$this - $any")
}

infix fun Any.`*`(any: Any): Expr {
    return Expr("$this * $any")
}

infix fun Any.times(any: Any): Expr {
    return Expr("""$this * $any""")
}

infix fun Any.div(any: Any): Expr {
    return Expr("""\frac{$this}{$any}""")
}

infix fun Any.`^`(any: Any): Expr {
    return Expr("$this^{$any}")
}

infix fun Any.`_`(any: Any): Expr {
    return Expr("${this}_{$any}")
}

infix fun Any.`=`(any: Any): Expr {
    return Expr("$this = $any")
}

infix fun Any.less(any: Any): Expr {
    return Expr("$this < $any")
}

infix fun Any.leq(any: Any): Expr {
    return Expr("""$this \leq $any""")
}

infix fun Any.greater(any: Any): Expr {
    return Expr("$this > $any")
}

infix fun Any.geq(any: Any): Expr {
    return Expr("""$this \geq $any""")
}

fun inBrackets(expr: MathContent): Expr {
    return Expr("($expr)")
}

inline fun inBrackets(
    expr: () -> Expr
) = inBrackets(expr())

fun sum(
    expr: Expr,
    `for`: Any = "",
    border: Any = ""
): Expr {
    return Expr("""\sum${subscript(`for`)}${superscript(border)}{$expr}""")
}

inline fun sum(
    `for`: Any = "",
    border: Any = "",
    content: () -> Expr
) = sum(content(), `for`, border)

fun int(
    from: Any = "",
    to: Any = "",
    content: Expr
): Expr {
    return Expr("""\int${subscript(from)}${superscript(to)}{$content}""")
}

inline fun int(
    from: Any = "",
    to: Any = "",
    content: () -> Expr
) = int(from, to, content())

private fun subscript(
    content: Any
): String {
    return if (content.toString().isEmpty()) "" else "_{$content}"
}

private fun superscript(
    content: Any
): String {
    return if (content.toString().isEmpty()) "" else "^{$content}"
}
