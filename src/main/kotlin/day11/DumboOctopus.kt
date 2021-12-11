package day11

import utils.Coordinate
import utils.readInputLines
import utils.splitToString

class DumboOctopus {

    fun parseInput(input: List<String>): Map<Coordinate, Octopus> {
        return input.flatMapIndexed { y, line ->
            line.splitToString()
                .withIndex()
                .associate { (x, energyLevel) -> Pair(Coordinate(x, y), Octopus(energyLevel.toInt())) }
                .entries
        }.associate { it.key to it.value }
    }

    fun countTotalFlashes(steps: Int, octopuses: Map<Coordinate, Octopus>): Int {
        return (0 until steps).sumOf {
            octopuses.values.forEach { octopus -> octopus.increaseEnergyLevel() }
            processChainReaction(octopuses)
            val count = octopuses.values.count { octopus -> octopus.hasFlashed() }
            octopuses.values.forEach { octopus -> octopus.resetFlashed() }
            return@sumOf count
        }
    }

    fun findFlashPoint(octopuses: Map<Coordinate, Octopus>): Int {
        var count = 0
        while(!octopuses.values.all { it.energyLevel == 0 }) {
            octopuses.values.forEach{ octopus -> octopus.increaseEnergyLevel() }
            processChainReaction(octopuses)
            octopuses.values.forEach { octopus -> octopus.resetFlashed() }
            count++
        }

        return count
    }

    private fun printOctopuses(octopuses: Map<Coordinate, Octopus>) {
        val width = octopuses.keys.maxOf { it.y }
        val height = octopuses.keys.maxOf { it.x }
        (0..width).forEach { y ->
            (0..height).forEach { x ->
                val energyLevel = octopuses.getValue(Coordinate(x, y)).energyLevel
//                print(if (energyLevel == 9) "\uD83D\uDD34" else "\uD83D\uDC19")
                print(energyLevel)
            }
            println()
        }
        println("[FRAME]")
    }

    private fun processChainReaction(octopuses: Map<Coordinate, Octopus>) {
        val triggered = octopuses
            .filter {  (_, octopus)  -> octopus.hasJustFlashed() }
            .onEach { (_, octopus) -> octopus.increaseEnergyLevel() }
            .flatMap {  (coordinate, _) -> coordinate.getAdjacentCoordinatesIncludingDiagonals() }
            .onEach { coordinate ->
                octopuses[coordinate]?.increaseEnergyLevel(true)
            }
            .size

        if (triggered > 0) {
//            printOctopuses(octopuses)
            processChainReaction(octopuses)
        }
    }

    data class Octopus(var energyLevel: Int) {
        fun increaseEnergyLevel(toMax: Boolean = false) {
            if (!toMax || energyLevel < 10) {
                energyLevel++
            }
        }

        fun resetFlashed() {
            if (energyLevel > 9) {
                energyLevel = 0
            }
        }

        fun hasFlashed() = energyLevel > 9
        fun hasJustFlashed() = energyLevel == 10
    }
}

fun main() {
    val input = readInputLines(11)
    val dumboOctopus = DumboOctopus()
    val octopuses = dumboOctopus.parseInput(input)
    val flashed = dumboOctopus.countTotalFlashes(100, octopuses)
    println("Total flashes after 100 steps: $flashed")
    val flashPoint = dumboOctopus.findFlashPoint(octopuses) + 100
    println("Flash point: $flashPoint")
}
