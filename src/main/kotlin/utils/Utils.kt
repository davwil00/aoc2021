package utils

import java.io.File

fun readInput(day: Int) : String {
    val paddedDay = day.toString().padStart(2, '0')
    return File("src/main/resources/day$paddedDay/input.txt").readText().trim()
}

fun readInputLines(day: Int) : List<String> {
    return readInput(day).lines()
}

fun readSingleInputLineOfIntsFromCsv(day: Int): List<Int> {
    return readInput(day).split(",").map { it.toInt() }
}