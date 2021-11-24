package com.github.allepilli.kapl.tokens

enum class OperatorToken(val symbol: Char): Token {
    PLUS('+'),
    
    MINUS('-'),
    
    MULT('x');
    
    companion object {
        val symbols = values().map(OperatorToken::symbol)
        
        fun symbolOf(symbol: Char): OperatorToken = values().find { it.symbol == symbol }
            ?: throw IllegalArgumentException("No enum constant for '$symbol'")
    }
}