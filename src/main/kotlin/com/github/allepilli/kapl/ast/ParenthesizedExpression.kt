package com.github.allepilli.kapl.ast

import com.github.allepilli.kapl.Value
import com.github.allepilli.kapl.tokens.SingleCharacterToken

data class ParenthesizedExpression(
    val openParen: SingleCharacterToken,
    val expression: Expression,
    val closeParen: SingleCharacterToken
): Expression {
    init {
        if (openParen != SingleCharacterToken.OPEN_PAREN)
            throw IllegalArgumentException("openParen parameter should be a open parenthesis token, got $openParen")
        if (closeParen != SingleCharacterToken.CLOSE_PAREN)
            throw IllegalArgumentException("closeParen parameter should be a close parenthesis token, got $openParen")
    }
    
    override fun eval(): Value = expression.eval()
    
    override fun toString(): String = "($expression)"
}