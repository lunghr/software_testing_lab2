package org.example

object FunctionCsvLoader {
    fun load(path: String): Map<Double, Double> {
        try {
            val relativePath = path.removePrefix("/")
            val stream = javaClass.getResourceAsStream("/$relativePath")
                ?: javaClass.getResourceAsStream(relativePath)
                ?: return emptyMap()

            return loadFromStream(stream)
        } catch (e: Exception) {
            return emptyMap()
        }
    }

    private fun loadFromStream(stream: java.io.InputStream): Map<Double, Double> {
        val lines = stream.bufferedReader().readLines()
        if (lines.size <= 1) {
            return emptyMap()
        }

        return lines.drop(1).mapNotNull { line ->
            try {
                val parts = line.split(",")
                if (parts.size < 2) return@mapNotNull null

                val x = parts[0].trim().toDoubleOrNull() ?: return@mapNotNull null
                val y = parts[1].trim().toDoubleOrNull() ?: return@mapNotNull null

                x to y
            } catch (e: Exception) {
                null
            }
        }.toMap()
    }
}