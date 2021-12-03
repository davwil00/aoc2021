package day03

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import utils.readInputLines

class BinaryDiagnosticTest {

    private val binaryDiagnostic = BinaryDiagnostic()

    private val testInput = """00100
11110
10110
10111
10101
01111
00111
11100
10000
11001
00010
01010""".lines()

    @Test
    fun `should calculate significant bits`() {
        assertThat(binaryDiagnostic.getSignificantBits(testInput)).containsExactly("1", "0", "1", "1", "0")
    }

    @Test
    fun `should calculate significant bits when equal number of 1s and 0s`() {
        val input = listOf("00000", "11111")
        assertThat(binaryDiagnostic.getSignificantBits(input)).containsExactly("1", "1", "1", "1", "1")
    }

	@Test
    fun `should calculate correct gamma`() {
        val bits = binaryDiagnostic.getSignificantBits(testInput)
        assertThat(binaryDiagnostic.calculateGamma(bits)).isEqualTo(22)
    }

	@Test
    fun `should calculate correct epsilon`() {
        val bits = binaryDiagnostic.getSignificantBits(testInput)
        assertThat(binaryDiagnostic.calculateEpsilon(bits)).isEqualTo(9)
    }

	@Test
    fun `should calculate correct power consumption`() {
        val bits = binaryDiagnostic.getSignificantBits(testInput)
        assertThat(binaryDiagnostic.calculatePowerConsumption(bits)).isEqualTo(198)
    }

    @Test
    fun `should calculate correct power consumption for full input`() {
        val bits = binaryDiagnostic.getSignificantBits(readInputLines(3))
        assertThat(binaryDiagnostic.calculatePowerConsumption(bits)).isEqualTo(1082324)
    }

    @Test
    fun `should calculate correct oxygen generator rating`() {
        val report = testInput
        assertThat(binaryDiagnostic.calculateOxygenGeneratorRating(report)).isEqualTo(23)
    }

    @Test
    fun `should calculate correct CO2 scrubber rating`() {
        val report = testInput
        assertThat(binaryDiagnostic.calculateCO2ScrubberRating(report)).isEqualTo(10)
    }

    @Test
    fun `should calculate correct life support rating`() {
        val report = testInput
        assertThat(binaryDiagnostic.calculateLifeSupportRating(report)).isEqualTo(230)
    }

    @Test
    fun `should calculate correct life support rating for full input`() {
        val report = readInputLines(3)
        assertThat(binaryDiagnostic.calculateLifeSupportRating(report)).isEqualTo(1353024)
    }
}
