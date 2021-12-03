package day03

import utils.readInputLines

class BinaryDiagnostic {

    fun getSignificantBits(report: List<String>): List<String> {
        val bitSize = report.first().length
        return (0 until bitSize).map { index ->
            val counts = report.map { it[index].toString() }.groupBy { it } //.maxByOrNull { it.value.size }?.key ?: "1"
            if ((counts["1"]?.size ?: 0) >= (counts["0"]?.size ?: 0)) "1" else "0"
        }
    }

    fun calculateGamma(bitList: List<String>) = bitList.toDecimal()
    fun calculateEpsilon(bitList: List<String>) = bitList.flipBinary().toDecimal()

    fun calculatePowerConsumption(bitList: List<String>) = calculateGamma(bitList) * calculateEpsilon(bitList)

    fun calculateOxygenGeneratorRating(report: List<String>) = calculateRating(report, false)
    fun calculateCO2ScrubberRating(report: List<String>) = calculateRating(report, true)

    private tailrec fun calculateRating(report: List<String>, flip: Boolean, index: Int = 0): Int {
        return if (report.size == 1) {
            report.toDecimal()
        } else {
            val significantBits = getSignificantBits(report)
            val significantBit = (if (flip) significantBits.flipBinary() else significantBits)[index]
            calculateRating(report.filter { it[index].toString() == significantBit }, flip, index + 1)
        }
    }

    fun calculateLifeSupportRating(report: List<String>) = calculateOxygenGeneratorRating(report) * calculateCO2ScrubberRating(report)

    private fun List<Any>.toDecimal() = this.joinToString("") { it.toString() }.toInt(2)
    private fun List<String>.flipBinary() = this.map { if (it == "1") "0" else "1" }
}

fun main() {
    val report = readInputLines(3)
    val binaryDiagnostic = BinaryDiagnostic()
    val bits = binaryDiagnostic.getSignificantBits(report)
    println("Power consumption is ${binaryDiagnostic.calculatePowerConsumption(bits)}")
    println("Life support rating is ${binaryDiagnostic.calculateLifeSupportRating(report)}")
}
