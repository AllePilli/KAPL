package com.github.allepilli.kapl.tokens

enum class SingleCharacterToken(val symbol: Char): Token {
    OPEN_PAREN('('),
    
    CLOSE_PAREN(')');
    
    companion object {
        val symbols = values().map(SingleCharacterToken::symbol)
        
        fun symbolOf(symbol: Char): SingleCharacterToken = values().find { it.symbol == symbol }
            ?: throw IllegalArgumentException("No enum constant for '$symbol'")
    }
}