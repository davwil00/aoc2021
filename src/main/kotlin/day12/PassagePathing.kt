package day12

import utils.isLowercase
import utils.readInputLines

class PassagePathing {

    fun getCaveConnections(input: List<String>): Map<String, Cave> {
        val caveConnections = mutableMapOf<String, Cave>()
        input.forEach { connection ->
            val (cave1Id, cave2Id) = connection.split("-")
            if (cave2Id == "start" || cave1Id == "end") {
                addCaveMapping(caveConnections, cave2Id, cave1Id)
            } else {
                addCaveMapping(caveConnections, cave1Id, cave2Id)
            }
        }

        return caveConnections
    }

    private fun addCaveMapping(caves: MutableMap<String, Cave>, cave1Id: String, cave2Id: String) {
        caves.putIfAbsent(cave2Id, Cave(cave2Id))

        caves.merge(cave1Id, Cave(cave1Id, mutableListOf(cave2Id))) { caveOrig, _ ->
            caveOrig.caves.add(cave2Id)
            return@merge caveOrig
        }

        if (cave1Id != "start" && cave2Id != "end") {
            caves.getValue(cave2Id).caves.add(cave1Id)
        }
    }

    fun findNumberOfPaths(caveConnections: Map<String, Cave>): Int {
        val criteria = { caveId: String, currentPath: List<String>, _: String ->
            caveId in currentPath.filter(::isSmallCave)
        }

        return findPaths(caveConnections, allowedCavesToVisitCriteria = criteria).size
    }

    fun findNumberOfPathsAllowingOneSmallCaveToBeVisitedTwice(caveConnections: Map<String, Cave>): Int {
        val criteria = { caveId: String, currentPath: List<String>, currentCaveId: String ->
            caveId in currentPath.filter(::isSmallCave) &&
                    hasAlreadyVisitedASmallCaveTwice(currentPath.plus(currentCaveId))
        }

        return findPaths(caveConnections, allowedCavesToVisitCriteria = criteria).size
    }

    private fun findPaths(
        caveConnections: Map<String, Cave>,
        currentCave: Cave = caveConnections.getValue("start"),
        currentPath: List<String> = listOf(),
        allowedCavesToVisitCriteria: (String, List<String>, String) -> Boolean,
        paths: MutableSet<List<String>> = mutableSetOf()
    ): Set<List<String>> {
        if (currentCave.isEnd()) {
            paths.add(currentPath + currentCave.id)
            return paths
        }

        val cavesWeCanVisit = currentCave.caves
            .filterNot { caveId -> allowedCavesToVisitCriteria.invoke(caveId, currentPath, currentCave.id) }

        if (cavesWeCanVisit.isEmpty()) {
            return paths
        }

        cavesWeCanVisit.forEach { caveId ->
            findPaths(caveConnections, caveConnections.getValue(caveId), currentPath + currentCave.id, allowedCavesToVisitCriteria, paths)
        }

        return paths
    }

    private fun hasAlreadyVisitedASmallCaveTwice(currentPath: List<String>): Boolean {
        return currentPath.any { caveId ->
            isSmallCave(caveId) && currentPath.count { it == caveId } == 2
        }
    }

    private fun isSmallCave(caveId: String) = caveId.isLowercase()

    class Cave(val id: String, val caves: MutableList<String> = mutableListOf()) {
        fun isEnd() = id == "end"
    }
}

fun main() {
    val input = readInputLines(12)
    val passagePathing = PassagePathing()
    val caveConnections = passagePathing.getCaveConnections(input)
    val numberOfPaths = passagePathing.findNumberOfPaths(caveConnections)
    println("Number of paths: $numberOfPaths")
    val numberOfPathsWithSmallCaveRevisit = passagePathing.findNumberOfPathsAllowingOneSmallCaveToBeVisitedTwice(caveConnections)
    println("Number of paths if allowing one small cave to be visited twice: $numberOfPathsWithSmallCaveRevisit")
}
