package day01

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import utils.readInputLines

class SonarSweepTest {

    private val sonarSweep = SonarSweep()

    @Test
    fun `should find total depth increases`() {
        val input = """199
200
208
210
200
207
240
269
260
263""".lines()
        assertThat(sonarSweep.findIncrements(input)).isEqualTo(7)
    }

    @Test
    fun `should produce the right answer for part 1`() {
        val input = readInputLines(1)
        assertThat(sonarSweep.findIncrements(input)).isEqualTo(1393)
    }

    @Test
    fun `should find total depth increases for window of size 3`() {
        val input = """199
200
208
210
200
207
240
269
260
263""".lines()
        assertThat(sonarSweep.findIncrementsWithWindow(input, 3)).isEqualTo(5)
    }

    @Test
    fun `should produce correct answer for part 2`() {
        val input = readInputLines(1)
        assertThat(sonarSweep.findIncrementsWithWindow(input, 3)).isEqualTo(1359)
    }
}
