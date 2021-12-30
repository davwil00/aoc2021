package day18

import day18.SnailfishNumber.Direction.LEFT
import day18.SnailfishNumber.Direction.RIGHT
import utils.isDigits
import utils.readInputLines
import utils.splitToString
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor

fun addAndReduceList(snailfishNumbers: List<String>): SnailfishNumber {
    return snailfishNumbers.map { SnailfishNumber.fromString(it) }
        .reduce { first, second ->
//            println("added = ${first + second}")
            reduce(first + second)
        }
}

fun reduce(snailfishNumber: SnailfishNumber): SnailfishNumber {
    while(snailfishNumber.explode() || snailfishNumber.split()) {
//        println(snailfishNumber)
    }

//    println("""
//        --------------------------------------
//        $snailfishNumber
//        --------------------------------------
//    """.trimIndent())
    return snailfishNumber
}

class SnailfishNumber(private var parent: SnailfishNumber?) {

    constructor(parent: SnailfishNumber?, left: Int, right: Int): this(parent) {
        this.leftValue = left
        this.rightValue = right
    }

    var leftValue: Int? = null
    var rightValue: Int? = null
    var leftNode: SnailfishNumber? = null
    var rightNode: SnailfishNumber? = null

    private fun getLeft() = leftNode ?: leftValue

    fun setLeft(left: Any) {
        when (left) {
            is Int -> leftValue = left
            is SnailfishNumber -> leftNode = left
            else -> throw IllegalArgumentException("Unexpected value type $left")
        }
    }

    private fun getRight() = rightNode ?: rightValue

    fun setRight(right: Any) {
        when (right) {
            is Int -> rightValue = right
            is SnailfishNumber -> rightNode = right
            else -> throw IllegalArgumentException("Unexpected value type $right")
        }
    }

    private fun getDepth(): Int {
        return if (this.parent == null) 0 else 1 + this.parent!!.getDepth()
    }

    fun explode(): Boolean {
        if (this.getDepth() == 4) {
            val nodeToExplode = this
            val leafNodes = getAllLeafNodes()
            val thisIdx = leafNodes.indexOfFirst { it.parent === nodeToExplode }
            if (thisIdx > 0) {
                val node = leafNodes[thisIdx - 1]
                val newValue = node.value + nodeToExplode.leftValue!!
                when (node.direction) {
                    LEFT -> node.parent.leftValue = newValue
                    RIGHT -> node.parent.rightValue = newValue
                }
            }
            if (thisIdx + 2 < leafNodes.size) {
                val node = leafNodes[thisIdx + 2]
                val newValue = nodeToExplode.rightValue!!
                when (node.direction) {
                    LEFT -> node.parent.leftValue = node.value + newValue
                    RIGHT -> node.parent.rightValue = node.value + newValue
                }
            }
            if (this.parent!!.leftNode === nodeToExplode) {
                this.parent!!.leftNode = null
                this.parent!!.leftValue = 0
            } else {
                this.parent!!.rightNode = null
                this.parent!!.rightValue = 0
            }

//            print("Exploded: ")
            return true
        }

        return (leftNode != null && leftNode!!.explode()) ||
                (rightNode != null && rightNode!!.explode())
    }

    fun split(): Boolean {
        val node = getAllLeafNodes().firstOrNull { it.value >= 10 }
        if (node != null) {
            val newValue = split(node.value, node.parent)
            when (node.direction) {
                LEFT -> {
                    node.parent.leftValue = null
                    node.parent.leftNode = newValue
                }
                RIGHT -> {
                    node.parent.rightValue = null
                    node.parent.rightNode = newValue
                }
            }
            return true
        }
        return false
    }

    fun getMagnitude(): Long {
        val left = if (leftValue != null) leftValue!! * 3L else leftNode!!.getMagnitude() * 3
        val right = if (rightValue != null) rightValue!! * 2L else rightNode!!.getMagnitude() * 2

        return left + right
    }

    data class LeafNode(val value: Int, val parent: SnailfishNumber, val direction: Direction)
    enum class Direction { LEFT, RIGHT }

    private fun getAllLeafNodes(): List<LeafNode> {
        if (this.parent != null) {
            return parent!!.getAllLeafNodes()
        }

        return getChildLeafNodes()
    }

    private fun getChildLeafNodes(): List<LeafNode> {
        val nodes = mutableListOf<LeafNode>()
        if (this.leftNode != null) {
            nodes.addAll(this.leftNode!!.getChildLeafNodes())
        }
        if (this.leftValue != null) {
            nodes.add(LeafNode(this.leftValue!!, this, LEFT))
        }
        if (this.rightNode != null) {
            nodes.addAll(this.rightNode!!.getChildLeafNodes())
        }
        if (this.rightValue != null) {
            nodes.add(LeafNode(this.rightValue!!, this, RIGHT))
        }

        return nodes
    }

    operator fun plus(other: SnailfishNumber) : SnailfishNumber {
        val newParent = SnailfishNumber(null)
        newParent.setLeft(this)
        this.parent = newParent
        newParent.setRight(other)
        other.parent = newParent

        return newParent
    }

    override fun toString(): String {
        return "[${this.getLeft()},${this.getRight()}]"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SnailfishNumber

        if (leftValue != other.leftValue) return false
        if (rightValue != other.rightValue) return false
        if (leftNode != other.leftNode) return false
        if (rightNode != other.rightNode) return false

        return true
    }

    override fun hashCode(): Int {
        var result = leftValue ?: 0
        result = 31 * result + (rightValue ?: 0)
        result = 31 * result + (leftNode?.hashCode() ?: 0)
        result = 31 * result + (rightNode?.hashCode() ?: 0)
        return result
    }

    companion object {
        fun fromString(snailfishNumberStr: String) =
            process(LinkedList(snailfishNumberStr.splitToString())) as SnailfishNumber

        fun split(digit: Int, parent: SnailfishNumber) = SnailfishNumber(
            parent,
            floor(digit / 2.0).toInt(),
            ceil(digit / 2.0).toInt()
        )

        private fun process(input: LinkedList<String>, parent: SnailfishNumber? = null): Any {
            val char = input.pop()!!
            return when {
                char == "[" -> {
                    val snailfishNumber = SnailfishNumber(parent)
                    snailfishNumber.setLeft(process(input, snailfishNumber))
                    snailfishNumber.setRight(process(input, snailfishNumber))
                    snailfishNumber
                }
                char.isDigits() -> {
                    if (input.peek().isDigits()) {
                        (char + input.pop()).toInt()
                    } else {
                        char.toInt()
                    }
                }
                else -> process(input, parent)
            }
        }
    }
}


fun main() {
    val input = readInputLines(18)
    val result = addAndReduceList(input)
    println(result.getMagnitude())
}
