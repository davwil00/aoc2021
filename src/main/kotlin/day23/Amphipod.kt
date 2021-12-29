package day23

import day23.Location.*
import utils.Edge
import utils.Graph
import utils.readInputLines
import kotlin.collections.Map.Entry
import kotlin.math.abs

sealed class Amphipod(val char: Char, val energyPerMove: Int) {
    abstract val id: Int
    var moves: Int = 0

    override fun toString(): String {
        return "$char$id"
    }

    abstract fun getTopRoom(): Location
    abstract fun getBottomRoom(): Location

    override fun equals(other: Any?) = other != null && this::class == other::class
    override fun hashCode(): Int {
        return this::class.hashCode()
    }

    companion object {
        fun fromChar(id: Int, char: Char): Amphipod {
            return when (char) {
                'A' -> AmberAmphipod(id)
                'B' -> BronzeAmphipod(id)
                'C' -> CopperAmphipod(id)
                'D' -> DesertAmphipod(id)
                else -> throw IllegalArgumentException("Unknown Amphipod $char")
            }
        }
    }
}

class AmberAmphipod(override val id: Int) : Amphipod('A', 1) {
    override fun getTopRoom() = ROOM_1_TOP
    override fun getBottomRoom() = ROOM_1_BOTTOM
}
class BronzeAmphipod(override val id: Int) : Amphipod('B', 10) {
    override fun getTopRoom() = ROOM_2_TOP
    override fun getBottomRoom() = ROOM_2_BOTTOM
}
class CopperAmphipod(override val id: Int) : Amphipod('C', 100) {
    override fun getTopRoom() = ROOM_3_TOP
    override fun getBottomRoom() = ROOM_3_BOTTOM
}
class DesertAmphipod(override val id: Int) : Amphipod('D', 1000) {
    override fun getTopRoom() = ROOM_4_TOP
    override fun getBottomRoom() = ROOM_4_BOTTOM
}

enum class Location(val index: Int, val isRoom: Boolean) {
    ROOM_1_TOP(2, true),
    ROOM_1_BOTTOM(2, true),
    ROOM_2_TOP(4, true),
    ROOM_2_BOTTOM(4, true),
    ROOM_3_TOP(6, true),
    ROOM_3_BOTTOM(6, true),
    ROOM_4_TOP(8, true),
    ROOM_4_BOTTOM(8, true),
    HALLWAY_0(0, false),
    HALLWAY_1(1, false),
    HALLWAY_3(3, false),
    HALLWAY_5(5, false),
    HALLWAY_7(7, false),
    HALLWAY_9(9, false),
    HALLWAY_10(10, false),
    ;

    fun getCostToHallway() = when(this) {
        ROOM_1_TOP, ROOM_2_TOP, ROOM_3_TOP, ROOM_4_TOP -> 1
        ROOM_1_BOTTOM, ROOM_2_BOTTOM, ROOM_3_BOTTOM, ROOM_4_BOTTOM -> 2
        else -> 0
    }

    fun isTop() = when (this) {
        ROOM_1_TOP, ROOM_2_TOP, ROOM_3_TOP, ROOM_4_TOP -> true
        else -> false
    }

    fun isBottom() = !isTop()

    fun getTop() = when(this) {
        ROOM_1_BOTTOM -> ROOM_1_TOP
        ROOM_2_BOTTOM -> ROOM_2_TOP
        ROOM_3_BOTTOM -> ROOM_3_TOP
        ROOM_4_BOTTOM -> ROOM_4_TOP
        else -> throw IllegalStateException("Not a bottom room")
    }

    fun getBottom() = when(this) {
        ROOM_1_TOP -> ROOM_1_BOTTOM
        ROOM_2_TOP -> ROOM_2_BOTTOM
        ROOM_3_TOP -> ROOM_3_BOTTOM
        ROOM_4_TOP -> ROOM_4_BOTTOM
        else -> throw IllegalStateException("Not a top room")
    }

    fun getBlockingLocations(destination: Location): List<Location> {
        return if (destination.index < this.index) {
            values().filter { !it.isRoom && it.index < this.index && it.index >= destination.index }
        } else {
            values().filter { !it.isRoom && it.index > this.index && it.index <= destination.index }
        }
    }

