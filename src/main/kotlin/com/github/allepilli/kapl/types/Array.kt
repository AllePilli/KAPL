package com.github.allepilli.kapl.types

/**
 * ARRAY:
 * -> Expression Expression Expression ... (multiple expressions separated by whitespaces)
 */
object Array: Type {
    override val conversion: (s: String) -> Any
        get() = throw IllegalStateException("The conversion field may not be invoked on an Array type")
}