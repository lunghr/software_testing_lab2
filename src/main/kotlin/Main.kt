package org.example

import kotlin.math.*

fun main(args: Array<String>) {
    require(args.size == 3) { "Usage: <start> <end> <step>" }
    val start = args[0].toDoubleOrNull() ?: error("Invalid start value")
    val end = args[1].toDoubleOrNull() ?: error("Invalid end value")
    val step = args[2].toDoubleOrNull() ?: error("Invalid step value")

    require(step > 0) { "Step must be positive" }
    val xSequence = generateSequence(start) { it + step }.takeWhile { it <= end }.toList()
    val functions = Functions()
    val fullFunction = FullFunction(functions, functions)


    val filename = "src/test/resources/graph.csv"
    CsvExporter.exportByList(xSequence, filename) { x ->
        try {
            fullFunction.calculate(x)
        } catch (e: Exception) {
            Double.NaN
        }
    }
    println("✅ Exported to $filename")

}