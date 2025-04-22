package org.example
import java.io.File

object CsvExporter {
    fun exportByList(values: List<Double>, filename: String, func: (Double) -> Double) {
        val file = File(filename)
        file.printWriter().use { out ->
            out.println("x,value")
            for (x in values) {
                val result = try {
                    func(x)
                } catch (e: Exception) {
                    Double.NaN
                }
                out.println("$x,${String.format(java.util.Locale.US, "%.8f", result)}")
            }
        }
    }

    fun import(filename: String): Map<Double, Double> {
        try {
            val file = File(filename)
            if (!file.exists() || file.length() == 0L) {
                return emptyMap()
            }

            return File(filename).bufferedReader().use { reader ->
                reader.readLines()
                    .drop(1)
                    .mapNotNull { line ->
                        val parts = line.split(",")
                        if (parts.size == 2) {
                            val x = parts[0].trim().toDoubleOrNull()
                            val y = parts[1].trim().toDoubleOrNull()
                            if (x != null && y != null) x to y else null
                        } else null
                    }
                    .toMap()
            }
        } catch (e: Exception) {
            return emptyMap()
        }
    }
}