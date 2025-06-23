package ru.yeahub.test

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

class SumCalcTest {

    data class SumCalcTestCase(
        val dataToTest: Pair<Int, Int>,
        val expectedResult: Int
    )

    class SumCalcArgumentsProvider : TestArgumentsProvider<SumCalcTestCase>() {

        override fun testCases(): List<SumCalcTestCase> = listOf(
            SumCalcTestCase(Pair(1, 2), 3),
            SumCalcTestCase(Pair(5, 2), 7),
            SumCalcTestCase(Pair(0, 0), 0),
            SumCalcTestCase(Pair(1, -1), 0),
            SumCalcTestCase(Pair(Int.MAX_VALUE, 0), Int.MAX_VALUE),
            SumCalcTestCase(Pair(Int.MIN_VALUE, 0), Int.MIN_VALUE)
        )
    }

    @ParameterizedTest
    @ArgumentsSource(SumCalcArgumentsProvider::class)
    fun `should calculate sum correctly`(testCase: SumCalcTestCase) {
        val result = SumCalc().sum(testCase.dataToTest.first, testCase.dataToTest.second)
        assertEquals(testCase.expectedResult, result)
    }
}