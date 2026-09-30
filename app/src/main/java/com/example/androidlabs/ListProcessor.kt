package com.example.androidlabs

class ListProcessor {

    fun calculateAverage(numbers: List<Int>): Double? {
        val suitableNumbers = numbers
            .withIndex()
            .filter { (index, value) ->
                index % 2 == 0 && value % 2 == 0
            }
            .map { it.value }

        return if (suitableNumbers.isEmpty()) {
            null
        } else {
            suitableNumbers.average()
        }
    }
}