package day12

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import readTestInputLines
import utils.readInputLines

class PassagePathingTest {

    private val passagePathing = PassagePathing()

    companion object {
        private val smallInput = readTestInputLines(12, "smallInput.txt")
        private val mediumInput = readTestInputLines(12, "mediumInput.txt")
        private val largeInput = readTestInputLines(12, "largeInput.txt")
        private val actualInput = readInputLines(12)

        @JvmStatic
        fun getInputsForPart1(): List<Arguments> {
            return listOf(
                Arguments.of(smallInput, 10),
                Arguments.of(mediumInput, 19),
                Arguments.of(largeInput, 226),
                Arguments.of(actualInput, 3230),
            )
        }

        @JvmStatic
        fun getInputsForPart2(): List<Arguments> {
            return listOf(
                Arguments.of(smallInput, 36),
                Arguments.of(mediumInput, 103),
                Arguments.of(largeInput, 3509),
                Arguments.of(actualInput, 83475),
            )
        }
    }

    @ParameterizedTest
    @MethodSource("getInputsForPart1")
    fun `find number of paths`(input: List<String>, expectedNumberOfPaths: Int) {
        val caveConnections = passagePathing.getCaveConnections(input)
        val paths = passagePathing.findNumberOfPaths(caveConnections)
        assertThat(paths).isEqualTo(expectedNumberOfPaths)
    }

    @ParameterizedTest
    @MethodSource("getInputsForPart2")
    fun `find number of paths allowing one small cave to be revisited`(input: List<String>, expectedNumberOfPaths: Int) {
        val caveConnections = passagePathing.getCaveConnections(input)
        val paths = passagePathing.findNumberOfPathsAllowingOneSmallCaveToBeVisitedTwice(caveConnections)
        assertThat(paths).isEqualTo(expectedNumberOfPaths)
    }
}
