package edu.isel.adeetc.expressionevaluator

import java.lang.Exception

fun evaluate(tokens: MutableList<String>): Int {
    val token = tokens.removeAt(0)
    val number: Int? = token.toIntOrNull()

    if (number != null)
        return number

    val left = evaluate(tokens)
    val right = evaluate(tokens)

    return when (token) {
        "+" -> left + right
        "*" -> left * right
        "/" -> left / right
        "-" -> left - right
        else -> throw InvalidOperationException()
    }
}

class InvalidOperationException : Exception() {

}

fun evaluate(expression: String): Int {
    val tokens = expression.split(" ")
    return evaluate(tokens.toMutableList())
}