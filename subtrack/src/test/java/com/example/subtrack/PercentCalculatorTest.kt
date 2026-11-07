package com.example.subtrack

import com.example.subtrack.domain.PercentCalculator
import org.junit.Assert.assertEquals
import org.junit.Test

class PercentCalculatorTest {
    @Test
    fun percents_alwaysSumTo100() {
        val percents = PercentCalculator.calculate(listOf(699.0, 399.0, 299.0, 259.0, 169.0))
        assertEquals(100, percents.sum())
    }

    @Test
    fun percents_areRoundedByLargestRemainder() {
        assertEquals(listOf(34, 33, 33), PercentCalculator.calculate(listOf(1.0, 1.0, 1.0)))
        assertEquals(listOf(50, 50), PercentCalculator.calculate(listOf(5.0, 5.0)))
    }

    @Test
    fun percents_areZeroWhenTotalIsZero() {
        assertEquals(listOf(0, 0), PercentCalculator.calculate(listOf(0.0, 0.0)))
    }
}
