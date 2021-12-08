package day07

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import utils.readInputLines
import utils.readSingleInputLineOfIntsFromCsv

class TheTreacheryOfWhalesTest {

    private val theTreacheryOfWhales = TheTreacheryOfWhales()
    private val testInput = "16,1,2,0,4,2,7,1,2,14".split(",").map { it.toInt() }

	@Test
    fun `should find the horizontal position that uses the least fuel`() {
        assertThat(theTreacheryOfWhales.findCheapestFuelCost(testInput)).isEqualTo(37)
    }

	@Test
    fun `should find the horizontal position that uses the least fuel for all crabs`() {
        assertThat(theTreacheryOfWhales.findCheapestFuelCost(readSingleInputLineOfIntsFromCsv(7))).isEqualTo(333755)
    }

    @Test
    fun `should calculate fuel for step`() {
        assertThat(theTreacheryOfWhales.calculateFuelForStep(3)).isEqualTo(6)
    }

    @Test
    fun `should find the cheapest fuel cost with an understanding of crab engineering`() {
        assertThat(theTreacheryOfWhales.findCheapestFuelCost(testInput, true)).isEqualTo(168)
    }

    @Test
    fun `should find the cheapest fuel cost for all crabs with an understanding of crab engineering`() {
        assertThat(theTreacheryOfWhales.findCheapestFuelCost(readSingleInputLineOfIntsFromCsv(7), true)).isEqualTo(94017638)
    }
}
