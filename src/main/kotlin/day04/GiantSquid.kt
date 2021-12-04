package day04

import utils.readInput
import utils.takeWhileInclusive

class GiantSquid {
    fun parseInput(input: String): Game {
        val parts = input.split("\n\n")
        val numbers = parts[0].split(",").map(String::toInt)
        val boards = parts.subList(1, parts.size).map(Board::fromString)
        return Game(numbers, boards)
    }

    class Game(private val numbers: List <Int>, private val boards: List<Board>) {
        private fun callNumber(number: Int): Boolean {
            return boards
                .onEach { board -> board.markNumberIfExists(number) }
                .onEach { board -> board.checkForWin() }
                .any { board -> board.hasWon }
        }

        fun playToWin(): Int {
            val lastNumberCalled = numbers
                .takeWhileInclusive {
                    !callNumber(it)
                }
                .last()
            val sumOfUnmarkedNumbers = boards.first { it.hasWon }.sumUnmarkedNumbers()
            return lastNumberCalled * sumOfUnmarkedNumbers
        }

        fun playToLose(): Int {
            var lastNumberCalled = 0
            var losingBoard: Board? = null

            while (!boards.all { it.hasWon }) {
                callNumber(numbers[lastNumberCalled])
                val boardsNotWon = boards.filterNot { board -> board.hasWon }
                if (boardsNotWon.size == 1) {
                    losingBoard = boardsNotWon.first()
                }
                lastNumberCalled++
            }

            return numbers[lastNumberCalled -1 ] * losingBoard!!.sumUnmarkedNumbers()
        }
    }

    class Board(private val numbers: List<List<BingoNumber>>) {
        var hasWon = false

        fun markNumberIfExists(number: Int) {
            numbers
                .flatten()
                .any { bingoNumber -> bingoNumber.markIfMatches(number) }
        }

        fun checkForWin() {
            hasWon = hasWon || (numbers.any { row -> hasWon(row) } ||
                (numbers.indices).map { rowIdx ->
                    (0 until numbers[rowIdx].size).map { colIdx ->
                        numbers[colIdx][rowIdx]
                    }
                }.any { hasWon(it) })
        }

        fun sumUnmarkedNumbers() = numbers
            .flatten()
            .filterNot { it.state }
            .sumOf { it.number }

        private fun hasWon(bingoNumbers: List<BingoNumber>) = bingoNumbers.all { it.state }

        companion object {
            private val WHITESPACE = Regex("""\s+""")
            fun fromString(string: String) =
                Board(string.lines().map { line ->
                    line.trimStart().split(regex = WHITESPACE)
                        .map(String::toInt)
                        .map(::BingoNumber)
                })
        }
    }

    data class BingoNumber(val number: Int, var state: Boolean = false) {
        fun markIfMatches(number: Int): Boolean {
            return (number == this.number)
                .also { if (it) this.state = true }
        }
    }
}

fun main() {
    val input = readInput(4)
    val giantSquid = GiantSquid()
    val game = giantSquid.parseInput(input)
    val winningScore = game.playToWin()
    println("Winning score is $winningScore")
    val losingScore = game.playToLose()
    println("Losing score is $losingScore")
}
