package utils

import kotlin.math.abs

data class Coordinate(val x: Int, val y: Int) {
    fun getAdjacentCoordinates(minX: Int = 0, minY: Int = 0) = sequenceOf(
        Coordinate(this.x - 1, this.y),
        Coordinate(this.x + 1, this.y),
        Coordinate(this.x, this.y - 1),
        Coordinate(this.x, this.y + 1),
    ).filter { it.x >= minX && it.y >= minY }

    fun getAdjacentCoordinatesIncludingDiagonals(
        minX: Int = 0,
        minY: Int = 0
    ) = sequenceOf(
        Coordinate(this.x - 1, this.y - 1),
        Coordinate(this.x + 1, this.y - 1),
        Coordinate(this.x + 1, this.y + 1),
        Coordinate(this.x - 1, this.y + 1)
    ).filter { it.x >= minX && it.y >= minY } + getAdjacentCoordinates(minX, minY)
}

data class Coordinate3D(val x: Int, val y: Int, val z: Int) {

    operator fun minus(other: Coordinate3D): Coordinate3D = Coordinate3D(x - other.x, y - other.y, z - other.z)

    operator fun plus(other: Coordinate3D) = Coordinate3D(x + other.x, y + other.y, z + other.z)

    fun manhattanDistanceTo(other: Coordinate3D) =
        abs(x - other.x) + abs(y - other.y) + abs(z - other.z)

    override fun toString() = "$x,$y,$z"
}