package day19

import org.slf4j.LoggerFactory
import utils.Coordinate3D
import utils.readInput
import java.util.*

class BeaconScanner {

    fun readInput(input: String): List<Scanner> {
        return input.split("\n\n")
            .map { Scanner.fromString(it) }
    }

    fun normaliseScanners(scanners: List<Scanner>): List<NormalisedScanner> {
        val overlaps = mutableSetOf(NormalisedScanner(scanners.first(), Coordinate3D(0, 0, 0)))
        while (overlaps.size < scanners.size) {
            val normalisedScanners = overlaps.map { it.scanner }
            val scannersToNormalise = scanners.filter { scanner ->
                scanner.id !in normalisedScanners.map { it.id }
            }
            overlaps.add(findScannerToNormalise(normalisedScanners, scannersToNormalise))
        }
        return overlaps.toList()
    }

    fun findTotalBeacons(scanners: List<NormalisedScanner>): Int {
        return scanners.flatMap { it.scanner.measurements }.toSet().also { coordinates ->
            logger.debug("Normalised coordinates: {}", coordinates.sortedWith(
                Comparator.comparing<Coordinate3D?, Int?> { it.x }.thenBy { it.y }.thenBy { it.z }
            ).joinToString("\n"))
        }.size
    }

    fun findLargestDistanceBetweenScanners(scanners: List<NormalisedScanner>): Int {
        var maxDistance = 0
        scanners.forEachIndexed { idx, scanner1 ->
            scanners.listIterator(idx).forEach { scanner2 ->
                val distance = scanner1.coordinate.manhattanDistanceTo(scanner2.coordinate)
                if (distance > maxDistance) {
                    maxDistance = distance
                }
            }
        }

        return maxDistance
    }

    class NormalisedScanner(val scanner: Scanner, val coordinate: Coordinate3D)

    private fun findScannerToNormalise(normalisedScanners: List<Scanner>, scannersToNormalise: List<Scanner>): NormalisedScanner {
        for (scannerToBeNormalised in scannersToNormalise) {
            for (normalisedScanner in normalisedScanners) {
                val match = findOverlappingCoordinates(normalisedScanner, scannerToBeNormalised)
                if (match != null) {
                    logger.info("Normalised scanner ${scannerToBeNormalised.id}")
                    return match
                }
            }
        }

        throw IllegalStateException("Unable to find overlaps for remaining scanners")
    }

    private fun findOverlappingCoordinates(origin: Scanner, potentialMatch: Scanner): NormalisedScanner? {
        for (scanner1Coordinate in origin.measurements) {
            for (scanner2Coordinate in potentialMatch.measurements) {
                for (transformFunc in coordinatePermutations) {
                    val adjustment = scanner1Coordinate - transformFunc(scanner2Coordinate)
                    val adjustedCoordinates = potentialMatch.measurements.map { transformFunc(it) + adjustment }.toSet()
                    val overlaps = adjustedCoordinates.intersect(origin.measurements.toSet())

                    if (overlaps.size == 12) {
                        logger.info("Found overlaps with adjustment $adjustment using transform ${transformFunc(Coordinate3D(1,2,3))}")
                        return NormalisedScanner(Scanner(potentialMatch.id, adjustedCoordinates), adjustment)
                    }
                }
            }
        }

        return null
    }

    data class Scanner(val id: Int, val measurements: Set<Coordinate3D>) {

        override fun toString(): String {
            return id.toString()
        }

        companion object {
            private val idRegex = Regex("""--- scanner (\d+) ---""")

            fun fromString(str: String): Scanner {
                val lines = str.lines()
                val id = idRegex.matchEntire(lines[0])!!.groupValues[1].toInt()
                val measurements = lines
                    .listIterator(1)
                    .asSequence()
                    .map { it.split(",") }
                    .map { Coordinate3D(it[0].toInt(), it[1].toInt(), it[2].toInt()) }

                return Scanner(id, measurements.toSet())
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(BeaconScanner::class.java)
        private val coordinatePermutations: Sequence<(Coordinate3D) -> Coordinate3D> = sequenceOf(
            { coordinate: Coordinate3D -> Coordinate3D(coordinate.x, coordinate.y, coordinate.z) },
            { coordinate: Coordinate3D -> Coordinate3D(coordinate.y, -coordinate.x, coordinate.z) },
            { coordinate: Coordinate3D -> Coordinate3D(-coordinate.x, -coordinate.y, coordinate.z) },
            { coordinate: Coordinate3D -> Coordinate3D(-coordinate.y, coordinate.x, coordinate.z) },
            { coordinate: Coordinate3D -> Coordinate3D(coordinate.z, coordinate.y, -coordinate.x) },
            { coordinate: Coordinate3D -> Coordinate3D(coordinate.y, -coordinate.z, -coordinate.x) },
            { coordinate: Coordinate3D -> Coordinate3D(-coordinate.z, -coordinate.y, -coordinate.x) },
            { coordinate: Coordinate3D -> Coordinate3D(-coordinate.y, coordinate.z, -coordinate.x) },
            { coordinate: Coordinate3D -> Coordinate3D(coordinate.x, -coordinate.y, -coordinate.z) },
            { coordinate: Coordinate3D -> Coordinate3D(-coordinate.y, -coordinate.x, -coordinate.z) },
            { coordinate: Coordinate3D -> Coordinate3D(-coordinate.x, coordinate.y, -coordinate.z) },
            { coordinate: Coordinate3D -> Coordinate3D(coordinate.y, coordinate.x, -coordinate.z) },
            { coordinate: Coordinate3D -> Coordinate3D(coordinate.z, -coordinate.y, coordinate.x) },
            { coordinate: Coordinate3D -> Coordinate3D(-coordinate.y, -coordinate.z, coordinate.x) },
            { coordinate: Coordinate3D -> Coordinate3D(-coordinate.z, coordinate.y, coordinate.x) },
            { coordinate: Coordinate3D -> Coordinate3D(coordinate.y, coordinate.z, coordinate.x) },
            { coordinate: Coordinate3D -> Coordinate3D(coordinate.x, coordinate.z, -coordinate.y) },
            { coordinate: Coordinate3D -> Coordinate3D(coordinate.z, -coordinate.x, -coordinate.y) },
            { coordinate: Coordinate3D -> Coordinate3D(-coordinate.x, -coordinate.z, -coordinate.y) },
            { coordinate: Coordinate3D -> Coordinate3D(-coordinate.z, coordinate.x, -coordinate.y) },
            { coordinate: Coordinate3D -> Coordinate3D(-coordinate.x, coordinate.z, coordinate.y) },
            { coordinate: Coordinate3D -> Coordinate3D(coordinate.z, coordinate.x, coordinate.y) },
            { coordinate: Coordinate3D -> Coordinate3D(coordinate.x, -coordinate.z, coordinate.y) },
            { coordinate: Coordinate3D -> Coordinate3D(-coordinate.z, -coordinate.x, coordinate.y) },
        )
    }
}

fun main() {
    val input = readInput(19)
    val beaconScanner = BeaconScanner()
    val scanners = beaconScanner.readInput(input)
    val normalisedScanners = beaconScanner.normaliseScanners(scanners)
    val numberOfBeacons = beaconScanner.findTotalBeacons(normalisedScanners)
    println("Number of beacons: $numberOfBeacons")
    val maxDistance = beaconScanner.findLargestDistanceBetweenScanners(normalisedScanners)
    println("Max distance between scanners: $maxDistance")
}
