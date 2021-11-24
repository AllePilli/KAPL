package com.github.allepilli.kapl

import com.github.allepilli.kapl.tokens.OperatorToken
import com.github.allepilli.kapl.tokens.OperatorToken.MINUS
import com.github.allepilli.kapl.tokens.OperatorToken.PLUS
import com.github.allepilli.kapl.tokens.SingleCharacterToken.CLOSE_PAREN
import com.github.allepilli.kapl.tokens.SingleCharacterToken.OPEN_PAREN
import com.github.allepilli.kapl.tokens.Token
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import com.github.allepilli.kapl.tokens.WhitespaceToken as WT

internal class ParserTest {
    /** TEST SUMS **/

    @Test
    fun `Parse sum of n numbers`(): Unit = testParser {
        for (n in 2..50) addAll(
            repRandomPositiveNumbers(10, n) { randomNums ->
                randomNums.joinToTokenPairs(" + ", WT1, PLUS, WT1)
            }
        )
    }
    
    /** OTHER **/
    
    @Test
    fun `Parse whitespace tokens`() = testParser {
        for (amtTerms in 2..10) addAll(
            repRandomPositiveNumbers(10, amtTerms) { randomNums ->
                randomNums.joinToTokenPairs("+", PLUS)
            }
        )
        
        for (amtWhitespaces in 1..50) addAll(
            repRandomPositiveNumbers(10, 5) { randomNums ->
                val whitespaces = " ".repeat(amtWhitespaces)
                val separator = "$whitespaces+$whitespaces"
        
                randomNums.joinToTokenPairs(separator, WT(amtWhitespaces), PLUS, WT(amtWhitespaces))
            }
        )
    }
    
    @Test
    fun `parse individual operator token successfully`(): Unit = OperatorToken.symbols.forEach { symbol ->
        val tokens = Parser("$symbol").parse()
        val expectedToken = OperatorToken.symbolOf(symbol)
        
        assertTrue(tokens.size == 1, "Expected 1 token, got ${tokens.size}")
        assertEquals(expectedToken, tokens.single())
    }
    
    @Test
    fun `Parse n digit constant`(): Unit = testParser {
        for (amtDigits in 1..9) repeat(10) {
            val rndNumber = RandomStringUtils.randomNumeric(amtDigits)!!
            add(rndNumber to listOf(rndNumber.toIntToken()))
        }
    }
    
    @Test
    fun `Parse negative constants`(): Unit = testParser {
        val negative = Parser.Companion::class.memberProperties
            .find { it.name == "NEGATIVE" }
            .let {
                assertNotNull(it)
                it!!
            }
            .apply { isAccessible = true }
            .call(Parser.Companion) as? Char
        
        assertNotNull(negative)
    
        for (amtDigits in 1..9) repeat(10) {
            val rndNumber = "$negative" + RandomStringUtils.randomNumeric(amtDigits)!!
            val rndNumberToken = ("-" + rndNumber.drop(1)).toIntToken()
            add(rndNumber to listOf(rndNumberToken))
        }
    }
    
    @Test
    fun `Parser returns empty list when receiving empty string`(): Unit = "".parsed().assertEmpty()
    
    @Test
    fun `Parser returns empty list for one or more whitespaces`(): Unit = testParser(
        " " to emptyList(),
        "  " to emptyList(),
        "\n" to emptyList(),
        "\t" to emptyList(),
        "\r" to emptyList(),
        "\n\r" to emptyList(),
        "\t\n\r" to emptyList(),
    )
    
    @Test
    fun `Parser successfully parses parentheses`(): Unit = testParser(
        "(" to tokensOf(OPEN_PAREN),
        ")" to tokensOf(CLOSE_PAREN),
        "(5)" to tokensOf(OPEN_PAREN, 5, CLOSE_PAREN),
        "(5 + 10)" to tokensOf(OPEN_PAREN, 5, WT1, PLUS, WT1, 10, CLOSE_PAREN),
        "((5 - 10) + 15)" to tokensOf(OPEN_PAREN, OPEN_PAREN, 5, WT1, MINUS, WT1, 10, CLOSE_PAREN,
                                      WT1, PLUS, WT1, 15, CLOSE_PAREN),
    )
    
    private fun testParser(pairBuilder: MutableList<Pair<String, List<Token>>>.() -> Unit): Unit =
        testParser(buildList(pairBuilder))
    
    private fun testParser(pairs: List<Pair<String, List<Token>>>) {
        pairs.map { (input, expectedTokens) -> input.parsed() to expectedTokens }
            .forEach { (actualTokens, expectedTokens) ->
                assertEquals(expectedTokens, actualTokens)
            }
    }
    
    private fun testParser(vararg pairs: Pair<String, List<Token>>) {
        pairs.map { (input, expectedTokens) -> input.parsed() to expectedTokens }
            .forEach { (actualTokens, expectedTokens) ->
                assertEquals(expectedTokens, actualTokens)
            }
    }
    
    private fun String.parsed(): List<Token> = Parser(this).parse()//Parser(this).parse()
}