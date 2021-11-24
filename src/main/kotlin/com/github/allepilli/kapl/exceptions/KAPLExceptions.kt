package com.github.allepilli.kapl.exceptions

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

open class KAPLException(msg: String): Exception(msg)

open class ParserException(msg: String): KAPLException(msg)

open class LexerException(msg: String): KAPLException(msg)

/** Preconditions **/
@ExperimentalContracts
inline fun lexerRequire(value: Boolean, lazyMsg: () -> Any) {
    contract {
        returns() implies value
    }
    if (!value) {
        val msg = lazyMsg()
        throw LexerException(msg.toString())
    }
}