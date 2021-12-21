package day21

import utils.asInt
import utils.readInputLines
import kotlin.math.max

class DiracDice {

    fun getStartingPositions(input: List<String>): List<Player> {
        val (player1, player2) = input.mapIndexed { index, startPos ->
            Player(index + 1, startPos.last().asInt())
        }
        return listOf(player1, player2)
    }

    fun playPracticeGame(players: List<Player>): Int {
        val dice = DeterministicDice()
        while (players.none { it.hasWon(1000) }) {
            players[0].haveTurn(dice.getSumOfThreeDiceRolls())
            if (!players[0].hasWon(1000)) {
                players[1].haveTurn(dice.getSumOfThreeDiceRolls())
            }
        }

        val loser = players.first { player -> !player.hasWon(1000) }
        return loser.score * dice.rolls
    }

    data class GameState(val p1: Player, val p2: Player)

    fun playGame(players: List<Player>): Long {
        val dice = DiracDice()
        var player1Wins = 0L
        var player2Wins = 0L
        var gameStates = mapOf(GameState(players[0], players[1]) to 1L)

        while (gameStates.isNotEmpty()) {
            gameStates = gameStates.flatMap { (gameState, count) ->
                dice.getSumOfThreeDiceRolls().flatMap { player1Dice ->
                    val player1 = gameState.p1.copy().haveTurn(player1Dice)
                    if (player1.hasWon(21)) {
                        player1Wins += count
                        emptyList()
                    } else {
                        dice.getSumOfThreeDiceRolls().mapNotNull { player2Dice ->
                            val player2 = gameState.p2.copy().haveTurn(player2Dice)
                            if (player2.hasWon(21)) {
                                player2Wins += count
                                null
                            } else {
                                Pair(GameState(player1.copy(), player2.copy()), count)
                            }
                        }
                    }
                }
            }.groupingBy { it.first }.fold(0L) { acc, (_, count) -> acc + count }
        }

        return max(player1Wins, player2Wins)
    }

    data class Player(val playerNo: Int, var currentPosition: Int, var score: Int = 0) {

        fun hasWon(target: Int) = score >= target

        fun haveTurn(diceRoll: Int): Player {
            move(diceRoll)
            score += currentPosition
            return this
        }

        private fun move(spaces: Int) {
            repeat(spaces % 10) {
                if (currentPosition == 10) {
                    currentPosition = 1
                } else {
                    currentPosition++
                }
            }
        }
    }

    class DeterministicDice {
        var currentValue = 0
        var rolls = 0

        fun getSumOfThreeDiceRolls(): Int {
            return (0 until 3).sumOf {
                rolls++
                if (currentValue == 100) {
                    currentValue = 1
                } else {
                    currentValue++
                }

                currentValue
            }
        }
    }

    class DiracDice {
        fun getSumOfThreeDiceRolls(): List<Int> {
            return diceRolls
        }

        companion object {
            private val diceRolls = listOf(
                3,
                4, 4, 4,
                5, 5, 5, 5, 5, 5, //6
                6, 6, 6, 6, 6, 6, 6, //7
                7, 7, 7, 7, 7, 7, //6
                8, 8, 8,
                9
            )
        }
    }
}

fun main() {
    val input = readInputLines(21)
    val diracDice = DiracDice()
    val practiceGameResult = diracDice.playPracticeGame(diracDice.getStartingPositions(input))
    println("Losing player score * dice rolls in practice game: $practiceGameResult")
    val realGameResult = diracDice.playGame(diracDice.getStartingPositions(input))
    println("Winning player wins in $realGameResult universes")
}
