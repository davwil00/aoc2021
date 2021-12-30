package day23

import utils.Edge
import utils.Graph
import utils.readInputLines
import kotlin.math.abs
import kotlin.reflect.KClass

sealed class Amphipod(val char: Char, val energyPerMove: Int) {

    override fun toString(): String {
        return "$char"
    }

    override fun equals(other: Any?) = other != null && this::class == other::class
    override fun hashCode(): Int {
        return this::class.hashCode()
    }

    companion object {
        fun fromChar(char: Char): Amphipod? {
            return when (char) {
                'A' -> AmberAmphipod()
                'B' -> BronzeAmphipod()
                'C' -> CopperAmphipod()
                'D' -> DesertAmphipod()
                '.' -> null
                else -> throw IllegalArgumentException("Unknown Amphipod $char")
            }
        }
    }
}

class AmberAmphipod: Amphipod('A', 1)
class BronzeAmphipod: Amphipod('B', 10)
class CopperAmphipod: Amphipod('C', 100)
class DesertAmphipod: Amphipod('D', 1000)

interface Location {
    val index: Int

    fun getBlockingLocations(destination: Location): List<Location> {
        return if (destination.index < index) {
            (destination.index until index).map { Hallway(it) }
        } else {
            (index + 1 .. destination.index).map { Hallway(it) }
        }
    }
}

data class Room(val roomType: KClass<out Amphipod>, val position: Int): Location {

    override val index = when(roomType) {
        AmberAmphipod::class -> 2
        BronzeAmphipod::class -> 4
        CopperAmphipod::class -> 6
        DesertAmphipod::class -> 8
        else -> throw IllegalStateException("Unknown room type")
    }

    fun getCostToHallway() = position

    fun isTop() = position == 1

    fun getRoomsAbove() = (position - 1 downTo 1)
        .map { this.copy(position = it) }

    fun getRoomsBelowTo(maxRoomOccupancy: Int) = (position..maxRoomOccupancy)
        .map { this.copy(position = it) }

    fun isRoomFor(amphipod: Amphipod?) = amphipod != null && roomType == amphipod::class

    override fun toString(): String {
        return "Room ${index/2} #$position"
    }
}

data class Hallway(override val index: Int): Location {
    companion object {
        fun getValidPositions() = sequenceOf(0, 1, 3, 5, 7, 9, 10)
    }
}

class Move(
    val amphipod: Amphipod,
    val location: Location,
    val destination: Location) {

    fun calculateCost() =
        amphipod.energyPerMove *
                (abs(location.index - destination.index) +
                        if (location is Room) location.getCostToHallway() else 0 +
                        if (destination is Room) destination.getCostToHallway() else 0)

    override fun toString(): String {
        return "$amphipod from $location to $destination"
    }
}

fun produceEdgeList(burrowMap: BurrowMap, seenStates: MutableSet<BurrowMap> = mutableSetOf(), level: Int = 0): List<Edge<BurrowMap>> {
    val availableMoves = burrowMap.getAvailableMoves()

    if (availableMoves.isEmpty()) {
        return emptyList()
    }

    return availableMoves.flatMap { move ->
        val newBurrowMap = burrowMap.move(move)
        val list = mutableListOf(Edge(burrowMap, newBurrowMap, move.calculateCost()))
        if (!seenStates.add(newBurrowMap)) {
            return@flatMap list
        }
        list.addAll(produceEdgeList(newBurrowMap, seenStates, level+1))
        list
    }
}

fun findPath(startingBurrowMap: BurrowMap): Int {
    val edgeList = produceEdgeList(startingBurrowMap)
    val graph = Graph(edgeList, true)
    graph.dijkstra(startingBurrowMap)
    val endState = sequenceOf(
        AmberAmphipod(),
        BronzeAmphipod(),
        CopperAmphipod(),
        DesertAmphipod()
    ).flatMap { amphipod ->
        (1..startingBurrowMap.maxRoomOccupancy).map { position ->
            Room(amphipod::class, position) to amphipod
        }
    }.toMap(BurrowMap(startingBurrowMap.maxRoomOccupancy))
    Hallway.getValidPositions().forEach { endState[Hallway(it)] = null }

    val cost = graph.getWeightToPath(endState)
//    graph.printPath(endState)
    return cost
}

class BurrowMap(val maxRoomOccupancy: Int): LinkedHashMap<Location, Amphipod?>(), Comparable<BurrowMap> {

    @Suppress("UNCHECKED_CAST")
    private fun getUnoccupiedHallwayLocations() =
        this.filter { (location, amphipod) ->
            amphipod == null && location is Hallway
        }.keys as Set<Hallway>

