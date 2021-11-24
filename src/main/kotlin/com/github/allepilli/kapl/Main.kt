package com.github.allepilli.kapl

import com.github.allepilli.kapl.ast.Expression
import com.github.allepilli.kapl.tokens.WhitespaceToken
import kotlin.contracts.ExperimentalContracts

private val debugRgx = """\s*::\s*debug\s*""".toRegex()

var DEBUG = false

val WT1 = WhitespaceToken(1)

@ExperimentalContracts
fun compile(text: String): Expression {
    if (DEBUG) {
        println("INPUT:")
        println("\t$text")
    }
    
    val parser = Parser(text)//Parser(text)
    val tokens = parser.parse()
    if (DEBUG) {
        println("PARSER OUTPUT:")
        println("\t$tokens")
    }
    
    val lexer = Lexer(tokens)
    val ast = lexer.lex()
    if (DEBUG) {
        println("LEXER OUTPUT:")
        println("\t$ast")
        
        val output = ast.eval()
        println("OUTPUT:")
        println("\t$output\n")
    }
    
    return ast
}

@ExperimentalContracts
fun main() {
    var input: String
    println("Welcome to KAPL")
    
    while (true) {
        print("> ")
        input = readln()
        
        if (input matches debugRgx) DEBUG = !DEBUG
        else {
            val expression = compile(input)
    
            if (!DEBUG) println("\t${expression.eval()}")
        }
    }
}