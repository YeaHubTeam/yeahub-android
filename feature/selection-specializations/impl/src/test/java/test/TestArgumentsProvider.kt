package test

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream

abstract class TestArgumentsProvider<T> : ArgumentsProvider {
    abstract fun testCases(): List<T>

    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        return testCases().map { Arguments.of(it) }.stream()
    }
}