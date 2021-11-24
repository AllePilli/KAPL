package com.github.allepilli.kapl.ast

import com.github.allepilli.kapl.Value
import com.github.allepilli.kapl.tokens.ConstantToken

data class ConstantExpression(val valueToken: ConstantToken): Expression {
    override fun eval(): Value = Value(valueToken.type, valueToken.type.conversion(valueToken.value))
    
    override fun toString(): String = "{${valueToken.type}: ${valueToken.value}}"
}