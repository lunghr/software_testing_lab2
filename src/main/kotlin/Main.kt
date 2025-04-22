package org.example

import kotlin.math.*

fun main() {
    val points: List<Double> = CsvExporter.import("src/test/resources/expected_results.csv").keys.toList()

    println(points)

    val pointsBeforeZero = points.filter { it <= 0 }
    CsvExporter.exportByList(pointsBeforeZero, "src/test/resources/output/sin.csv") { x -> sin(x) }
    CsvExporter.exportByList(pointsBeforeZero, "src/test/resources/output/cos.csv") { x -> cos(x) }
    CsvExporter.exportByList(pointsBeforeZero, "src/test/resources/output/tan.csv") { x -> tan(x) }
    CsvExporter.exportByList(pointsBeforeZero, "src/test/resources/output/cot.csv") { x -> 1 / tan(x) }
    CsvExporter.exportByList(pointsBeforeZero, "src/test/resources/output/sec.csv") { x -> 1 / cos(x) }
    CsvExporter.exportByList(pointsBeforeZero, "src/test/resources/output/csc.csv") { x -> 1 / sin(x) }

    val pointsAfterZero = points.filter { it > 0 }
    CsvExporter.exportByList(pointsAfterZero, "src/test/resources/output/ln.csv") { x -> ln(x) }
    CsvExporter.exportByList(pointsAfterZero, "src/test/resources/output/log2.csv") { x -> ln(x) / ln(2.0) }
    CsvExporter.exportByList(pointsAfterZero, "src/test/resources/output/log3.csv") { x -> ln(x) / ln(3.0) }
    CsvExporter.exportByList(pointsAfterZero, "src/test/resources/output/log10.csv") { x -> log10(x) }

}