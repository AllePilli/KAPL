package com.github.allepilli.kapl

import com.github.allepilli.kapl.exceptions.ParserException
import com.github.allepilli.kapl.lists.IterList
import com.github.allepilli.kapl.tokens.*
import com.github.allepilli.kapl.types.Int
import java.util.*

class Parser(text: String) {
    companion object {
        private const val NEGATIVE = '_'
    }
    
    private val chars = IterList(text.trim().toList(), true)
    
    fun parse(): List<Token> = with(LinkedList<Token>()) {
        while (true) {
            val peeked = chars.peekPrevious ?: break
            
            val token = when {
                peeked.isDigit() -> parseConstant()
                peeked.isWhitespace() -> parseWhitespace()
                peeked in OperatorToken.symbols -> parseOperator()
                peeked in SingleCharacterToken.symbols -> parseSingleCharacterToken()
                peeked == NEGATIVE -> throw ParserException("'$NEGATIVE' is only allowed in front of numbers")
                else -> throw ParserException("Illegal character: $peeked")
            }
            
            push(token)
        }
        
        toList()
    }
    
    /**
     * CONSTANT:
     * -> [[NEGATIVE]]DIGITS+ (Multiple Digits)
     */
    private fun parseConstant(): ConstantToken = ConstantToken(Int) {
        do append(chars.previous) while (chars.hasPrevious && chars.peekPrevious!!.isDigit())
        if (chars.hasPrevious && chars.peekPrevious == NEGATIVE) {
            chars.previous
            append('-')
        }
        reverse()
    }
    
    /**
     * @see OperatorToken.symbol
     */
    private fun parseOperator(): OperatorToken = OperatorToken.symbolOf(chars.previous)
    
    /**
     * @see SingleCharacterToken.symbol
     */
    private fun parseSingleCharacterToken(): SingleCharacterToken = SingleCharacterToken.symbolOf(chars.previous)
    
    private fun parseWhitespace(): WhitespaceToken = with(chars) {
        val startPosition = position
        
        @Suppress("ControlFlowWithEmptyBody")
        while (hasPrevious && peekPrevious!!.isWhitespace()) { previous }
        WhitespaceToken(startPosition - position)
    }
}