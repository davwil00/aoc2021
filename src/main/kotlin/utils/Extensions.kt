package utils

inline fun <T> Iterable<T>.takeWhileInclusive(predicate: (T) -> Boolean): List<T> {
    var shouldContinue = true
    return takeWhile {
        val result = shouldContinue
        shouldContinue = shouldContinue && predicate(it)
        result
    }
}

fun String.containsAll(other: String) = this.split("").containsAll(other.split(""))
fun String.equalsIgnoringOrder(other: String) = this.toList().sorted() == other.toList().sorted()
fun String.splitToString() = this.chunked(1)