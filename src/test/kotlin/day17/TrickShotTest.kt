package day17

import day17.TrickShot.TargetArea
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import utils.readInput

class TrickShotTest {

    private val trickShot = TrickShot()
    private val testInput = "target area: x=20..30, y=-10..-5"

	@Test
    fun `should parse target area`() {
        val actual = TargetArea.fromString(testInput)
        assertThat(actual.xLeft).isEqualTo(20)
        assertThat(actual.xRight).isEqualTo(30)
        assertThat(actual.yBottom).isEqualTo(-10)
        assertThat(actual.yTop).isEqualTo(-5)
    }

    @Test
    fun `calculate X velocity`() {
        val targetArea = TargetArea.fromString(testInput)
        assertThat(trickShot.calculateXVelocity(targetArea)).isEqualTo(6)
    }

    @Test
    fun `calculate max Y`() {
        val targetArea = TargetArea.fromString(testInput)
        assertThat(trickShot.calculateMaxY(6, targetArea)).isEqualTo(45)
    }

    @Test
    fun `calculate max Y full input`() {
        val targetArea = TargetArea.fromString(readInput(17))
        assertThat(trickShot.calculateMaxY(21, targetArea)).isEqualTo(7626)
    }

    @Test
    fun simulate() {
        val targetArea = TargetArea.fromString(testInput)
        val probe = trickShot.simulate(targetArea, 6, 9)

        assertThat(probe.isWithinTargetArea(targetArea)).isTrue
    }

    @Test
    fun `find all`() {
        val targetArea = TargetArea.fromString(testInput)
        assertThat(trickShot.findAll(targetArea)).isEqualTo(112)
    }

    @Test
    fun `find all full input`() {
        val targetArea = TargetArea.fromString(readInput(17))
        assertThat(trickShot.findAll(targetArea)).isEqualTo(2032)
    }
}
