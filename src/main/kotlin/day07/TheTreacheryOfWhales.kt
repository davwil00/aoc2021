package day07

import utils.readSingleInputLineOfIntsFromCsv
import kotlin.math.abs

class TheTreacheryOfWhales {

    fun findCheapestFuelCost(crabPositions: List<Int>, understandCrabEngineering: Boolean = false): Int {
        return (crabPositions.minOrNull()!!..crabPositions.maxOrNull()!!).fold(Int.MAX_VALUE) { currentSmallest, position ->
            val totalFuel = crabPositions.sumOf {
                val numberOfStepsToMove = abs(position - it)
                if (understandCrabEngineering) calculateFuelForStep(numberOfStepsToMove) else numberOfStepsToMove
            }
            return@fold if (totalFuel < currentSmallest) totalFuel else currentSmallest
        }
    }

    fun calculateFuelForStep(step: Int): Int {
        return (step * (step + 1)) / 2
    }
}

fun main() {
    val input = readSingleInputLineOfIntsFromCsv(7)
    val theTreacheryOfWhales = TheTreacheryOfWhales()
    val leastFuel = theTreacheryOfWhales.findCheapestFuelCost(input)
    println("The crabs must spend $leastFuel")
    val leastFuel2 = theTreacheryOfWhales.findCheapestFuelCost(input, true)
    println("The crabs must spend $leastFuel2")
}
