package com.github.allepilli.kapl.tokens

import com.github.allepilli.kapl.types.Type

data class ConstantToken(val type: Type, val value: String): Token {
    override fun toString(): String = "$type: $value"
    
    companion object {
        inline operator fun invoke(type: Type, stringBuilder: StringBuilder.() -> Unit): ConstantToken =
            ConstantToken(type, buildString(stringBuilder))
    }
}