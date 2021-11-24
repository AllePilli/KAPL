package com.github.allepilli.kapl

fun <T> Any?.safeCast(): T? = this as? T
