package utils

data class Coordinate(val x: Int, val y: Int) {
    fun getAdjacentCoordinates(minX: Int = 0, minY: Int = 0) = sequenceOf(
        Coordinate(this.x, this.y - 1),
        Coordinate(this.x, this.y + 1),
        Coordinate(this.x - 1, this.y),
        Coordinate(this.x + 1, this.y)
    ).filter { it.x >= minX && it.y >= minY }

    fun getAdjacentCoordinatesIncludingDiagonals(
        minX: Int = 0,
        minY: Int = 0
    ) = sequenceOf(
        Coordinate(this.x - 1, this.y - 1),
        Coordinate(this.x + 1, this.y - 1),
        Coordinate(this.x + 1, this.y + 1),
        Coordinate(this.x - 1, this.y + 1)
    ).filter { it.x >= minX && it.y >= minY } + getAdjacentCoordinates()
}