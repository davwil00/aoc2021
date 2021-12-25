package day25

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import utils.readInputLines

class SeaCucumberTest {

    private val seaCucumber = SeaCucumber()
    private val testInput = """v...>>.vv>
.vv>>.vv..
>>.>v>...v
>>v>>.>.v.
v>v.vv.v..
>.>>..v...
.vv..>.>v.
v.v..>>v.v
....v..v.>""".lines()

	@Test
    fun `should detect when the sea cucumbers stop moving`() {
        val map = seaCucumber.readInput(testInput)
        assertThat(seaCucumber.findWhenSeaCucumberStopMoving(map)).isEqualTo(58)
    }

	@Test
    fun `should detect when the sea cucumbers stop moving for the real input`() {
        val map = seaCucumber.readInput(readInputLines(25))
        assertThat(seaCucumber.findWhenSeaCucumberStopMoving(map)).isEqualTo(571)
    }
}
