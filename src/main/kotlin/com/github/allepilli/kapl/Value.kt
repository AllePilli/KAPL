package com.github.allepilli.kapl

import com.github.allepilli.kapl.types.Type

data class Value(val type: Type, val value: Any) {
    override fun toString(): String = if (DEBUG) "{type: $type, value: $value}" else value.toString()
}