package org.example

interface TrigFunctions {
    fun sin(x: Double): Double
    fun cos(x: Double): Double
    fun tan(x: Double): Double
    fun cot(x: Double): Double
    fun sec(x: Double): Double
    fun csc(x: Double): Double
}

interface LogFunctions {
    fun ln(x: Double): Double
    fun log2(x: Double): Double
    fun log3(x: Double): Double
    fun log10(x: Double): Double
}

interface UnaryFunction {
    fun calculate(x: Double): Double
}
