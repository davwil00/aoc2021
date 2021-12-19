package day18

import day18.SnailfishNumber.ExplodeCandidate.Direction.LEFT
import day18.SnailfishNumber.ExplodeCandidate.Direction.RIGHT
import day18.SnailfishNumber.ExplodeCandidate.Status.*
import utils.isDigit
import utils.readInputLines
import utils.splitToString
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor


fun reduce(snailfishNumber: SnailfishNumber) {
    var exploded = snailfishNumber.explode()
    while(exploded) {
        println("Exploded: $snailfishNumber")
        exploded = snailfishNumber.explode()
    }
    val split = snailfishNumber.split()
    if (split) { println("Split: $snailfishNumber") }
    if (split || exploded) {
        reduce(snailfishNumber)
    }
}

data class SnailfishNumber(var depth: Int, var left: Any, var right: Any) {
    operator fun plus(other: SnailfishNumber) = SnailfishNumber(0, this.incrementDepth(), other.incrementDepth())

    fun asIntPair() = Pair(left as Int, right as Int)

    private fun incrementDepth(): SnailfishNumber {
        this.depth++
        if (left is SnailfishNumber) {
            (left as SnailfishNumber).incrementDepth()
        }
        if (right is SnailfishNumber) {
            (right as SnailfishNumber).incrementDepth()
        }

        return this
    }

    fun explode(): Boolean {
        val explodeCandidate = ExplodeCandidate()
        findExplodeCandidate(explodeCandidate)
        return explodeCandidate.hasFoundPair()
    }

    fun split(): Boolean {
        if (left is Int && left as Int > 9) {
            left = split(left as Int, depth + 1)
            return true
        } else if (right is Int && right as Int > 9) {
            right = split(right as Int, depth + 1)
            return true
        } else {
            if (left is SnailfishNumber && (left as SnailfishNumber).split()) {
                return true
            } else if (right is SnailfishNumber && (right as SnailfishNumber).split()) {
                return true
            }
        }

        return false
    }

    private fun findExplodeCandidate(explodeCandidate: ExplodeCandidate) {
        if (!explodeCandidate.hasFoundPair()) {
            if (this.depth == 4) {
                explodeCandidate.found(this.asIntPair())
            } else if (left is Int) {
                explodeCandidate.updateLeftValue(this, LEFT)
                exploreRight(explodeCandidate)
            } else if (left is SnailfishNumber) {
                (left as SnailfishNumber).findExplodeCandidate(explodeCandidate.explore(EXPLORING_LEFT))
                if (explodeCandidate.hasFoundPair()) {
                    processFoundPair(explodeCandidate)
                }
                if (explodeCandidate.status == EXPLODED_RIGHT) {
                    if (right is Int) {
                        right = (right as Int) + explodeCandidate.pair!!.second
                        explodeCandidate.updatedRight = true
                    } else {
                        addToFirstInt(right as SnailfishNumber, explodeCandidate.pair!!.second)
                    }
                } else {
                    exploreRight(explodeCandidate)
                }
            } else if (right is Int) {
                explodeCandidate.updateLeftValue(this, RIGHT)
            } else {
                exploreRight(explodeCandidate)
            }
        }
    }

    private fun exploreRight(explodeCandidate: ExplodeCandidate) {
        if (right is SnailfishNumber) {
            (right as SnailfishNumber).findExplodeCandidate(explodeCandidate.explore(EXPLORING_RIGHT))
            if (explodeCandidate.hasFoundPair()) {
                processFoundPair(explodeCandidate)
            }
        }
    }

    private fun processFoundPair(explodeCandidate: ExplodeCandidate) {
        if (!explodeCandidate.explodedPair) {
            if (explodeCandidate.status == FOUND_ON_LEFT) {
                left = 0
                explodeCandidate.status = EXPLODED_LEFT
                if (right is Int) {
                    right = (right as Int) + explodeCandidate.pair!!.second
                    explodeCandidate.updatedRight = true
                }
            } else {
                right = 0
                explodeCandidate.status = EXPLODED_RIGHT
            }
            explodeCandidate.explodedPair = true
        }
        // if !updated left
    }

    private fun addToFirstInt(snailfishNumber: SnailfishNumber, number: Int) {
        if (snailfishNumber.left is Int) {
            snailfishNumber.left = snailfishNumber.left as Int + number
        } else {
            addToFirstInt(snailfishNumber.left as SnailfishNumber, number)
        }
    }

    class ExplodeCandidate() {
        var pair: Pair<Int, Int>? = null
        private var leftValue: SnailfishNumber? = null
        var updatedLeft = false
        var updatedRight = false
        var explodedPair = false
        var status: Status = EXPLORING_LEFT
        private var leftValueDirection: Direction? = null

        fun hasFoundPair() = pair != null
        fun hasLeftValue() = leftValue != null

        fun updateLeftValue(value: SnailfishNumber, direction: Direction) {
            leftValue = value
            leftValueDirection = direction
        }

        fun found(pair: Pair<Int, Int>) {
            this.pair = pair
            this.status = if (status == EXPLORING_LEFT) FOUND_ON_LEFT else FOUND_ON_RIGHT
            leftValue?.let {
                when (leftValueDirection) {
                    LEFT -> leftValue!!.left = (leftValue!!.left as Int) + pair.first
                    RIGHT -> leftValue!!.right = (leftValue!!.right as Int) + pair.first
                    else -> {}
                }
                this.updatedLeft = true
            }
        }

        fun explore(status: Status): ExplodeCandidate {
            this.status = status
            return this
        }

        enum class Direction { LEFT, RIGHT }

        enum class Status {
            EXPLORING_LEFT,
            EXPLORING_RIGHT,
            FOUND_ON_LEFT,
            FOUND_ON_RIGHT,
            EXPLODED_LEFT,
            EXPLODED_RIGHT
        }
    }

    override fun toString(): String {
        return "[${this.left},${this.right}]"
    }

    companion object {
        fun fromString(snailfishNumberStr: String): SnailfishNumber {
            return process(LinkedList(snailfishNumberStr.splitToString())) as SnailfishNumber
        }

        fun split(digit: Int, depth: Int): SnailfishNumber {
            return SnailfishNumber(depth, floor(digit / 2.0).toInt(), ceil(digit / 2.0).toInt())
        }

        private fun process(input: LinkedList<String>, depth: Int = 0, left: Any? = null): Any {
            val char = input.pop()!!
            return when {
                char == "[" -> SnailfishNumber(depth, process(input, depth + 1), process(input, depth + 1))
                char.isDigit() -> {
                    left?.let {
                        if (left is SnailfishNumber) {
                            SnailfishNumber(depth, left.copy(depth = depth), char.toInt())
                        } else {
                            SnailfishNumber(depth, left, char.toInt())
                        }
                    }
                    char.toInt()
                }
                else -> process(input, depth, left)
            }
        }
    }
}


fun main() {
    val input = readInputLines(18)
//    val snailfish = Snailfish()
}
