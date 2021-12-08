package day08

import day08.SevenSegmentSearch.Digits.*
import utils.containsAll
import utils.equalsIgnoringOrder
import utils.readInputLines
import utils.splitToString

class SevenSegmentSearch {

    fun parseInput(input: List<String>): List<Observation> {
        return input.map { it.split(" | ") }
             .map { (pattern, output) -> Observation(pattern.split(" "), output.split(" "))}
    }

    fun countSegmentsWithKnownSizes(observations: List<Observation>): Int {
        return observations.map { it.outputValue }
            .sumOf { outputValues -> outputValues.count { it.hasUniqueNumberOfSegments() } }
    }

    fun deriveMappings(observations: List<Observation>): Int {
        return observations.sumOf(this::findOutputValue)
    }

    fun findOutputValue(observation: Observation): Int {
        val eight = DigitMatch(8, observation.uniqueSignalPatterns.first { it.length == EIGHT.numberOfSegments() })
        val seven = DigitMatch(7, observation.uniqueSignalPatterns.first { it.length == SEVEN.numberOfSegments() })
        val four = DigitMatch(4, observation.uniqueSignalPatterns.first { it.length == FOUR.numberOfSegments() })
        val one = DigitMatch(1, observation.uniqueSignalPatterns.first { it.length == ONE.numberOfSegments() })

        val a = seven - one
        val six = DigitMatch(6, observation.uniqueSignalPatterns.first { it.length == SIX.numberOfSegments() && !it.containsAll(one.segments)})
        val c = eight - six
        val five = DigitMatch(5, observation.uniqueSignalPatterns.first { it.length == FIVE.numberOfSegments() && !it.contains(c) })
        val nine = DigitMatch(9, five.segments.splitToString().toSet().plus(one.segments.splitToString()).joinToString(""))
        val zero = DigitMatch(0, observation.uniqueSignalPatterns.first { it.length == ZERO.numberOfSegments() && !it.containsAll(nine.segments) && !it.containsAll(six.segments) })
//        val d = five - zero
        val e = eight - nine
        val three = DigitMatch(3, observation.uniqueSignalPatterns.first { it.length == THREE.numberOfSegments() && !it.containsAll(five.segments) && !it.contains(e) })
        val two = DigitMatch(2, observation.uniqueSignalPatterns.first { it.length == TWO.numberOfSegments() && !it.containsAll(five.segments) && !it.containsAll(three.segments) })
        val digits = listOf(zero, one, two, three, four, five, six, seven, eight, nine)
//        val f = one - two
//        val g = nine - four - a
//        val b = eight - two - f

        return observation.outputValue.map { value -> digits.first { digit -> digit.matches(value) }.digit }.joinToString("").toInt()
    }

    class Observation(val uniqueSignalPatterns: List<String>, val outputValue: List<String>)

    class DigitMatch(val digit: Int, val segments: String) {

        fun matches(value: String) = value.equalsIgnoringOrder(segments)

        operator fun minus(other: DigitMatch) = this.segments.splitToString().toSet().minus(other.segments.splitToString().toSet()).single()
    }

    private fun String.hasUniqueNumberOfSegments() = this.length in listOf(2, 4, 3, 7)

    enum class Digits(val segments: List<Int>) {
        ZERO(listOf(0, 1, 2, 4, 5, 6)),
        ONE(listOf(2, 5)),
        TWO(listOf(0, 2, 3, 4, 6)),
        THREE(listOf(0, 2, 3, 5, 6)),
        FOUR(listOf(1, 2, 3, 5)),
        FIVE(listOf(0, 1, 3, 5, 6)),
        SIX(listOf(0, 1, 3, 4, 5, 6)),
        SEVEN(listOf(0, 2, 5)),
        EIGHT(listOf(0, 1, 2, 3, 4, 5, 6)),
        NINE(listOf(0, 1, 2, 3, 5, 6));

        fun numberOfSegments() = segments.size

        fun hasUniqueNumberOfSegments() = this in listOf(ONE, FOUR, SEVEN, EIGHT)
    }
}

fun main() {
    val input = readInputLines(8)
    val sevenSegmentSearch = SevenSegmentSearch()
    val observations = sevenSegmentSearch.parseInput(input)
    val segmentsWithKnownSizes = sevenSegmentSearch.countSegmentsWithKnownSizes(observations)
    println("The digits 1, 4, 7 and 8 appear $segmentsWithKnownSizes times")
    val sumOfOutputDigits = sevenSegmentSearch.deriveMappings(observations)
    println("The sum of the output digits is $sumOfOutputDigits")
}
