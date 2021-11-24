package com.github.allepilli.kapl

import com.github.allepilli.kapl.ast.*
import com.github.allepilli.kapl.tokens.ConstantToken
import com.github.allepilli.kapl.tokens.OperatorToken
import com.github.allepilli.kapl.tokens.SingleCharacterToken
import com.github.allepilli.kapl.tokens.SingleCharacterToken.CLOSE_PAREN
import com.github.allepilli.kapl.tokens.SingleCharacterToken.OPEN_PAREN
import com.github.allepilli.kapl.tokens.Token
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.contracts.ExperimentalContracts
import kotlin.random.Random

fun <T> List<T>.assertEmpty() {
    assertTrue(isEmpty(), "Expected empty List, got list of size $size")
}

fun randomNumbers(amount: Int): List<Int> = (1..amount).map { Random.nextInt() }

fun <T> repRandomNumbers(repetitions: Int, amount: Int, transform: (List<Int>) -> T): List<T> = (1..repetitions)
    .map { randomNumbers(amount) }
    .map(transform)

fun <T> repRandomPositiveNumbers(repetitions: Int, amount: Int, transform: (List<Int>) -> T): List<T> = (1..repetitions)
    .map { randomNumbers(amount).map { if (it < 0) -1 * it else it } }
    .map(transform)

/** Token Generators **/
fun tokensOf(vararg values: Any): List<Token> = values.map { if (it !is Token) it.toToken() else it }

fun Any.toToken(): Token = when (this) {
    is Int -> toToken()
    is Char -> toToken()
    is String -> toToken()
    else -> throw IllegalArgumentException("No toToken function found for type: ${this::class.simpleName}")
}

fun Int.toToken(): ConstantToken = ConstantToken(com.github.allepilli.kapl.types.Int, "$this")

fun Char.toToken(): Token = "$this".toToken()

fun String.toToken(): Token = when (this) {
    "+" -> OperatorToken.PLUS
    else -> throw IllegalArgumentException("No token representation for string: $this")
}

fun String.toIntToken(): Token = ConstantToken(com.github.allepilli.kapl.types.Int, this)

fun List<Any>.toTokenList(): List<Token> = map { if (it !is Token) it.toToken() else it }

fun <T> List<T>.joinToList(separator: List<T>): List<T> = buildList {
    this@joinToList.withIndex().forEach { (idx, element) ->
        add(element)
        if (idx < this@joinToList.size - 1) addAll(separator)
    }
}

fun <T> List<T>.joinToList(vararg separators: T): List<T> = buildList {
    this@joinToList.withIndex().forEach { (idx, element) ->
        add(element)
        if (idx < this@joinToList.size - 1) addAll(separators)
    }
}

fun <T> List<T>.joinToTokens(vararg separators: Token): List<Token> = buildList {
    this@joinToTokens.withIndex().forEach { (idx, element) ->
        add(element?.toToken() ?: throw Exception("blahblah"))
        if (idx < this@joinToTokens.size - 1) addAll(separators)
    }
}

fun <T> List<T>.joinToTokenPairs(stringSeparator: String, vararg tokenSeparators: Token): Pair<String, List<Token>> =
    joinToString(separator = stringSeparator) to joinToTokens(*tokenSeparators)

/** Expression Generators **/
fun Any.toExpression(): Expression = when (this) {
    is Int -> toExpression()
    else -> throw IllegalArgumentException("No toExpression function found for type: ${this::class.simpleName}")
}

fun Int.toExpression(): ConstantExpression = ConstantExpression(toToken())

fun paren(any: Any): ParenthesizedExpression =
    ParenthesizedExpression(OPEN_PAREN, if (any !is Expression) any.toExpression() else any, CLOSE_PAREN)

//fun Expression.paren(): ParenthesizedExpression =
//    ParenthesizedExpression(SingleCharacterToken.OPEN_PAREN, this, SingleCharacterToken.CLOSE_PAREN)
//
//fun Int.paren(): ParenthesizedExpression = toExpression().paren()

@ExperimentalContracts
fun List<Any>.array(): ArrayExpression = ArrayExpression(map { if (it !is Expression) it.toExpression() else it })

@ExperimentalContracts
fun array(vararg items: Any): ArrayExpression = ArrayExpression(
    items.map { if (it !is Expression) it.toExpression() else it }
)

fun binary(left: Any, operator: OperatorToken, right: Any): BinaryExpression {
    val leftExpression = if (left !is Expression) left.toExpression() else left
    val rightExpression = if (right !is Expression) right.toExpression() else right
    return BinaryExpression(leftExpression, operator, rightExpression)
}

/** Value Generators **/
fun Int.toValue(): Value = Value(com.github.allepilli.kapl.types.Int, toLong())