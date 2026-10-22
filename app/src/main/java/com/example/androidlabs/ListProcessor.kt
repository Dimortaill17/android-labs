package com.example.androidlabs

import kotlin.random.Random

class ListProcessor {

    fun generateNumbers(): List<Int> {
        return List(10) {
            Random.nextInt(-20, 21)
        }
    }

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

    fun getResult(numbers: List<Int>): String {
        val average = calculateAverage(numbers)

        return if (average == null) {
            "Подходящих элементов нет"
        } else {
            average.toString()
        }
    }
}