    fun isPathToHallwayLocationNotBlocked(location: Room, destination: Hallway) =
        (location.isTop() || location.getRoomsAbove()
            .map { this[it] }
            .all { it == null }
        ) && location.getBlockingLocations(destination).all { this[it] == null }

    fun isHallwayPathToRoomNotBlocked(location: Hallway, destination: KClass<out Amphipod>): Boolean {
        val topRoom = Room(destination, 1)
        return this[topRoom] == null && location.getBlockingLocations(topRoom).all { this[it] == null }
    }

    fun getBottommostValidRoomOrNull(amphipodType: KClass<out Amphipod>): Int? {
        val allPositionsInRoom = (maxRoomOccupancy downTo 1).map { Room(amphipodType, it) }
        if (allPositionsInRoom.all { this[it] == null || it.isRoomFor(this[it]) }) {
            return allPositionsInRoom.first { this[it] == null }.position
        }
        return null
    }

    fun getAvailableMoves(): List<Move> {
        val allPossibleMoves = getIncorrectLocations()
            .flatMap { (location, amphipod) ->
                if (location is Room) {
                    getUnoccupiedHallwayLocations()
                        .filter { destination -> isPathToHallwayLocationNotBlocked(location, destination) }
                        .map { destination -> Move(amphipod, location, destination) }
                } else {
                    val bottommostValidRoom = getBottommostValidRoomOrNull(amphipod::class)
                    if (isHallwayPathToRoomNotBlocked(location as Hallway, amphipod::class) && bottommostValidRoom != null) {
                        listOf(Move(amphipod, location, Room(amphipod::class, bottommostValidRoom)))
                    } else {
                        emptyList()
                    }
                }
            }
        return if (allPossibleMoves.any { it.destination is Room }) {
            allPossibleMoves.filter { it.destination is Room }
        } else {
            allPossibleMoves
        }
    }

    fun move(move: Move): BurrowMap {
        return this
            .entries
            .associateTo(BurrowMap(this.maxRoomOccupancy)) { (mapLocation, mapAmphipod) ->
                mapLocation to when (mapLocation) {
                    move.location -> null
                    move.destination -> move.amphipod
                    else -> mapAmphipod
                }
            }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getIncorrectLocations(): List<Map.Entry<Location, Amphipod>> {
        return this
            .entries
            .filter { (location, amphipod) ->
                amphipod != null && (
                    location !is Room || (
                        !location.isRoomFor(amphipod) || // is wrong
                        // is not bottom and positions below are wrong
                        (location.position != maxRoomOccupancy && location.getRoomsBelowTo(maxRoomOccupancy).any {
                            position -> !position.isRoomFor(this[position])
                        })
                    )
                )
            } as List<Map.Entry<Location, Amphipod>>
    }

    companion object {
        fun fromString(input: List<String>, maxRoomOccupancy: Int = 2): BurrowMap {
            val burrowMap = Amphipod::class.sealedSubclasses.flatMapIndexed { idx, roomType ->
                (2..maxRoomOccupancy + 1).map { line ->
                    val amphipod = Amphipod.fromChar(input[line][(idx * 2) + 3])
                    Room(roomType, line - 1) to amphipod
                }
            }.toMap(BurrowMap(maxRoomOccupancy))

            Hallway.getValidPositions().forEach { burrowMap[Hallway(it)] = Amphipod.fromChar(input[1][it + 1]) }

            return burrowMap
        }
    }

    override fun compareTo(other: BurrowMap): Int {
        return this.toString().compareTo(other.toString())
    }

    fun isCorrect() =
        this
            .filterKeys { it is Room }
            .all { (key, value) -> (key as Room).isRoomFor(value) }
            && this.values.filterNotNull().size == 8


    override fun toString(): String {
        return buildString {
            appendLine("#############")
            append("#")
            (0..10).forEach { append(this@BurrowMap[Hallway(it)]?.char ?: '.') }
            appendLine("#")
            append("##")
            Amphipod::class.sealedSubclasses.forEach {
                append('#')
                append(this@BurrowMap[Room(it, 1)]?.char ?: '.')
            }
            appendLine("###")

            (2..maxRoomOccupancy).forEach { idx ->
                append("  ")
                Amphipod::class.sealedSubclasses.forEach { kClass ->
                    append('#')
                    append(this@BurrowMap[Room(kClass, idx)]?.char ?: '.')
                }
                appendLine('#')
            }
            appendLine("  #########")
        }
    }
}

fun main() {
    val input = readInputLines(23)
    val burrow = BurrowMap.fromString(input)
    println("Least energy required is ${findPath(burrow)}")
    val part2Input = readInputLines(23, "input-part2.txt")
    val part2BurrowMap = BurrowMap.fromString(part2Input, 4)
    println("Least energy required is ${findPath(part2BurrowMap)}")
}
