package com.github.allepilli.kapl.types

sealed interface Type {
    val conversion: (s: String) -> Any
}
