package day01

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SonarSweepTest {

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
        assertThat(SonarSweep().findIncrements(input)).isEqualTo(7)
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
        assertThat(SonarSweep().findIncrementsWithWindow(input, 3)).isEqualTo(5)
    }
}
