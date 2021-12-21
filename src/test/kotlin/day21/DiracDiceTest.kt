package day21

import day21.DiracDice.Player
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import utils.readInputLines

class DiracDiceTest {

    private val diracDice = DiracDice()

	@Test
    fun `simulate practice game`() {
        val players = listOf(
            Player(1, 4),
            Player(2, 8)
        )
        val result = diracDice.playPracticeGame(players)

        assertThat(result).isEqualTo(739785)
    }

	@Test
    fun `simulate practice game on actual input`() {
        val input = readInputLines(21)
        val players = diracDice.getStartingPositions(input)
        val result = diracDice.playPracticeGame(players)

        assertThat(result).isEqualTo(1073709)
    }

	@Test
    fun `simulate game`() {
        val players = listOf(
            Player(1, 4),
            Player(2, 8)
        )
        val result = diracDice.playGame(players)

        assertThat(result).isEqualTo(444356092776315)
    }

	@Test
    fun `simulate game for actual input`() {
        val input = readInputLines(21)
        val players = diracDice.getStartingPositions(input)
        val result = diracDice.playGame(players)

        assertThat(result).isEqualTo(148747830493442)
    }

}


/**
 * 111 = 3 1
 * 112 = 4 1
 * 113 = 5 8
 * 121 = 4
 * 122 = 5
 * 123 = 6 7
 * 131 = 5
 * 132 = 6
 * 133 = 7 6
 * 211 = 4
 * 212 = 5
 * 213 = 6
 * 221 = 5
 * 222 = 6
 * 223 = 7
 * 231 = 6
 * 232 = 7
 * 233 = 8 3
 * 311 = 5
 * 312 = 6
 * 313 = 7
 * 321 = 6
 * 322 = 7
 * 323 = 8
 * 331 = 7
 * 332 = 8
 * 333 = 9 1
 */