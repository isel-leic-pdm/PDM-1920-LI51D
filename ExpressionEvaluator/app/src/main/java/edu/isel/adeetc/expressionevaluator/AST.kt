package edu.isel.adeetc.expressionevaluator

import java.lang.Exception

open class ASTNode

class OperationNode(val symbol: Char,
              val left: ASTNode,
              val right: ASTNode) : ASTNode()

class ValueNode(val number: Int) : ASTNode()

class InvalidOperationException : Exception()
