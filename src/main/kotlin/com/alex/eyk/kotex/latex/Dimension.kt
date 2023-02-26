package com.alex.eyk.kotex.latex

enum class Dimension(
    val abbreviation: String
) {

    /**
     * Point.
     */
    PT("pt"),

    /**
     * Pica. 1 [PC] = 12 [PT].
     */
    PC("pc"),

    /**
     * Inch. 1 [IN] = 72.27 [PT].
     */
    IN("in"),

    /**
     * Big point. 72 [BP] = 1 [IN].
     */
    BP("bp"),

    /**
     * Centimeter.
     */
    CM("cm"),

    /**
     * Millimeter.
     */
    MM("mm"),

    /**
     * Didot point. 1157 [DD] = 1238 [PT].
     */
    DD("dd"),

    /**
     * Cicero. 1 [CC] = 12 [DD].
     */
    CC("cc"),

    /**
     * Scaled point. 65536 [SP] = 1 [PT]
     */
    SP("sp"),

    /**
     * The size depends on the font.
     */
    EX("ex"),

    /**
     * The size depends on the font.
     */
    EM("em"),

    /**
     * Math unit. Available only in math mode. 1 [EM] = 18 [MU], em is taken
     * from the math symbols family.
     */
    MU("mu"),

    /**
     * Pixel. Available in pdfTeX and LuaTeX.
     */
    PX("px")
}