    fun isRoomFor(amphipod: Amphipod?) = when (this) {
        ROOM_1_TOP, ROOM_1_BOTTOM -> amphipod is AmberAmphipod
        ROOM_2_TOP, ROOM_2_BOTTOM -> amphipod is BronzeAmphipod
        ROOM_3_TOP, ROOM_3_BOTTOM -> amphipod is CopperAmphipod
        ROOM_4_TOP, ROOM_4_BOTTOM -> amphipod is DesertAmphipod
        else -> false
    }
}

class Move(
    val amphipod: Amphipod,
    val location: Location,
    val destination: Location) {

    fun calculateCost() =
        amphipod.energyPerMove *
                (abs(location.index - destination.index) +
                        location.getCostToHallway() +
                        destination.getCostToHallway())

    override fun toString(): String {
        return "$amphipod from $location to $destination"
    }
}

fun produceEdgeList(burrow: Burrow, seenStates: MutableSet<BurrowMap> = mutableSetOf()): List<Edge<BurrowMap>> {
    val availableMoves = burrow.getAvailableMoves()

    if (availableMoves.isEmpty()) {
        return emptyList()
    }

    return availableMoves.flatMap { move ->
        val newBurrow = burrow.move(move)
        val list = mutableListOf(Edge(burrow.burrowMap, newBurrow.burrowMap, move.calculateCost()))
        if (!seenStates.add(newBurrow.burrowMap)) {
            return@flatMap list
        }
        list.addAll(produceEdgeList(newBurrow, seenStates))
        list
    }
}

fun findPath(startingBurrow: Burrow): Int {
    val edgeList = produceEdgeList(startingBurrow)
    val graph = Graph(edgeList, true)
    graph.dijkstra(startingBurrow.burrowMap)
    val endState = sequenceOf(
        ROOM_1_TOP to AmberAmphipod(1),
        ROOM_1_BOTTOM to AmberAmphipod(1),
        ROOM_2_TOP to BronzeAmphipod(1),
        ROOM_2_BOTTOM to BronzeAmphipod(1),
        ROOM_3_TOP to CopperAmphipod(1),
        ROOM_3_BOTTOM to CopperAmphipod(1),
        ROOM_4_TOP to DesertAmphipod(1),
        ROOM_4_BOTTOM to DesertAmphipod(1),
        HALLWAY_0 to null,
        HALLWAY_1 to null,
        HALLWAY_3 to null,
        HALLWAY_5 to null,
        HALLWAY_7 to null,
        HALLWAY_9 to null,
        HALLWAY_10 to null,
    ).toMap(BurrowMap())
    val cost = graph.getWeightToPath(endState)
//    graph.printPath(endState)
    return cost
}

class Burrow(val burrowMap: BurrowMap) {

    private fun getUnoccupiedHallwayLocations() =
        burrowMap.filter { (location, amphipod) ->
            amphipod == null && !location.isRoom
        }.keys

    private fun isPathToHallwayLocationNotBlocked(location: Location, destination: Location) =
        (location.isTop() || burrowMap[location.getTop()] == null) &&
            location.getBlockingLocations(destination).all { burrowMap[it] == null }

    private fun isPathToRoomNotBlocked(location: Location, destination: Location) =
        burrowMap[destination] == null &&
                (destination.isTop() || burrowMap[destination.getTop()] == null) &&
                location.getBlockingLocations(destination).all { burrowMap[it] == null }

    private fun isBottomRoomCorrect(amphipod: Amphipod): Boolean {
        val bottomRoomOccupant = burrowMap.getValue(amphipod.getBottomRoom())
        return bottomRoomOccupant != null && bottomRoomOccupant::class == amphipod::class
    }

    fun getAvailableMoves(): List<Move> {
        return getIncorrectLocations()
            .flatMap { (location, amphipod) ->
                if (location.isRoom) {
                    getUnoccupiedHallwayLocations()
                        .filter { destination -> isPathToHallwayLocationNotBlocked(location, destination) }
                        .map { destination -> Move(amphipod, location, destination) }
                } else {
//                    // move to top room if bottom room correct and path not blocked
                    if (isBottomRoomCorrect(amphipod) && isPathToRoomNotBlocked(location, amphipod.getTopRoom())) {
                        listOf(Move(amphipod, location, amphipod.getTopRoom()))
                    } else if (isPathToRoomNotBlocked(location, amphipod.getBottomRoom())) {
                        // try to move to bottom room
                        listOf(Move(amphipod, location, amphipod.getBottomRoom()))
                    } else {
                        emptyList()
                    }
                }
            }
    }

