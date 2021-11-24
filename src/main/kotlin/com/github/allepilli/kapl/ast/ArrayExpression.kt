package com.github.allepilli.kapl.ast

import com.github.allepilli.kapl.Value
import com.github.allepilli.kapl.exceptions.lexerRequire
import com.github.allepilli.kapl.types.Array
import kotlin.contracts.ExperimentalContracts

@ExperimentalContracts
data class ArrayExpression(val items: List<Expression>): Expression {
    init {
        lexerRequire(items.none { it is ArrayExpression }) {
            "An ArrayExpression cannot contain another ArrayExpression, but got $items"
        }
    }
    
    override fun eval(): Value = Value(Array, items.map(Expression::eval))
    override fun toString(): String = items.toString()
}