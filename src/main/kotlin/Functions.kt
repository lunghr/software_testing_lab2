package org.example

import kotlin.math.PI
import kotlin.math.abs


class Functions : TrigFunctions, LogFunctions {

    private val eps = 1e-10

    private fun normalizeToMinusPiToPi(x: Double): Double {
        val twoPi = 2 * PI
        var reduced = x % twoPi
        if (reduced > PI) reduced -= twoPi
        if (reduced < -PI) reduced += twoPi
        return reduced
    }

    override fun sin(x: Double): Double {
        val normalizedX = normalizeToMinusPiToPi(x)
        tailrec fun sinRec(term: Double, sum: Double, n: Int): Double =
            if (abs(term) <= eps) sum
            else {
                val nextN = n + 2
                val nextTerm = term * (-normalizedX * normalizedX) / (nextN * (nextN - 1))
                sinRec(nextTerm, sum + nextTerm, nextN)
            }
        return sinRec(normalizedX, normalizedX, 1)
    }

    override fun cos(x: Double): Double = sin(x + PI / 2)

    override fun tan(x: Double): Double {
        val c = cos(x)
        return sin(x) / c
    }

    override fun cot(x: Double): Double {
        val s = sin(x)
        return cos(x) / s
    }

    override fun sec(x: Double): Double {
        val c = cos(x)
        return 1 / c
    }

    override fun csc(x: Double): Double {
        val s = sin(x)
        return 1 / s
    }

    override fun ln(x: Double): Double {
        require(x > 0)
        var result = 0.0
        var term = (x - 1) / (x + 1)
        val termSquared = term * term
        var n = 1
        while (abs(term) > eps) {
            result += term / n
            term *= termSquared
            n += 2
        }
        return 2 * result
    }

    override fun log2(x: Double) = ln(x) / ln(2.0)
    override fun log3(x: Double) = ln(x) / ln(3.0)
    override fun log10(x: Double) = ln(x) / ln(10.0)
}