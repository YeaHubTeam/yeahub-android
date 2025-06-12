package ru.yeahub.test


data class TestCase<T, R>(
    val dataToTest: T,
    val expectedResult: R
)