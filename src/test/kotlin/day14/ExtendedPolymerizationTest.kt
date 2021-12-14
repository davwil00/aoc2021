package day14

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import utils.readInputLines

class ExtendedPolymerizationTest {

    private val extendedPolymerization = ExtendedPolymerization()
    private val testInput = """NNCB

CH -> B
HH -> N
CB -> H
NH -> C
HB -> C
HC -> B
HN -> C
NN -> C
BH -> H
NC -> B
NB -> B
BN -> B
BB -> N
BC -> B
CC -> N
CN -> C""".lines()

	@Test
    fun `should find difference between max and min polymer count`() {
        val manualData = extendedPolymerization.parseInput(testInput)
        assertThat(extendedPolymerization.findOptimalFormula(manualData, 10)).isEqualTo(1588)
    }

	@Test
    fun `should find difference between max and min polymer count2`() {
        val manualData = extendedPolymerization.parseInput(testInput)
        assertThat(extendedPolymerization.findOptimalFormula(manualData, 10)).isEqualTo(1588)
    }

	@Test
    fun `should find difference between max and min polymer count2 40`() {
        val manualData = extendedPolymerization.parseInput(testInput)
        assertThat(extendedPolymerization.findOptimalFormula(manualData, 40)).isEqualTo(2188189693529)
    }

	@Test
    fun `should find difference between max and min polymer count for full input`() {
        val manualData = extendedPolymerization.parseInput(readInputLines(14))
        assertThat(extendedPolymerization.findOptimalFormula(manualData, 10)).isEqualTo(2712)
    }

	@Test
    fun `should find difference between max and min polymer count for full input2`() {
        val manualData = extendedPolymerization.parseInput(readInputLines(14))
        assertThat(extendedPolymerization.findOptimalFormula(manualData, 10)).isEqualTo(2712)
    }
}
