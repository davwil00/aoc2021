package utils

// version 1.1.51
// Adapted from https://rosettacode.org/wiki/Dijkstra%27s_algorithm#Kotlin
import java.util.TreeSet

class Edge<T: Comparable<T>>(val v1: T, val v2: T, val dist: Int) {
    override fun toString(): String {
        return "$v1 -> $v2 = $dist"
    }
}

/** One vertex of the graph, complete with mappings to neighbouring vertices */
class Vertex<T: Comparable<T>>(private val name: T) : Comparable<Vertex<T>> {

    var dist = Int.MAX_VALUE  // MAX_VALUE assumed to be infinity
    var previous: Vertex<T>? = null
    val neighbours = HashMap<Vertex<T>, Int>()

    fun printPath() {
        when (previous) {
            this -> println(name)
            null -> print("$name(unreached)")
            else -> {
                previous!!.printPath()
                print(" -> \n$name\n($dist)")
            }
        }
    }

    fun getWeight(): Int {
        return dist
    }

    override fun compareTo(other: Vertex<T>): Int {
        if (dist == other.dist) {
            return name.compareTo(other.name)
        }
        return dist.compareTo(other.dist)
    }

    override fun toString() = "($name, $dist)"
}

class Graph<T: Comparable<T>> (
    edges: List<Edge<T>>,
    private val directed: Boolean,
    private val showAllPaths: Boolean = false
) {
    // mapping of vertex names to Vertex objects, built from a set of Edges
    private val graph = HashMap<T, Vertex<T>>(edges.size)

    init {
        // one pass to find all vertices
        for (e in edges) {
            graph.putIfAbsent(e.v1, Vertex(e.v1))
            graph.putIfAbsent(e.v2, Vertex(e.v2))
        }

        // another pass to set neighbouring vertices
        for (e in edges) {
            graph.getValue(e.v1).neighbours[graph.getValue(e.v2)] = e.dist
            // also do this for an undirected graph if applicable
            if (!directed) {
                graph.getValue(e.v2).neighbours[graph.getValue(e.v1)] = e.dist
            }
        }
    }

    /** Runs dijkstra using a specified source vertex */
    fun dijkstra(startName: T) {
        if (startName !in graph) {
            throw IllegalStateException("Graph doesn't contain start vertex '$startName'")
        }
        val source = graph[startName]
        val q = TreeSet<Vertex<T>>()

        // set-up vertices
        for (v in graph.values) {
            v.previous = if (v == source) source else null
            v.dist = if (v == source)  0 else Int.MAX_VALUE
            q.add(v)
        }

        dijkstra(q)
    }

    /** Implementation of dijkstra's algorithm using a binary heap */
    private fun dijkstra(q: TreeSet<Vertex<T>>) {
        while (!q.isEmpty()) {
            // vertex with the shortest distance (first iteration will return source)
            val u = q.pollFirst()!!
            // if distance is infinite we can ignore 'u' (and any other remaining vertices)
            // since they are unreachable
            if (u.dist == Int.MAX_VALUE) break

            //look at distances to each neighbour
            for (a in u.neighbours) {
                val v = a.key // the neighbour in this iteration

                val alternateDist = u.dist + a.value
                if (alternateDist < v.dist) { // shorter path to neighbour found
                    q.remove(v)
                    v.dist = alternateDist
                    v.previous = u
                    q.add(v)
                }
            }
        }
    }

    /** Prints a path from the source to the specified vertex */
    fun printPath(endName: T) {
        if (endName !in graph) {
            throw IllegalStateException("Graph doesn't contain end vertex '$endName'")
        }
        println(if (directed) "Directed   : " else "Undirected : ")
        graph.getValue(endName).printPath()
        println()
        if (showAllPaths) {
            printAllPaths()
        } else {
            println()
        }
    }

    fun getWeightToPath(endName: T): Int {
        return graph.getValue(endName).getWeight()
    }

    /** Prints the path from the source to every vertex (output order is not guaranteed) */
    private fun printAllPaths() {
        for (v in graph.values) {
            v.printPath()
            println()
        }
        println()
    }
}