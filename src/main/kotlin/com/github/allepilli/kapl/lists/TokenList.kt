package com.github.allepilli.kapl.lists

import com.github.allepilli.kapl.tokens.Token
import com.github.allepilli.kapl.tokens.WhitespaceToken

class TokenList(tokens: List<Token>): IterList<Token>(tokens, true) {
    val peekPreviousSkipWT: Token?
        get() = values.subList(0, position)
            .lastOrNull { token -> token !is WhitespaceToken }
    
    val previousSkipWT: Token
        get() {
            if (peekPrevious !is WhitespaceToken) return previous
    
            @Suppress("ControlFlowWithEmptyBody")
            while (hasPrevious && previous is WhitespaceToken) {}
            return current!!
        }
}