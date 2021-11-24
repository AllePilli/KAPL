package com.github.allepilli.kapl.ast

import com.github.allepilli.kapl.Value
import com.github.allepilli.kapl.tokens.OperatorToken
import com.github.allepilli.kapl.types.Array
import com.github.allepilli.kapl.types.Int

class UnaryExpression(val operator: OperatorToken, val expression: Expression): Expression {
    init {
        require(operator.isPotentialUnary) {
            "A unary expression can only take an operator that can be used monadically, but got $operator"
        }
    }
    
    private fun minusInt(a: Value): Value = Value(Int, -1L * (a.value as Long))
    
    private val operationMap = mapOf(
        OperatorToken.MINUS to mapOf(
            Int to ::minusInt,
            Array to { a: Value -> Value(Array, (a.value as List<*>).map { minusInt(it as Value) }) }
        )
    )
    
    override fun eval(): Value {
        val expressionEval = expression.eval()
        return operationMap[operator]!![expressionEval.type]!!(expressionEval)
    }
}
