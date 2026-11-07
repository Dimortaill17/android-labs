package com.example.subtrack.domain

object PercentCalculator {
    fun calculate(values: List<Double>): List<Int> {
        val total = values.sum()
        if (total <= 0.0) return values.map { 0 }

        val exact = values.map { it / total * 100.0 }
        val result = exact.map { it.toInt() }.toMutableList()
        var left = 100 - result.sum()

        val order = exact.indices.sortedByDescending { exact[it] - result[it] }
        for (index in order) {
            if (left == 0) break
            result[index]++
            left--
        }
        return result
    }
}
