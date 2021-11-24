package com.github.allepilli.kapl.ast

import com.github.allepilli.kapl.Value

/**
 * EXPRESSION:
 * -> BINARY EXPRESSION
 * -> CONSTANT EXPRESSION
 */
interface Expression {
    fun eval(): Value
}