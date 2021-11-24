package com.github.allepilli.kapl

import com.github.allepilli.kapl.ast.Expression
import com.github.allepilli.kapl.exceptions.LexerException
import com.github.allepilli.kapl.tokens.OperatorToken.MINUS
import com.github.allepilli.kapl.tokens.OperatorToken.PLUS
import com.github.allepilli.kapl.tokens.SingleCharacterToken.CLOSE_PAREN
import com.github.allepilli.kapl.tokens.SingleCharacterToken.OPEN_PAREN
import com.github.allepilli.kapl.tokens.Token
import com.github.allepilli.kapl.tokens.WhitespaceToken
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.contracts.ExperimentalContracts

@ExperimentalContracts
class LexerTest {
    @Test
    fun `binary sum`(): Unit = testLexer(
        tokensOf(5, WT1, PLUS, WT1, 10) to binary(5, PLUS, 10),
        tokensOf(5, WT1, PLUS, WT1, 10, PLUS, 33) to binary(5, PLUS, binary(10, PLUS, 33)),
    )
    
    @Test
    fun `binary subtraction`(): Unit = testLexer(
        tokensOf(5, WT1, MINUS, WT1, 10) to binary(5, MINUS, 10),
        tokensOf(5, WT1, MINUS, WT1, 10, MINUS, 33) to binary(5, MINUS, binary(10, MINUS, 33)),
    )
    
    @Test
    fun `amount of whitespace tokens does not matter when lexing binary expression`(): Unit = testLexer(
        tokensOf(5, WT1, WT1, WT1, WT1, WT1, MINUS, WT1, WT1, 10) to binary(5, MINUS, 10),
        tokensOf(3, PLUS, 4) to binary(3, PLUS, 4)
    )
    
    @Test
    fun `correct parenthesisExpressions`(): Unit = testLexer(
        tokensOf(OPEN_PAREN, 5, CLOSE_PAREN) to paren(5),
        tokensOf(OPEN_PAREN, WT1, 5, WT1, CLOSE_PAREN) to paren(5),
        tokensOf(OPEN_PAREN, 5, WT1, PLUS, WT1, 10, CLOSE_PAREN) to paren(binary(5, PLUS, 10)),
        tokensOf(OPEN_PAREN, OPEN_PAREN, 5, WT1, MINUS, WT1, 10, CLOSE_PAREN, WT1, PLUS, WT1, 15, CLOSE_PAREN) to
                paren(binary(paren(binary(5, MINUS, 10)), PLUS, 15)),
        tokensOf(OPEN_PAREN, WhitespaceToken(3), 5, PLUS, 5, WhitespaceToken(3), CLOSE_PAREN) to
                paren(binary(5, PLUS, 5))
    )
    
    @Test
    fun `correct arrays`(): Unit = testLexer(
        tokensOf(1, WT1, 2, WT1, 3, WT1, 4, WT1, 5) to array(1, 2, 3, 4, 5),
        tokensOf(1, WT1) to 1.toExpression(),
        tokensOf(OPEN_PAREN, WT1, 1, WT1, CLOSE_PAREN) to paren(1),
        //(1 + 2) 3
        tokensOf(OPEN_PAREN, 1, WT1, PLUS, WT1, 2, CLOSE_PAREN, WT1, 3) to
                array(paren(binary(1, PLUS, 2)), 3),
        //1 (2 + 3) 4
        tokensOf(1, WT1, OPEN_PAREN, 2, WT1, PLUS, WT1, 3, WT1, CLOSE_PAREN, WT1, 4) to
                array(1, paren(binary(2, PLUS, 3)), 4),
        //1 2 (3 + 4)
        tokensOf(1, WT1, 2, WT1, OPEN_PAREN, 3, WT1, PLUS, WT1, 4, CLOSE_PAREN) to
                array(1, 2, paren(binary(3, PLUS, 4))),
        //1 2 (3 4 5)
        tokensOf(1, WT1, 2, WT1, OPEN_PAREN, 3, WT1, 4, WT1, 5, CLOSE_PAREN) to
                array(1, 2, paren(array(3, 4, 5))),
        //1 (2 3) (4 5 6)
        tokensOf(1, WT1, OPEN_PAREN, 2, WT1, 3, CLOSE_PAREN, WT1, OPEN_PAREN, 4, WT1, 5, WT1, 6, CLOSE_PAREN) to
                array(1, paren(array(2, 3)), paren(array(4, 5, 6))),
    )
    
    private fun testLexer(pairBuilder: MutableList<Pair<List<Token>, Expression>>.() -> Unit): Unit =
        testLexer(buildList(pairBuilder))
    
    private fun testLexer(pairs: List<Pair<List<Token>, Expression>>) {
        pairs.map { (input, expectedExpression) ->
            val lexed = try {
                input.lexed()
            } catch (e: LexerException) {
                println("INPUT: $input")
                println("EXPECTED OUTPUT: $expectedExpression")
                throw e
            }
        
            lexed to expectedExpression
        }.forEach { (actualExpression, expectedExpression) ->
            assertEquals(expectedExpression, actualExpression)
        }
    }
    
    private fun testLexer(vararg pairs: Pair<List<Token>, Expression>) {
        pairs.map { (input, expectedExpression) ->
            val lexed = try {
                input.lexed()
            } catch (e: LexerException) {
                println("INPUT: $input")
                println("EXPECTED OUTPUT: $expectedExpression")
                throw e
            }
            
            lexed to expectedExpression
        }.forEach { (actualExpression, expectedExpression) ->
            assertEquals(expectedExpression, actualExpression)
        }
    }
    
    private fun List<Token>.lexed(): Expression = Lexer(this).lex()
}