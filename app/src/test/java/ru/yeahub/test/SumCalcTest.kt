package ru.yeahub.test

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource


class SumCalcTest {

    @ParameterizedTest
    @ArgumentsSource(SumCalcArgumentsProvider::class)
    fun `should calculate sum correctly`(testCase: TestCase<Pair<Int, Int>, Int>) {
        val result = SumCalc().sum(testCase.dataToTest.first, testCase.dataToTest.second)
        assertEquals(testCase.expectedResult, result)
    }

}