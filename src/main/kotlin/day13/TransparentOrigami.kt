package day13

import day13.TransparentOrigami.Axis.X_AXIS
import day13.TransparentOrigami.Axis.Y_AXIS
import utils.Coordinate
import utils.readInput
import utils.readInputLines
import java.lang.IllegalArgumentException

class TransparentOrigami {

    fun readInstructions(input: String): Input {
        val (coordinates, instructions) = input.split("\n\n")
        val parsedInstructions = parseInstructions(instructions)
        val grid = makeGrid(coordinates)

        return Input(grid, parsedInstructions)
    }

    private fun parseInstructions(instructions: String): List<Instruction> {
        return instructions.lines().map { instruction -> Instruction.fromString(instruction) }
    }

    private fun makeGrid(coordinates: String): Set<Coordinate> {
        return coordinates
            .lines()
            .map { it.split(",") }
            .map { (x, y) -> Coordinate(x.toInt(), y.toInt()) }
            .toSet()
    }

    fun fold(grid: Set<Coordinate>, instruction: Instruction): Set<Coordinate> {
        return when(instruction.axis) {
            X_AXIS -> foldX(grid, instruction.position)
            Y_AXIS -> foldY(grid, instruction.position)
        }
    }

    private fun foldX(grid: Set<Coordinate>, position: Int): Set<Coordinate> {
        val newGrid = grid.toMutableSet()
        grid.forEach { coordinate ->
            if (coordinate.x >= position) {
                newGrid.remove(coordinate)
                val xDiff = position - coordinate.x
                val newCoordinate = coordinate.copy(x = coordinate.x + (xDiff * 2))
                newGrid.add(newCoordinate)
            }
        }

        return newGrid
    }

    private fun foldY(grid: Set<Coordinate>, position: Int): Set<Coordinate> {
        val newGrid = grid.toMutableSet()
        grid.forEach { coordinate ->
            if (coordinate.y >= position) {
                newGrid.remove(coordinate)
                val yDiff = position - coordinate.y
                val newCoordinate = coordinate.copy(y = coordinate.y + (yDiff * 2))
                newGrid.add(newCoordinate)
            }
        }

        return newGrid
    }

    fun printGrid(grid: Set<Coordinate>) {
        val maxX = grid.maxByOrNull { it.x }!!.x
        val maxY = grid.maxByOrNull { it.y }!!.y
        (0 .. maxY).forEach { y ->
            (0..maxX).forEach { x ->
                if (grid.contains(Coordinate(x, y))) {
                    print("#")
                } else {
                    print(" ")
                }
            }
            println()
        }
    }

    class Input(val grid: Set<Coordinate>, val instructions: List<Instruction>)
    class Instruction(val axis: Axis, val position: Int) {

        companion object {
            private val instructionRegex = Regex("""fold along ([xy])=(\d+)""")

            fun fromString(instruction: String): Instruction {
                val (_, axis, position) = instructionRegex.matchEntire(instruction)!!.groupValues
                return Instruction(Axis.fromAxisChar(axis), position.toInt())
            }
        }
    }
    enum class Axis(val axisChar: String) {
        X_AXIS("x"), Y_AXIS("y");

        companion object {
            fun fromAxisChar(axisChar: String): Axis {
                return when (axisChar) {
                    "x" -> X_AXIS
                    "y" -> Y_AXIS
                    else -> throw IllegalArgumentException("Unknown axis $axisChar")
                }
            }
        }

    }
}

fun main() {
    val input = readInput(13)
    val transparentOrigami = TransparentOrigami()
    val inputx = transparentOrigami.readInstructions(input)
    val resultAfterFirstFold = transparentOrigami.fold(inputx.grid, inputx.instructions[0])
    println("Number of dots after first fold: ${resultAfterFirstFold.size}")
    val finalGrid = inputx.instructions.fold(inputx.grid) { grid, instruction ->
        transparentOrigami.fold(grid, instruction)
    }
    transparentOrigami.printGrid(finalGrid)
}
