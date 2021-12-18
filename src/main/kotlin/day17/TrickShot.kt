package day17

import day17.TrickShot.TargetArea
import utils.readInput
import java.util.stream.IntStream

class TrickShot {

    fun calculateXVelocity(targetArea: TargetArea): Int {
        return IntStream.iterate(0) { it + 1 }
            .filter { triangle(it) in targetArea.getXRange() }
            .findFirst()
            .asInt
    }

    fun calculateMaxY(xVelocity: Int, targetArea: TargetArea): Int {
        return (0..500).map { y ->
            simulate(targetArea, xVelocity, y)
        }.filter { it.isWithinTargetArea(targetArea) }
        .maxOf { it.maxY }
    }

    fun findAll(targetArea: TargetArea): Int {
        return (0..500).flatMap x@ { x ->
            (-500..500).map y@ { y ->
                simulate(targetArea, x, y)
            }
        }.filter { it.isWithinTargetArea(targetArea) }
        .size
    }

    private fun triangle(x: Int) = (x * (x + 1)) / 2

    fun simulate(targetArea: TargetArea, xVelocity: Int, yVelocity: Int): Probe {
        val probe = Probe(xVelocity, yVelocity)
        while(probe.xPos <= targetArea.xRight && probe.yPos >= targetArea.yBottom) {
            probe.step()
            if (probe.isWithinTargetArea(targetArea)) {
                return probe
            }
        }
        return probe
    }

    class TargetArea(val xLeft: Int, val xRight: Int, val yBottom: Int, val yTop: Int) {

        fun getXRange() = xLeft..xRight
        fun getYRange() = yBottom..yTop

        companion object {
            private val inputRegex = Regex("""target area: x=(-?\d+)[.]{2}(-?\d+), y=(-?\d+)[.]{2}(-?\d+)""")
            fun fromString(input: String): TargetArea {
                inputRegex.matchEntire(input)?.let {
                    val (_, xMin, xMax, yMin, yMax) = it.groupValues
                    return TargetArea(xMin.toInt(), xMax.toInt(), yMin.toInt(), yMax.toInt())
                }
                throw IllegalArgumentException("Unparseable input")
            }
        }
    }

    class Probe(initialXVelocity: Int, initialYVelocity: Int) {
        var xPos = 0
        var yPos = 0
        var maxY = 0
        var xVelocity = initialXVelocity
        var yVelocity = initialYVelocity

        fun step() {
            xPos += xVelocity
            yPos += yVelocity
            if (yPos > maxY) {
                maxY = yPos
            }
            when  {
                xVelocity < 0 -> xVelocity++
                xVelocity > 0 -> xVelocity--
            }
            yVelocity--
        }

        fun isWithinTargetArea(targetArea: TargetArea): Boolean {
            return (xPos in targetArea.getXRange() && yPos in targetArea.getYRange())
        }
    }
}

fun main() {
    val input = readInput(17)
    val trickShot = TrickShot()
    val targetArea = TargetArea.fromString(input)

    val xVelocity = trickShot.calculateXVelocity(targetArea)
    val maxY = trickShot.calculateMaxY(xVelocity, targetArea)
    println("Max y is: $maxY")
    val numberOfVelocitiesWithinTarget = trickShot.findAll(targetArea)
    println("Number of velocities within target ares: $numberOfVelocitiesWithinTarget")
}