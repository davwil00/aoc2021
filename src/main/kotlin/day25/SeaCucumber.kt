package day25

import day25.SeaCucumber.Direction.EAST
import day25.SeaCucumber.Direction.SOUTH
import utils.Coordinate
import utils.readInputLines

class SeaCucumber {

    fun readInput(input: List<String>): Map<Coordinate, Direction> {
        return input.flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, direction ->
                val coordinate = Coordinate(x, y)
                if (direction != '.') {
                    Pair(coordinate, Direction.fromChar(direction))
                } else {
                    null
                }
            }
        }.toMap()
    }

    fun printMap(map: Map<Coordinate, SeaCucumber>) {
        val maxX = map.keys.maxByOrNull { it.x }!!.x
        val maxY = map.keys.maxByOrNull { it.y }!!.y

        (0..maxY).forEach { y ->
            (0..maxX).forEach { x ->
                val coordinate = Coordinate(x, y)
                print(if (coordinate in map) map.getValue(coordinate) else ".")
            }
            println()
        }
        println()
        println()
    }

    fun findWhenSeaCucumberStopMoving(map: Map<Coordinate, Direction>): Int {
        val maxX = map.keys.maxByOrNull { it.x }!!.x
        val maxY = map.keys.maxByOrNull { it.y }!!.y
        var stillMoving = true
        var i = 0
        var currentMap = map
        while (stillMoving) {
            val newMap = currentMap.entries
                .filter { it.value == EAST }
                .associate { (coordinate, direction) ->
                    Pair(moveEastIfPossible(coordinate, maxX, currentMap), direction)
                }.toMutableMap()

            currentMap.entries
                .filter { it.value == SOUTH }
                .forEach { (coordinate, direction) ->
                    newMap[moveSouthIfPossible(coordinate, maxY, currentMap, newMap)] = direction
                }

            stillMoving = currentMap != newMap
            currentMap = newMap
//        printMap(newMap)
            i++
        }

        return i
    }

    private fun moveSouthIfPossible(
        coordinate: Coordinate,
        maxY: Int,
        currentMap: Map<Coordinate, Direction>,
        newMap: MutableMap<Coordinate, Direction>,
    ): Coordinate {
        val newY = if (coordinate.y + 1 > maxY) {
            0
        } else {
            coordinate.y + 1
        }
        val coordinateToCheck = coordinate.copy(y = newY)
        return if ((coordinateToCheck !in currentMap || currentMap.getValue(coordinateToCheck) == EAST)
            && coordinateToCheck !in newMap
        ) {
            coordinateToCheck
        } else {
            coordinate
        }
    }

    private fun moveEastIfPossible(
        coordinate: Coordinate,
        maxX: Int,
        currentMap: Map<Coordinate, Direction>,
    ): Coordinate {
        val newX = if (coordinate.x + 1 > maxX) {
            0
        } else {
            coordinate.x + 1
        }
        val coordinateToCheck = coordinate.copy(x = newX)
        return if (coordinateToCheck !in currentMap) {
            coordinateToCheck
        } else {
            coordinate
        }
    }

    enum class Direction(val char: Char) {
        EAST('>'), SOUTH('v');

        override fun toString(): String {
            return "$char"
        }

        companion object {
            fun fromChar(char: Char): Direction {
                return values().first { it.char == char }
            }
        }
    }
}

fun main() {
    val input = readInputLines(25)
    val seaCucumber = SeaCucumber()
    val map = seaCucumber.readInput(input)
    println(seaCucumber.findWhenSeaCucumberStopMoving(map))
}
