package day01

import utils.readInputLines

class SonarSweep {

    fun findIncrements(depths: List<String>) =
        depths.map(String::toInt)
            .zipWithNext()
            .fold(0) { acc, (curr, next) ->
                acc + if (next > curr) 1 else 0
            }

    fun findIncrementsWithWindow(depths: List<String>, windowSize: Int) =
        depths.asSequence()
            .map(String::toInt)
            .windowed(windowSize)
            .map(List<Int>::sum)
            .zipWithNext()
            .fold(0) { acc, (window1Sum, window2Sum) ->
                acc + if (window2Sum > window1Sum) 1 else 0
            }
}

fun main() {
    val input = readInputLines(1)
    val sonarSweep = SonarSweep()
    println("Total depth increases: ${sonarSweep.findIncrements(input)}")
    println("Total depth increases with window: ${sonarSweep.findIncrementsWithWindow(input, 3)}")
}
