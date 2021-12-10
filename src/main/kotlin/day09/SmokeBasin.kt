package day09

import utils.Coordinate
import utils.product
import utils.readInputLines
import utils.splitToString

class SmokeBasin {

    fun produceMap(input: List<String>): HeightMap {
        val map = input.flatMapIndexed { rowIdx, line ->
            line.splitToString().withIndex().associate { (colIdx, height) ->
                Pair(Coordinate(rowIdx, colIdx), height.toInt())
            }.entries
        }.associate { it.key to it.value }

        return HeightMap(map, input[0].length, input.size)
    }

    fun findRiskLevelsOfLowPoints(heightMap: HeightMap): Int {
        return heightMap.map.entries.fold(0) { total, (coordinate, height) ->
            if (height < (getAdjacentHeights(coordinate, heightMap).minOrNull()!!)) {
                total + height + 1
            } else {
                total
            }
        }
    }

    fun findBasin(heightMap: HeightMap): Long {
        val assignedBasins = mutableMapOf<Coordinate, Int>()
        var basinNo = 0
        heightMap.map.forEach { (coordinate, height) ->
            if (height != 9 && coordinate !in assignedBasins) {
                findSurroundingNonNines(heightMap, coordinate, assignedBasins, basinNo)
                basinNo++
            }
        }
        return assignedBasins.values
            .groupingBy { it }
            .eachCount()
            .values
            .sortedDescending()
            .subList(0, 3).map { it.toLong() }
            .product()
    }

    private fun getAdjacentHeights(coordinate: Coordinate, map: HeightMap): Sequence<Int> {
        return coordinate.getAdjacentCoordinates().mapNotNull { map[it] }
    }

    private fun findSurroundingNonNines(heightMap: HeightMap, coordinate: Coordinate, assignedBasins: MutableMap<Coordinate, Int>, basinNo: Int) {
        assignedBasins[coordinate] = basinNo
        coordinate.getAdjacentCoordinates()
            .filter(heightMap::isInBoundary)
            .filterNot(heightMap::isBorder)
            .filterNot(assignedBasins::contains)
            .forEach { adjacentCoordinate ->
                findSurroundingNonNines(heightMap, adjacentCoordinate, assignedBasins, basinNo)
            }
    }

    class HeightMap(val map: Map<Coordinate, Int>, private val maxY: Int, private val maxX: Int) {
        operator fun get(coordinate: Coordinate) = map[coordinate]
        fun isBorder(coordinate: Coordinate) = map[coordinate] == 9
        fun isInBoundary(coordinate: Coordinate) = coordinate.x < maxX && coordinate.y < maxY
    }
}


fun main() {
    val input = readInputLines(9)
    val smokeBasin = SmokeBasin()
    val map = smokeBasin.produceMap(input)
    val riskLevelsOfLowPoints = smokeBasin.findRiskLevelsOfLowPoints(map)
    println("Risk levels of low points is $riskLevelsOfLowPoints")
    val multiplied3LargestBasins = smokeBasin.findBasin(map)
    println("Three largest basins: $multiplied3LargestBasins")
}
