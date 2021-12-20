package day20

import utils.Coordinate
import utils.isEven
import utils.readInput

class TrenchMap {

    fun readInput(input: String): ImageEnhancementData {
        val (imageEnhancementAlgorithm, inputImage) = input.split("\n\n")
        val litPixels = inputImage.lines().flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, char ->
                if (char == '#') Coordinate(x, y) else null
            }
        }.toSet()

        return ImageEnhancementData(imageEnhancementAlgorithm, litPixels)
    }

    fun enhanceImage(numberOfEnhancements: Int, imageEnhancementData: ImageEnhancementData): Int {
        var enhancedImage = imageEnhancementData.litPixels

        repeat(numberOfEnhancements) {
            val checkBoundary = imageEnhancementData.requiresBoundaryCheck() && !it.isEven()
            enhancedImage = enhanceImage(enhancedImage, imageEnhancementData.imageEnhancementAlgorithm, checkBoundary)
//            enhancedImage.print()
        }
        return enhancedImage.size
    }

    private fun enhanceImage(inputImage: LitPixels, imageEnhancementAlgorithm: String, checkInArea: Boolean): LitPixels {
        val (xBounds, yBounds) = inputImage.getBounds()
        val coordinatesToCheck = (yBounds.first - 1..yBounds.last + 1).flatMap { y ->
            (xBounds.first - 1..xBounds.last + 1).map { x -> Coordinate(x, y) }
        }
        val isCoordinateInsideCheckArea = { coordinate: Coordinate ->
            checkInArea && !(coordinate.x in xBounds && coordinate.y in yBounds) }
        return coordinatesToCheck.mapNotNull { coordinate ->
            getEnhancedValue(coordinate, isCoordinateInsideCheckArea, inputImage, imageEnhancementAlgorithm)
        }.toSet()
    }

    private fun getEnhancedValue(
        coordinate: Coordinate,
        isCoordinateInsideCheckArea: (Coordinate) -> Boolean,
        litPixels: LitPixels,
        imageEnhancementAlgorithm: String
    ): Coordinate? {
        val binaryNumber = getSurroundingPixels(coordinate).map { surroundingCoordinate ->
            when {
                surroundingCoordinate in litPixels -> 1
                isCoordinateInsideCheckArea(surroundingCoordinate) -> 1
                else -> 0
            }
        }.joinToString("").toInt(2)
        val enhancedValue = imageEnhancementAlgorithm[binaryNumber]
        return if (enhancedValue == '#') coordinate else null
    }

    fun getSurroundingPixels(coordinate: Coordinate): Sequence<Coordinate> {
        return (coordinate.getAdjacentCoordinatesIncludingDiagonals(Int.MIN_VALUE, Int.MIN_VALUE) + coordinate)
            .sortedWith(compareBy<Coordinate> { it.y }.thenBy { it.x })
    }

    class ImageEnhancementData(val imageEnhancementAlgorithm: String, val litPixels: Set<Coordinate>) {
        fun requiresBoundaryCheck() = imageEnhancementAlgorithm.first() == '#'
    }
}

typealias LitPixels = Set<Coordinate>

fun LitPixels.getBounds(): Pair<IntRange, IntRange> {
    val minY = this.minByOrNull { it.y }!!.y
    val maxY = this.maxByOrNull { it.y }!!.y
    val minX = this.minByOrNull { it.x }!!.x
    val maxX = this.maxByOrNull { it.x }!!.x
    return Pair((minX..maxX), (minY..maxY))
}

fun LitPixels.print() {
    val (yBounds, xBounds) = getBounds()
    yBounds.forEach { y ->
        xBounds.forEach { x ->
            print(if (Coordinate(x, y) in this) '#' else '.')
        }
        println()
    }
    println()
}

fun main() {
    val input = readInput(20)
    val trenchMap = TrenchMap()
    val imageEnhancementData = trenchMap.readInput(input)
    val numberOfLitPixelsAfter2Enhancements = trenchMap.enhanceImage(2, imageEnhancementData)
    println("Number of lit pixels after 2 enhancements: $numberOfLitPixelsAfter2Enhancements")
    val numberOfLitPixelsAfter50Enhancements = trenchMap.enhanceImage(50, imageEnhancementData)
    println("Number of lit pixels after 50 enhancements: $numberOfLitPixelsAfter50Enhancements")
}
