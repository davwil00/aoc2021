package day05

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import utils.Coordinate
import utils.readInputLines

class HydrothermalVentureTest {

    private val hydrothermalVenture = HydrothermalVenture()
    val testInput = """0,9 -> 5,9
8,0 -> 0,8
9,4 -> 3,4
2,2 -> 2,1
7,0 -> 7,4
6,4 -> 2,0
0,9 -> 2,9
3,4 -> 1,4
0,0 -> 8,8
5,5 -> 8,2""".trimIndent()

	@ParameterizedTest
    @MethodSource("expandCoordinateRangeSource")
    fun `expandCoordinateRange correctly provides all coordinates`(coordinateRange: CoordinateRange, expected: List<Coordinate<Int>>) {
        val actual = hydrothermalVenture.expandCoordinateRange(coordinateRange)
        assertThat(actual).containsExactlyInAnyOrder(*expected.toTypedArray())
    }

    @Test
    fun `count dangerous horizontal or vertical vents`() {
        val ventLocations = hydrothermalVenture.parseInput(testInput.lines())
        assertThat(hydrothermalVenture.countDangerousAreas(ventLocations, false)).isEqualTo(5)
    }

    @Test
    fun `count dangerous horizontal or vertical vents for full input`() {
        val ventLocations = hydrothermalVenture.parseInput(readInputLines(5))
        assertThat(hydrothermalVenture.countDangerousAreas(ventLocations, false)).isEqualTo(6710)
    }

    @Test
    fun `count all dangerous vents`() {
        val ventLocations = hydrothermalVenture.parseInput(testInput.lines())
        assertThat(hydrothermalVenture.countDangerousAreas(ventLocations, true)).isEqualTo(12)
    }

    @Test
    fun `count dangerous vents for full input`() {
        val ventLocations = hydrothermalVenture.parseInput(readInputLines(5))
        assertThat(hydrothermalVenture.countDangerousAreas(ventLocations, true)).isEqualTo(20121)
    }

    companion object {

        @JvmStatic
        fun expandCoordinateRangeSource(): List<Arguments> = listOf(
            arguments(CoordinateRange(Coordinate(1, 1), Coordinate(1, 3)), listOf(Coordinate(1, 1), Coordinate(1, 2), Coordinate(1, 3))),
            arguments(CoordinateRange(Coordinate(9, 7), Coordinate(7, 7)), listOf(Coordinate(9, 7), Coordinate(8, 7), Coordinate(7, 7))),
            arguments(CoordinateRange(Coordinate(1, 1), Coordinate(3, 3)), listOf(Coordinate(1, 1), Coordinate(2, 2), Coordinate(3, 3))),
            arguments(CoordinateRange(Coordinate(9, 7), Coordinate(7, 9)), listOf(Coordinate(9, 7), Coordinate(8, 8), Coordinate(7, 9)))
        )
    }
}
