package day06

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LanternfishTest {

    val testInput = "3,4,3,1,2"

	@Test
    fun `should simulate correct number of lanternfish after 18 days`() {
        val lanternfish = createLaternfishFromInput(testInput)
        val actual = simulateDays(18, lanternfish)
        assertThat(actual).isEqualTo(26)
    }

	@Test
    fun `should simulate correct number of lanternfish after 80 days`() {
        val lanternfish = createLaternfishFromInput(testInput)
        val actual = simulateDays(80, lanternfish)
        assertThat(actual).isEqualTo(5934)
    }

	@Test
    fun `should simulate correct number of lanternfish after 256 days`() {
        val lanternfish = createLaternfishFromInput(testInput)
        val actual = simulateDays(256, lanternfish)
        assertThat(actual).isEqualTo(26984457539)
    }
}
