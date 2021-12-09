package day09

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import utils.readInputLines

class SmokeBasinTest {

    private val testInput = """2199943210
3987894921
9856789892
8767896789
9899965678""".lines()
    private val smokeBasin = SmokeBasin()

	@Test
    fun `should find sum of low points`() {
        val map = smokeBasin.produceMap(testInput)
        assertThat(smokeBasin.findRiskLevelsOfLowPoints(map)).isEqualTo(15)
    }

	@Test
    fun `should find sum of low points for full input`() {
        val map = smokeBasin.produceMap(readInputLines(9))
        assertThat(smokeBasin.findRiskLevelsOfLowPoints(map)).isEqualTo(580)
    }

	@Test
    fun `should find 3 largest basins`() {
        val map = smokeBasin.produceMap(testInput)
        assertThat(smokeBasin.findBasin(map)).isEqualTo(1134)
    }

	@Test
    fun `should find 3 largest basins for full input`() {
        val map = smokeBasin.produceMap(readInputLines(9))
        assertThat(smokeBasin.findBasin(map)).isEqualTo(856716)
    }
}
