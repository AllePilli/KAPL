package com.github.allepilli.kapl.types

object Int: Type {
    override val conversion: (s: String) -> Any
        get() = String::toLong
    
    override fun toString(): String = this::class.simpleName!!
}