package com.github.allepilli.kapl.ast

import com.github.allepilli.kapl.Value
import com.github.allepilli.kapl.tokens.OperatorToken

class UnaryExpression(val operator: OperatorToken, val expression: Expression): Expression {
    override fun eval(): Value {
        return expression.eval()
    }
}