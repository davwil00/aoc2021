package day21

import day21.DiracDice.Player
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DiracDiceTest {

    private val diracDice = DiracDice()

	@Test
    fun `simulate test game`() {
        val players = listOf(
            Player(1, 4),
            Player(2, 8)
        )
        val result = diracDice.playPracticeGame(players)

        assertThat(result).isEqualTo(739785)
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
}


/**
 * 111 = 3 1
 * 112 = 4 1
 * 113 = 5 8
 * 121 = 5
 * 122 = 5
 * 123 = 6 7
 * 131 = 5
 * 132 = 6
 * 133 = 7 6
 * 211 = 5
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