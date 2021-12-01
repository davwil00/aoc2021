package day01

import utils.readInputLines

class SonarSweep {

    fun findIncrements(depths: List<String>): Int {
        return depths.map(String::toInt)
            .zipWithNext()
            .fold(0) { acc, (curr, next) ->
                acc + if (next > curr) 1 else 0
            }
    }

    fun findIncrementsWithWindow(depths: List<String>, windowSize: Int): Int {
        return depths.map(String::toInt)
            .windowed(windowSize + 1)
            .fold(0) { acc, measurements ->
                val window1 = measurements.subList(0, windowSize)
                val window2 = measurements.subList(1, measurements.size)
                acc + if (window2.sum() > window1.sum()) 1 else 0
            }
    }
}

fun main() {
    val input = readInputLines(1)
    val sonarSweep = SonarSweep()
    println("Total depth increases: ${sonarSweep.findIncrements(input)}")
    println("Total depth increases with window: ${sonarSweep.findIncrementsWithWindow(input, 3)}")
}
