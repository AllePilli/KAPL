package com.github.allepilli.kapl

import com.github.allepilli.kapl.ast.*
import com.github.allepilli.kapl.exceptions.LexerException
import com.github.allepilli.kapl.exceptions.lexerRequire
import com.github.allepilli.kapl.lists.TokenList
import com.github.allepilli.kapl.tokens.ConstantToken
import com.github.allepilli.kapl.tokens.OperatorToken
import com.github.allepilli.kapl.tokens.SingleCharacterToken
import com.github.allepilli.kapl.tokens.SingleCharacterToken.CLOSE_PAREN
import com.github.allepilli.kapl.tokens.SingleCharacterToken.OPEN_PAREN
import com.github.allepilli.kapl.tokens.Token
import java.util.*
import kotlin.contracts.ExperimentalContracts

@ExperimentalContracts
class Lexer(tokens: List<Token>) {
    private val tokens = TokenList(tokens)
    
    fun lex(): Expression = lexExpression()
    
    private fun lexExpression(): Expression = buildList {
        while (true) when (tokens.peekPreviousSkipWT) {
            is ConstantToken -> {
                if (size > 0) add(lexArrayExpression(removeLast()))
                else add(lexConstantExpression())
            }
            is OperatorToken -> {
                val operator = tokens.previousSkipWT as OperatorToken
                val expression = removeLastOrNull() ?: throw LexerException("Expected expression after '$operator'")
                
                if (operator.isPotentialUnary) when (tokens.peekPreviousSkipWT) {
                    null, OPEN_PAREN -> add(lexUnaryExpression(operator, expression))
                    else -> add(lexBinaryExpression(operator, expression))
                } else add(lexBinaryExpression(operator, expression))
            }
            CLOSE_PAREN -> {
                if (size > 0) add(lexArrayExpression(removeLast()))
                else add(lexParenthesizedExpression(tokens.previousSkipWT as SingleCharacterToken))
            }
            OPEN_PAREN, null -> break
            else -> tokens.previousSkipWT
        }
    }.singleOrNull() ?: throw LexerException("Expected only 1 expression")
    
    private fun lexConstantExpression(): ConstantExpression {
        val constantToken = tokens.previousSkipWT
        lexerRequire(constantToken is ConstantToken) { "expected ${ConstantToken::class.simpleName}, but got $constantToken" }
        
        return ConstantExpression(constantToken)
    }
    
    private fun lexArrayExpression(lastItem: Expression): ArrayExpression = ArrayExpression(
        with(LinkedList<Expression>()) {
            push(lastItem)
            
            do when (tokens.peekPreviousSkipWT) {
                is ConstantToken -> push(lexConstantExpression())
                CLOSE_PAREN -> push(lexParenthesizedExpression(tokens.previousSkipWT as SingleCharacterToken))
                OPEN_PAREN, is OperatorToken, null -> break
            } while (true)
            
            toList()
        }
    )
    
    private fun lexUnaryExpression(operator: OperatorToken, expression: Expression): UnaryExpression =
        UnaryExpression(operator, expression)
    
    private fun lexBinaryExpression(operatorToken: OperatorToken, rightExpression: Expression): BinaryExpression {
        val peekedSkipWT = tokens.peekPreviousSkipWT ?: throw LexerException("Expected a constant or a '('")
        lexerRequire(peekedSkipWT is ConstantToken || peekedSkipWT == CLOSE_PAREN) {
            "Expected a constant or a ')', but got ${peekedSkipWT::class.simpleName}"
        }
        
        val leftExpression = when (peekedSkipWT) {
            is ConstantToken -> lexConstantExpression()
            else -> lexParenthesizedExpression(tokens.previousSkipWT as SingleCharacterToken)
        }
        
        return BinaryExpression(leftExpression, operatorToken, rightExpression)
    }
    
    private fun lexParenthesizedExpression(closeParen: SingleCharacterToken): ParenthesizedExpression {
        require(closeParen == CLOSE_PAREN) { "openParen should be OPEN_PAREN, got $closeParen" }
        
        val expression = lexExpression()
        
        val peeked = tokens.peekPreviousSkipWT ?: throw LexerException("expected '('")
        lexerRequire(peeked == OPEN_PAREN) { "expected '(', but got $peeked" }
        
        return ParenthesizedExpression(tokens.previousSkipWT as SingleCharacterToken, expression, closeParen)
    }
}
