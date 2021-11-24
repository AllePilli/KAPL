package com.github.allepilli.kapl.tokens

enum class OperatorToken(val symbol: Char, val valence: OperatorValence): Token {
    PLUS('+', OperatorValence.DYADIC),
    
    MINUS('-', OperatorValence.BOTH),
    
    MULT('x', OperatorValence.DYADIC);
    
    enum class OperatorValence {
        MONADIC,
        DYADIC,
        BOTH
    }
    
    val isPotentialUnary: Boolean
        get() = valence == OperatorValence.BOTH || valence == OperatorValence.MONADIC
    
    companion object {
        val symbols = values().map(OperatorToken::symbol)
        
        fun symbolOf(symbol: Char): OperatorToken = values().find { it.symbol == symbol }
            ?: throw IllegalArgumentException("No enum constant for '$symbol'")
    }
}
