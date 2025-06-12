package ru.yeahub.test

class SumCalcArgumentsProvider : TestArgumentsProvider<TestCase<Pair<Int, Int>, Int>>() {

    override fun testCases(): List<TestCase<Pair<Int, Int>, Int>> = listOf(
        TestCase(Pair(1, 2), 3),
        TestCase(Pair(5, 2), 7),
        TestCase(Pair(0, 0), 0),
        TestCase(Pair(1, -1), 0),
        TestCase(Pair(Int.MAX_VALUE, 0), Int.MAX_VALUE),
        TestCase(Pair(Int.MIN_VALUE, 0), Int.MIN_VALUE)
    )
}

