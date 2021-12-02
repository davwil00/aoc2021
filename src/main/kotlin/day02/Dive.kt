package day02

import utils.readInputLines

class Dive {

    fun parseCommands(commands: List<String>): List<Command> {
        return commands.map { command ->
            val (directionStr, diffStr) = command.split(" ")
            Command(directionStr, diffStr)
        }
    }

    fun plotCourse(commands: List<Command>): Int {
        val newPosition = commands.fold(PositionWithAim(0, 0)) { position, command -> command.moveFrom(position)}
        return newPosition.product()
    }

    fun plotCourseWithAim(commands: List<Command>): Int {
        val newPosition = commands.fold(PositionWithAim(0, 0, 0)) { positionWithAim, command -> command.moveFromWithAim(positionWithAim)}
        return newPosition.product()
    }

    class Command(private val direction: Direction, private val diff: Int) {
        constructor(direction: String, diff: String): this(Direction.fromString(direction), diff.toInt())

        fun moveFrom(position: PositionWithAim): PositionWithAim {
            return when (direction) {
                Direction.FORWARD -> PositionWithAim(position.horizontal + diff, position.depth)
                Direction.UP -> PositionWithAim(position.horizontal, position.depth - diff)
                Direction.DOWN -> PositionWithAim(position.horizontal, position.depth + diff)
            }
        }

        fun moveFromWithAim(positionWithAim: PositionWithAim): PositionWithAim {
            return when (direction) {
                Direction.FORWARD -> positionWithAim.copy(
                    horizontal = positionWithAim.horizontal + diff,
                    depth = positionWithAim.depth + (positionWithAim.aim * diff)
                )
                Direction.UP -> positionWithAim.copy(aim = positionWithAim.aim - diff)
                Direction.DOWN -> positionWithAim.copy(aim = positionWithAim.aim + diff)
            }
        }
    }

    enum class Direction {
        FORWARD, UP, DOWN;

        companion object {
            fun fromString(direction: String): Direction {
                return values().first { it.name == direction.uppercase() }
            }
        }
    }

    data class PositionWithAim(val horizontal: Int, val depth: Int, val aim: Int = 0) {
        fun product() = horizontal * depth
    }
}

fun main() {
    val input = readInputLines(2)
    val dive = Dive()
    val commands = (dive.parseCommands(input))
    println("New position is ${dive.plotCourse(commands)}")
    println("New position with aim is ${dive.plotCourseWithAim(commands)}")
}
