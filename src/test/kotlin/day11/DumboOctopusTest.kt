package day11

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import utils.readInputLines

class DumboOctopusTest {

    val testInput = """5483143223
2745854711
5264556173
6141336146
6357385478
4167524645
2176841721
6882881134
4846848554
5283751526""".lines()
    private val dumboOctopus = DumboOctopus()

	@Test
    fun `should count total flashes for small input`() {
        val testInput = """11111
19991
19191
19991
11111""".lines()
        val octopuses = dumboOctopus.parseInput(testInput)
        assertThat(dumboOctopus.countTotalFlashes(2, octopuses)).isEqualTo(9)
    }

    @ParameterizedTest
    @CsvSource(
        "10,204",
        "100,1656"
    )
    fun `should count total flashes`(steps: Int, totalFlashes: Int) {
        val octopuses = dumboOctopus.parseInput(testInput)
        assertThat(dumboOctopus.countTotalFlashes(steps, octopuses)).isEqualTo(totalFlashes)
    }

    @Test
    fun `should count total flashes for full input`() {
        val octopuses = dumboOctopus.parseInput(readInputLines(11))
        assertThat(dumboOctopus.countTotalFlashes(100, octopuses)).isEqualTo(1634)
    }

    @Test
    fun `should find flash point`() {
        val octopuses = dumboOctopus.parseInput(testInput)
        assertThat(dumboOctopus.findFlashPoint(octopuses)).isEqualTo(195)
    }

    @Test
    fun `should find flash point for full input`() {
        val octopuses = dumboOctopus.parseInput(readInputLines(11))
        assertThat(dumboOctopus.findFlashPoint(octopuses)).isEqualTo(210)
    }
}
