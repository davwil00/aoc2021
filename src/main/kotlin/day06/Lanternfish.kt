package day06

import utils.readInput

fun createLaternfishFromInput(input: String): Map<Int, Long> {
    return input.split(",")
        .map { it.toInt() }
        .groupingBy { it }
        .foldTo(mutableMapOf<Int, Long>(), 0) { acc, _ -> acc + 1L }
        .toMap()
}

fun simulateDays(days: Int, lanternfish: Map<Int, Long>): Long {
    var mapCopy = lanternfish.toMutableMap()
    repeat(days) {
        val newMap = mutableMapOf<Int, Long>()
        mapCopy.forEach { (timer, count) ->
            when (timer) {
                0 -> {
                    newMap.merge(6, count) { a, b -> a + b }
                    newMap[8] = count
                }
                7 -> newMap.merge(6, count) { a, b -> a + b }
                else -> newMap[timer - 1] = count
            }
        }

        mapCopy = newMap
    }

    return mapCopy.values.sum()
}

fun main() {
    val input = readInput(6)
    val lanternfish = createLaternfishFromInput(input)
    val numberOfLanternfishAfter80Days = simulateDays(80, lanternfish)
    println("Number of lanternfish after 80 days: $numberOfLanternfishAfter80Days")
    val numberOfLanternfishAfter256Days = simulateDays(256, lanternfish)
    println("Number of lanternfish after 256 days: $numberOfLanternfishAfter256Days") //1559542828
}
