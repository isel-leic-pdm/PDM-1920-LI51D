package edu.isel.adeetc.expressionevaluator

fun evaluate(ast: ASTNode): Int =
    when (ast) {
        is ValueNode -> ast.number
        is OperationNode -> when (ast.symbol) {
            '+' -> evaluate(ast.left) + evaluate(ast.right)
            '-' -> evaluate(ast.left) - evaluate(ast.right)
            '*' -> evaluate(ast.left) * evaluate(ast.right)
            '/' -> evaluate(ast.left) / evaluate(ast.right)
            else -> throw InvalidOperationException()
        }
        else -> throw InvalidOperationException()
    }

fun prettyPrint(ast: ASTNode): String =
    when (ast) {
        is ValueNode -> ast.number.toString()
        is OperationNode -> "${prettyPrint(ast.left)} ${ast.symbol} ${prettyPrint(ast.right)}"
        else -> throw InvalidOperationException()
    }
