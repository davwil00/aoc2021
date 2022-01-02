package day19

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import readTestInput
import utils.Coordinate3D
import utils.readInput

class BeaconScannerTest {

    private val testInput = readTestInput(19)
    private val beaconScanner = BeaconScanner()

	@Test
    fun `find scanner position`() {
        val scanners = beaconScanner.readInput(testInput)
        val normalisedScanners = beaconScanner.normaliseScanners(scanners)
        assertThat(normalisedScanners).extracting("scanner.id", "coordinate")
            .containsExactlyInAnyOrder(
                Tuple(0, Coordinate3D(0, 0, 0)),
                Tuple(1, Coordinate3D(68, -1246, -43)),
                Tuple(2, Coordinate3D(1105, -1205, 1229)),
                Tuple(3, Coordinate3D(-92, -2380, -20)),
                Tuple(4, Coordinate3D(-20, -1133, 1061))
            )
    }

    @Test
    fun `find total beacons`() {
        val scanners = beaconScanner.readInput(testInput)
        val normalisedScanners = beaconScanner.normaliseScanners(scanners)
        assertThat(beaconScanner.findTotalBeacons(normalisedScanners)).isEqualTo(79)
    }

    @Test
    fun `find total beacons for full input`() {
        val scanners = beaconScanner.readInput(readInput(19))
        val normalisedScanners = beaconScanner.normaliseScanners(scanners)
        assertThat(beaconScanner.findTotalBeacons(normalisedScanners)).isEqualTo(403)
    }

    @Test
    fun `find max distance between scanners`() {
        val scanners = beaconScanner.readInput(testInput)
        val normalisedScanners = beaconScanner.normaliseScanners(scanners)
        assertThat(beaconScanner.findLargestDistanceBetweenScanners(normalisedScanners)).isEqualTo(3621)
    }

    @Test
    fun `find max distance between scanners for full input`() {
        val scanners = beaconScanner.readInput(readInput(19))
        val normalisedScanners = beaconScanner.normaliseScanners(scanners)
        assertThat(beaconScanner.findLargestDistanceBetweenScanners(normalisedScanners)).isEqualTo(10569)
    }
}
