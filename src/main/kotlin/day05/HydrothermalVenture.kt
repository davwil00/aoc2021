package day05

import utils.Coordinate
import utils.readInputLines
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.max

class HydrothermalVenture {

    fun parseInput(input: List<String>): List<CoordinateRange> {
        return input.map { line: String ->
            val (_, x1, y1, x2, y2) = INPUT_REGEX.matchEntire(line)!!.groupValues
            CoordinateRange(Coordinate(x1.toInt(), y1.toInt()), Coordinate(x2.toInt(), y2.toInt()))
        }
    }

    private fun mapHorizontalVerticalVents(ventLocations: List<CoordinateRange>, includeDiagonals: Boolean): Map<Coordinate, Int> {
        return ventLocations
            .filter { if (includeDiagonals) true else (!isDiagonal(it)) }
            .flatMap { expandCoordinateRange(it) }
            .fold( mutableMapOf() ) { map, coordinate ->
                map.merge(coordinate, 1, Int::plus)
                return@fold map
            }
    }

    fun countDangerousAreas(ventLocations: List<CoordinateRange>, includeDiagonals: Boolean): Int {
        val mapHorizontalVerticalVents = mapHorizontalVerticalVents(ventLocations, includeDiagonals)
        return mapHorizontalVerticalVents.values.count { it > 1 }
    }

    fun expandCoordinateRange(range: CoordinateRange): List<Coordinate> {
        val (x1, y1) = range.first
        val (x2, y2) = range.second
        val minX = min(x1, x2)
        val maxX = max(x1, x2)
        val minY = min(y1, y2)
        val maxY = max(y1, y2)

        return if (isDiagonal(range)) {
            val steps = maxX - minX
            (0 .. steps).map { i ->
                val x = if (x1 > x2) minX + i else maxX - i
                val y = if (y1 > y2) minY + i else maxY - i
                return@map Coordinate(x, y)
            }
        } else {
            (minX..maxX).flatMap { x ->
                (minY..maxY).map { y -> Coordinate(x, y) }
            }
        }
    }

    private fun isDiagonal(range: CoordinateRange) = abs(range.second.x - range.first.x) == abs(range.second.y - range.first.y)

    companion object {
        private val INPUT_REGEX = Regex("""(\d+),(\d+) -> (\d+),(\d+)""")
    }
}

typealias CoordinateRange = Pair<Coordinate, Coordinate>

fun main() {
    val input = readInputLines(5)
    val hydrothermalVenture = HydrothermalVenture()
    val ventLocations = hydrothermalVenture.parseInput(input)
    val dangerousAreas = hydrothermalVenture.countDangerousAreas(ventLocations, false)
    println("No of dangerous areas: $dangerousAreas")
    val dangerousAreasIncludingDiagonals = hydrothermalVenture.countDangerousAreas(ventLocations, true)
    println("No of dangerous areas including diagonals: $dangerousAreasIncludingDiagonals")
}
