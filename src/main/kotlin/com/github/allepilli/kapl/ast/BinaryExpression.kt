package com.github.allepilli.kapl.ast

import com.github.allepilli.kapl.Value
import com.github.allepilli.kapl.exceptions.KAPLException
import com.github.allepilli.kapl.tokens.OperatorToken
import com.github.allepilli.kapl.types.Type

/**
 * BINARY EXPRESSION:
 * -> EXPRESSION OPERATOR CONSTANT
 */
data class BinaryExpression(
    val left: Expression,
    val operator: OperatorToken,
    val right: Expression
): Expression {
    init {
        if (left !is ConstantExpression && left !is ParenthesizedExpression) throw KAPLException(
            "Left expression has to be a ConstantExpression or ParenthesizedExpression, but got $left"
        )
    }
    
    private val operationMap: Map<OperatorToken, Map<Type, (Value, Value) -> Any>> = mapOf(
        OperatorToken.PLUS to mapOf(
            com.github.allepilli.kapl.types.Int to { a: Value, b: Value -> a.value as Long + b.value as Long },
        ),
        OperatorToken.MINUS to mapOf(
            com.github.allepilli.kapl.types.Int to { a: Value, b: Value -> a.value as Long - b.value as Long },
        ),
        OperatorToken.MULT to mapOf(
            com.github.allepilli.kapl.types.Int to { a: Value, b: Value -> a.value as Long * b.value as Long },
        ),
    )
    
    override fun eval(): Value {
        val leftEval = left.eval()
        val rightEval = right.eval()
        
        val value = operationMap[operator]!![leftEval.type]!!(leftEval, rightEval)
        
        return Value(leftEval.type, value)
    }
    
    override fun toString(): String = "{$left ${operator.symbol} $right}"
}