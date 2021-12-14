package day14

import utils.readInputLines
import utils.splitToString
import kotlin.math.ceil
import kotlin.math.round

class ExtendedPolymerization {

    fun parseInput(instructionList: List<String>): ManualData {
        val polymerTemplate = instructionList.first()
        val insertionRules = instructionList.drop(2).associate { instruction ->
            val (left, right) = instruction.split(" -> ")
            Pair(left, right)
        }

        return ManualData(polymerTemplate, insertionRules)
    }

    fun findOptimalFormula(manualData: ManualData): Int {
        val x = setOf(null)

        var currentFormula = manualData.polymerTemplate.splitToString()
        repeat(10) {
            val newFormula = mutableListOf<String>()
            currentFormula.zipWithNext().forEach { (elementA, elementB) ->
                val insertionRule = manualData.insertionRules.getValue(elementA + elementB)
                newFormula.add(elementA)
                newFormula.add(insertionRule)
            }
            newFormula.add(currentFormula.last())
            currentFormula = newFormula
        }

        val eachCount = currentFormula.groupingBy { it }.eachCount()
        val max = eachCount.maxByOrNull { it.value }!!.value
        val min = eachCount.minByOrNull { it.value }!!.value

        return max - min
    }

    fun findOptimalFormula2(manualData: ManualData, numberOfSteps: Int): Long {
        val initialMap = manualData.polymerTemplate
            .splitToString()
            .zipWithNext()
            .map { (first, second) -> first + second}
            .associateWith { 1L }

        val x = findOptimalFormulaRecursively(initialMap, manualData.insertionRules, numberOfSteps)
        val chars = x.keys.flatMap { it.splitToString() }.distinct()
        val y = chars.map { char ->
            x.entries
                .filter { e -> e.key.contains(char) }
                .sumOf { it.value + if (it.key[0] == it.key[1]) it.value else 0 }
        }

        val max = ceil(y.maxOrNull()!! / 2.0).toLong()
        val min = ceil(y.minOrNull()!! / 2.0).toLong()

        return max - min
    }

    private fun findOptimalFormulaRecursively(initialMap: Map<String, Long>, insertionRules: InsertionRules, maxIterations: Int): Map<String, Long> {
        var oldMap = initialMap //mutableMapOf<String, Long>().withDefault { 0  }

        repeat(maxIterations) {
            val counts = mutableMapOf<String, Long>().withDefault { 0 }
            for ((key, value) in oldMap.entries) {
                val nextChar = insertionRules.getValue(key)
                val pair1 = key[0] + nextChar
                counts.merge(pair1, value , Long::plus)
                val pair2 = nextChar + key[1]
                counts.merge(pair2, value, Long::plus)
            }
            oldMap = counts
        }

        return oldMap
    }

    class ManualData(val polymerTemplate: String, val insertionRules: InsertionRules)
}

typealias InsertionRules = Map<String, String>

fun main() {
    val input = readInputLines(14)
    val extendedPolymerization = ExtendedPolymerization()
    val manualData = extendedPolymerization.parseInput(input)
    val part1 = extendedPolymerization.findOptimalFormula(manualData)
    println("Part1: $part1")
    val part2 = extendedPolymerization.findOptimalFormula2(manualData, 40)
    println("Part2: $part2") // 7155869758393
}
