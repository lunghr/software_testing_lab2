package org.example

import kotlin.math.pow

class FullFunction(
    private val trig: TrigFunctions,
    private val log: LogFunctions
) {
    private fun trigPart(x: Double): Double {
        return ((((((((((trig.csc(x) * trig.sec(x)) - trig.cot(x)) - trig.cot(x)) - trig.csc(x)) - trig.csc(x)) / trig.cot(x)) -
                (trig.cos(x).pow(3))) / ((trig.sec(x) - trig.cos(x)).pow(3))) +
                (trig.sec(x) + (trig.sin(x) - ((trig.cot(x) * (trig.cos(x) / trig.cot(x))).pow(3))))) *
                ((trig.sec(x) + ((((trig.sin(x).pow(2)) * (trig.cos(x) + trig.cot(x))) + trig.cos(x)) + trig.cot(x))) +
                        ((trig.csc(x) * ((trig.sin(x) * (trig.cot(x) - trig.tan(x))) + trig.tan(x))) * (trig.cos(x) + trig.tan(x)))))
    }

    private fun logPart(x: Double): Double {
        return (((((log.ln(x) - log.ln(x)) * log.log2(x)).pow(2)) /
                (log.log10(x) * log.log3(x))).pow(3)).takeIf { !it.isNaN() } ?: 0.0
    }

    fun calculate(x: Double): Double {
        return if (x <= 0) trigPart(x) else logPart(x)
    }
}
