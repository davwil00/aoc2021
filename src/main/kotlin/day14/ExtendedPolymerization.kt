package day14

import utils.eachCountToLong
import utils.readInputLines
import utils.splitToString
import kotlin.math.ceil

class ExtendedPolymerization {

    fun parseInput(instructionList: List<String>): ManualData {
        val polymerTemplate = instructionList.first()
        val insertionRules = instructionList.drop(2).associate { instruction ->
            val (left, right) = instruction.split(" -> ")
            Pair(left, right)
        }

        return ManualData(polymerTemplate, insertionRules)
    }

    fun findOptimalFormula(manualData: ManualData, numberOfSteps: Int): Long {
        val templatePairCounts = manualData.polymerTemplate
            .splitToString()
            .zipWithNext(String::plus)
            .groupingBy { it }
            .eachCountToLong()

        val pairCounts = findOptimalFormula(templatePairCounts, manualData.insertionRules, numberOfSteps)
        val elements = manualData.insertionRules.values.distinct()
        val elementCounts = elements.map { element ->
            pairCounts.entries
                .filter { e -> e.key.contains(element) }
                .sumOf { it.value + if (it.key[0] == it.key[1]) it.value else 0 }
        }

        val max = ceil(elementCounts.maxOrNull()!! / 2.0).toLong()
        val min = ceil(elementCounts.minOrNull()!! / 2.0).toLong()

        return max - min
    }

    private fun findOptimalFormula(templatePairCounts: Map<String, Long>, insertionRules: InsertionRules, maxIterations: Int): Map<String, Long> {
        var pairCounts = templatePairCounts

        repeat(maxIterations) {
            pairCounts = pairCounts.entries.fold(mutableMapOf()) { acc, (key, value) ->
                val nextChar = insertionRules.getValue(key)
                val pair1 = key[0] + nextChar
                acc.merge(pair1, value , Long::plus)
                val pair2 = nextChar + key[1]
                acc.merge(pair2, value, Long::plus)
                return@fold acc
            }
        }

        return pairCounts
    }

    class ManualData(val polymerTemplate: String, val insertionRules: InsertionRules)
}

typealias InsertionRules = Map<String, String>

fun main() {
    val input = readInputLines(14)
    val extendedPolymerization = ExtendedPolymerization()
    val manualData = extendedPolymerization.parseInput(input)
    val part1 = extendedPolymerization.findOptimalFormula(manualData, 10)
    println("After 10 iterations: $part1")
    val part2 = extendedPolymerization.findOptimalFormula(manualData, 40)
    println("After 40 iterations: $part2")
}
