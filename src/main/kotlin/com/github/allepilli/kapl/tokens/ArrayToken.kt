package com.github.allepilli.kapl.tokens

data class ArrayToken(val items: List<String>): Token {
    override fun toString(): String = items.toString()
}