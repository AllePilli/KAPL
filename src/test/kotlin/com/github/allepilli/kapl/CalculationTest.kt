package com.github.allepilli.kapl

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.contracts.ExperimentalContracts

@ExperimentalContracts
class CalculationTest {
    @Test
    fun `Test addition`(): Unit = testCompile(
        "5 + 5" to 10.toValue(),
        "5 + 5 + 2 + 3" to 15.toValue(),
    )
    
    @Test
    fun `Test subtraction`(): Unit = testCompile(
        "5 - 5" to 0.toValue(),
        "5 - 5 - 2 - 3" to (-1).toValue(),
    )
    
    @Test
    fun `Test mix Addition & Subtraction`(): Unit = testCompile(
        "2+ 5 - 5" to 2.toValue(),
        "5 - 5 + 2 - 3" to 1.toValue(),
    )
    
    @Test
    fun `Test precedence using parentheses`(): Unit = testCompile(
        "(1 + 2) x 5" to 15.toValue(),
        "1 + 2 x 5" to 11.toValue(),
        "1 + (2 - (3 x 4))" to (-9).toValue(),
        "((4 + 2) - 3) x 4" to (12).toValue(),
    )
    
    private fun testCompile(vararg pairs: Pair<String, Value>): Unit = pairs.forEach { (input, expectedOutput) ->
        val actualOutput = compile(input).eval()
        Assertions.assertEquals(expectedOutput, actualOutput)
    }
}