    fun move(move: Move): Burrow {
        val newMap = burrowMap
            .entries
            .associateTo(BurrowMap()) { (mapLocation, mapAmphipod) ->
                mapLocation to when (mapLocation) {
                    move.location -> null
                    move.destination -> move.amphipod
                    else -> mapAmphipod
                }
            }

        return Burrow(newMap)
    }

    @Suppress("UNCHECKED_CAST")
    private fun getIncorrectLocations(): List<Entry<Location, Amphipod>> {
        return burrowMap
            .entries
            .filter { (location, amphipod) ->
                amphipod != null && (
                    !location.isRoom || (
                        !location.isRoomFor(amphipod) || // is wrong
                        (location.isTop() && !location.getBottom().isRoomFor(burrowMap[location.getBottom()])) // is top and bottom is wrong
                    )
                )
            } as List<Entry<Location, Amphipod>>
    }

    companion object {
        fun fromString(input: List<String>): Burrow {
            val burrowMap = BurrowMap()

            burrowMap[ROOM_1_TOP] = Amphipod.fromChar(1, input[2][3])
            burrowMap[ROOM_1_BOTTOM] = Amphipod.fromChar(2, input[3][3])

            burrowMap[ROOM_2_TOP] = Amphipod.fromChar(3, input[2][5])
            burrowMap[ROOM_2_BOTTOM] = Amphipod.fromChar(4, input[3][5])

            burrowMap[ROOM_3_TOP] = Amphipod.fromChar(5, input[2][7])
            burrowMap[ROOM_3_BOTTOM] = Amphipod.fromChar(6, input[3][7])

            burrowMap[ROOM_4_TOP] = Amphipod.fromChar(7, input[2][9])
            burrowMap[ROOM_4_BOTTOM] = Amphipod.fromChar(8, input[3][9])

            Location.values()
                .filter { it !in burrowMap }
                .forEach { burrowMap[it] = null }

            return Burrow(burrowMap)
        }
    }
}

class BurrowMap: LinkedHashMap<Location, Amphipod?>(), Comparable<BurrowMap> {
    override fun compareTo(other: BurrowMap): Int {
        return this.toString().compareTo(other.toString())
    }

    fun isCorrect() =
        this[ROOM_1_TOP] is AmberAmphipod &&
        this[ROOM_1_BOTTOM] is AmberAmphipod &&
        this[ROOM_2_TOP] is BronzeAmphipod &&
        this[ROOM_2_BOTTOM] is BronzeAmphipod &&
        this[ROOM_3_TOP] is CopperAmphipod &&
        this[ROOM_3_BOTTOM] is CopperAmphipod &&
        this[ROOM_4_TOP] is DesertAmphipod &&
        this[ROOM_4_BOTTOM] is DesertAmphipod &&
        this.values.filterNotNull().size == 8


    override fun toString(): String {
        return """
            #############
            #${this[HALLWAY_0]?.char ?: "."}${this[HALLWAY_1]?.char ?: "."}.${this[HALLWAY_3]?.char ?: "."}.${this[HALLWAY_5]?.char ?: "."}.${this[HALLWAY_7]?.char ?: "."}.${this[HALLWAY_9]?.char ?: "."}${this[HALLWAY_10]?.char ?: "."}#
            ###${this[ROOM_1_TOP]?.char ?: "."}#${this[ROOM_2_TOP]?.char ?: "."}#${this[ROOM_3_TOP]?.char ?: "."}#${this[ROOM_4_TOP]?.char ?: "."}###
              #${this[ROOM_1_BOTTOM]?.char ?: "."}#${this[ROOM_2_BOTTOM]?.char ?: "."}#${this[ROOM_3_BOTTOM]?.char ?: "."}#${this[ROOM_4_BOTTOM]?.char ?: "."}#
              #########
        """.trimIndent()
    }

}

fun main() {
    val input = readInputLines(23)
    val burrow = Burrow.fromString(input)
    println("Least energy required is ${findPath(burrow)}")
}
