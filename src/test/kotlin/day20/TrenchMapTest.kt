package day20

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import readTestInput
import utils.Coordinate
import utils.readInput

class TrenchMapTest {

    private val trenchMap = TrenchMap()

	@Test
    fun `enhance test image twice`() {
        val testInput = readTestInput(21)
        val imageEnhancementData = trenchMap.readInput(testInput)
        assertThat(trenchMap.enhanceImage(2, imageEnhancementData)).isEqualTo(35)
    }

    @Test
    fun `getSurroundingCoordinates`() {
        val coordinate = Coordinate(-1, 0)
        assertThat(coordinate.getAdjacentCoordinates(Int.MIN_VALUE, Int.MIN_VALUE).toList()).hasSize(4)
        assertThat(coordinate.getAdjacentCoordinatesIncludingDiagonals(Int.MIN_VALUE, Int.MIN_VALUE).toList()).hasSize(8)
        assertThat(trenchMap.getSurroundingPixels(coordinate).toList()).hasSize(9)
    }

    @ParameterizedTest
    @CsvSource("2,5461", "50,18226")
    fun `enhance real image`(repeats: Int, expected: Int) {
        val input = readInput(20)
        val imageEnhancementData = trenchMap.readInput(input)
        val numberOfLitPixelsInEnhancedImage = trenchMap.enhanceImage(repeats, imageEnhancementData)
        assertThat(numberOfLitPixelsInEnhancedImage).isEqualTo(expected)
    }
}
