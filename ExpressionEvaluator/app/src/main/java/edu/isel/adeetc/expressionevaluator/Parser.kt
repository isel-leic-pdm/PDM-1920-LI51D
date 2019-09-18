package edu.isel.adeetc.expressionevaluator

fun parse(tokens: MutableList<String>): ASTNode {
    val token = tokens.removeAt(0)
    val number: Int? = token.toIntOrNull()

    return if (number != null)
        ValueNode(number)
    else
        OperationNode(token.first(), parse(tokens), parse(tokens))
}

fun parse(expression: String) =
    parse(expression.split(" ").toMutableList())
