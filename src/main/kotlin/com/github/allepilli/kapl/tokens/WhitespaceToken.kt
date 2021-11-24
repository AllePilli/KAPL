package com.github.allepilli.kapl.tokens

data class WhitespaceToken(val length: Int): Token {
    init {
        if (length <= 0) throw IllegalArgumentException("length cannot be smaller than or equal to 0, got: $length")
    }
    
    override fun toString(): String = "WT$length"
}