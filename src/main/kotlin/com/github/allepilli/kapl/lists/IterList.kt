package com.github.allepilli.kapl.lists

open class IterList<out T>(protected val values: List<T>, startEndOfList: Boolean = false) {
    var position = if (startEndOfList) values.size else -1
        private set
    
    val current: T?
        get() = values.getOrNull(position)
    
    val next: T
        get() = values[++position]
    
    val hasNext: Boolean
        get() = position + 1 < values.size
    
    val peek: T?
        get() = values.getOrNull(position + 1)
    
    val previous: T
        get() = values[--position]
    
    val hasPrevious: Boolean
        get() = position > 0
    
    val peekPrevious: T?
        get() = values.getOrNull(position - 1)
}