package day15

import utils.*

class Chiton {

    fun produceEdgeList(input: List<String>): List<Edge<String>> {
        return input.flatMapIndexed { y, row ->
            row.splitToString().flatMapIndexed { x, riskLevel ->
                val node = Coordinate(x, y)
                node.getAdjacentCoordinates()
                    .filter { it.x < row.length && it.y < input.size }
                    .map { coordinate -> Edge(coordinate.toString(), node.toString(), riskLevel.toInt()) }
            }
        }
    }

    fun produceMassiveMapInput(input: List<String>): List<String> {
        return (0..4).flatMap { yIter ->
            input.map { line ->
                (0..4).flatMap { xIter ->
                    line.splitToString().map { getMassiveRiskLevel(xIter, yIter, it.toInt()) }
                }.joinToString("")
            }
        }
    }

    fun getMassiveRiskLevel(xIter: Int, yIter: Int, riskLevel: Int): Int {
        val newRiskLevel = riskLevel + xIter + yIter
        return if (newRiskLevel > 9) {
            newRiskLevel - 9
        } else {
            newRiskLevel
        }
    }

    fun findLowestRiskLevelPath(edges: List<Edge<String>>, endingCoordinate: Coordinate): Int {
        val startingCoordinate = Coordinate(0, 0)
        val graph = Graph(edges, true)
        graph.dijkstra(startingCoordinate.toString())

//        graph.printPath(endingCoordinate.toString())
        return graph.getWeightToPath(endingCoordinate.toString())
    }
}

fun main() {
    val input = readInputLines(15)
    val chiton = Chiton()
    val riskMap = chiton.produceEdgeList(input)
    val shortestPathCost = chiton.findLowestRiskLevelPath(riskMap, Coordinate(input.size - 1, input.size - 1))
    println("Shortest path cost is: $shortestPathCost")
    val massiveMapInput = chiton.produceMassiveMapInput(input)
    val massiveRiskMap = chiton.produceEdgeList(massiveMapInput)
    val shortestPathCostInMassiveMap = chiton.findLowestRiskLevelPath(massiveRiskMap, Coordinate(massiveMapInput.size - 1, massiveMapInput.size - 1))
    println("Shortest path cost for massive map is: $shortestPathCostInMassiveMap")
}
