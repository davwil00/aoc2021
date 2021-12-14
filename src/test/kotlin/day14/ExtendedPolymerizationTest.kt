package day14

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
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

	@ParameterizedTest
    @CsvSource(
        "10,1588",
        "40,2188189693529"
    )
    fun `should find difference between max and min polymer count for {0} iterations`(iterations: Int, expected: Long) {
        val manualData = extendedPolymerization.parseInput(testInput)
        assertThat(extendedPolymerization.findOptimalFormula(manualData, iterations)).isEqualTo(expected)
    }

    @ParameterizedTest
    @CsvSource(
        "10,2712",
        "40,8336623059567"
    )
    fun `should find difference between max and min polymer count for full input and {0} iterations`(iterations: Int, expected: Long) {
        val manualData = extendedPolymerization.parseInput(readInputLines(14))
        assertThat(extendedPolymerization.findOptimalFormula(manualData, iterations)).isEqualTo(expected)
    }
}
