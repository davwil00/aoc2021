package day01

import utils.readInputLines

class SonarSweep {

    fun findIncrements(depths: List<String>) =
        depths.map(String::toInt)
            .zipWithNext()
            .count { (curr, next) -> next > curr }

    fun findIncrementsWithWindow(depths: List<String>, windowSize: Int) =
        depths.asSequence()
            .map(String::toInt)
            .windowed(windowSize)
            .map(List<Int>::sum)
            .zipWithNext()
            .count { (window1Sum, window2Sum) -> window2Sum > window1Sum }
}

fun main() {
    val input = readInputLines(1)
    val sonarSweep = SonarSweep()
    println("Total depth increases: ${sonarSweep.findIncrements(input)}")
    println("Total depth increases with window: ${sonarSweep.findIncrementsWithWindow(input, 3)}")
}
