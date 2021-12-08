package day08

import day08.SevenSegmentSearch.Digit.*
import utils.*

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
        val eight = observation.findDigit(EIGHT)
        val seven = observation.findDigit(SEVEN)
        val four = observation.findDigit(FOUR)
        val one = observation.findDigit(ONE)

        val six = observation.findDigit(SIX) { s -> !s.containsAll(one.segments) }
        val c = eight - six
        val five = observation.findDigit(FIVE) { !it.contains(c) }
        val nine = DigitMatch(NINE, (five.segments + one.segments).unique())
        val zero = observation.findDigit(ZERO) { !it.containsAll(nine.segments) && !it.containsAll(six.segments) }

        val e = eight - nine
        val three = observation.findDigit(THREE) { !it.containsAll(five.segments) && !it.contains(e) }
        val two = observation.findDigit(TWO) { !it.containsAll(five.segments) && !it.containsAll(three.segments) }
        val digits = listOf(zero, one, two, three, four, five, six, seven, eight, nine)

        return observation.outputValue.map { value -> digits.first { digitMatch -> digitMatch.matches(value) }.digit.value }.joinToString("").toInt()
    }

    class Observation(private val uniqueSignalPatterns: List<String>, val outputValue: List<String>) {
        fun findDigit(digit: Digit): DigitMatch = DigitMatch(digit, uniqueSignalPatterns.first { it.length == digit.numberOfSegments })
        fun findDigit(digit: Digit, additionalCriteria: (String) -> Boolean) = DigitMatch(digit, uniqueSignalPatterns.first { it.length == digit.numberOfSegments && additionalCriteria.invoke(it) })
    }

    class DigitMatch(val digit: Digit, val segments: String) {
        fun matches(value: String) = value.equalsIgnoringOrder(segments)

        operator fun minus(other: DigitMatch) = this.segments.splitToString().toSet().minus(other.segments.splitToString().toSet()).single()
    }

    private fun String.hasUniqueNumberOfSegments() = this.length in listOf(2, 4, 3, 7)

    enum class Digit(val value: Int, val numberOfSegments: Int) {
        ZERO(0, 6),
        ONE(1, 2),
        TWO(2, 5),
        THREE(3, 5),
        FOUR(4, 4),
        FIVE(5, 5),
        SIX(6, 6),
        SEVEN(7, 3),
        EIGHT(8, 7),
        NINE(9, 5);
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
