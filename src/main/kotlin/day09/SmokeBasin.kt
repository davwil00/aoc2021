package day09

import utils.product
import utils.readInputLines
import utils.splitToString

class SmokeBasin {

    fun produceMap(input: List<String>): List<List<Int>> {
        return input.map { line -> line.splitToString().map { it.toInt() } }
    }

    fun findRiskLevelsOfLowPoints(map: List<List<Int>>): Int {
        return map.foldIndexed(0) { rowIdx, total, row ->
            return@foldIndexed row.foldIndexed(total) colTotal@ { colIdx, rowTotal, height ->
                return@colTotal if (height < (getAdjacentValues(rowIdx, colIdx, map).minOrNull() ?: height)) {
                    rowTotal + height + 1
                } else {
                    rowTotal
                }
            }
        }
    }

    fun findBasin(map: List<List<Int>>): Long {
        val assignedBasins = mutableMapOf<Pair<Int, Int>, Int>()
        var basinNo = 0
        map.forEachIndexed { rowIdx, row ->
            row.forEachIndexed { colIdx, height ->
                if (height != 9 && !assignedBasins.contains(Pair(rowIdx, colIdx))) {
                    findSurroundingNonNines(map, rowIdx, colIdx, assignedBasins, basinNo)
                    basinNo++
                }
            }
        }
        return assignedBasins.values
            .filter { it != 0 }
            .groupingBy { it }
            .eachCount()
            .values
            .sortedDescending()
            .subList(0, 3).map { it.toLong() }
            .product()
    }

    fun findSurroundingNonNines(map: List<List<Int>>, rowIdx: Int, colIdx: Int, assignedBasins: MutableMap<Pair<Int, Int>, Int>, basinNo: Int) {
        assignedBasins[Pair(rowIdx, colIdx)] = basinNo
        val toProcess = mutableSetOf<Pair<Int, Int>>()
        // check left
        for (it in colIdx - 1 downTo 0) {
            if (map[rowIdx][it] == 9 || assignedBasins.contains(Pair(rowIdx, it))) {
                break
            } else {
                toProcess.add(Pair(rowIdx, it))
            }
        }

        // check right
        for (it in colIdx + 1 until map[rowIdx].size) {
            if (map[rowIdx][it] == 9 || assignedBasins.contains(Pair(rowIdx, it))) {
                break
            } else {
                toProcess.add(Pair(rowIdx, it))
            }
        }

        // check up
        for (it in rowIdx - 1 downTo 0) {
            if (map[it][colIdx] == 9 || assignedBasins.contains(Pair(it, colIdx))) {
                break
            } else {
                toProcess.add(Pair(it, colIdx))
            }
        }

        // check down
        for (it in rowIdx + 1 until map.size) {
            if (map[it][colIdx] == 9 || assignedBasins.contains(Pair(it, colIdx))) {
                break
            } else {
                toProcess.add(Pair(it, colIdx))
            }
        }

        toProcess.forEach { (newRowIdx, newColIdx) ->
            if (!assignedBasins.contains(Pair(newRowIdx, newColIdx))) {
                findSurroundingNonNines(map, newRowIdx, newColIdx, assignedBasins, basinNo)
            }
        }
    }

    private fun getAdjacentValues(rowIdx: Int, colIdx: Int, map: List<List<Int>>): List<Int> {
        val vals = mutableListOf<Int>()
        if (colIdx != 0) {
            vals.add(map[rowIdx][colIdx - 1])
        }
        if (colIdx < map[rowIdx].size - 1) {
            vals.add(map[rowIdx][colIdx + 1])
        }
        if (rowIdx != 0) {
            vals.add(map[rowIdx - 1][colIdx])
        }
        if (rowIdx < map.size - 1) {
            vals.add(map[rowIdx + 1][colIdx])
        }
        return vals
    }
}

fun main() {
    val input = readInputLines(9)
    val smokeBasin = SmokeBasin()
    val map = smokeBasin.produceMap(input)
    val riskLevelsOfLowPoints = smokeBasin.findRiskLevelsOfLowPoints(map)
    println("Risk levels of low points is $riskLevelsOfLowPoints")
    val multiplied3LargestBasins = smokeBasin.findBasin(map)
    println("Ans: $multiplied3LargestBasins") // 381300 too low // 617520 too low

}
