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
fun String.unique() = this.splitToString().toSet().joinToString("")
fun String.isLowercase() = this == this.lowercase()

fun List<Long>.product() = this.reduce(Long::times)

fun <T, U> Grouping<T, U>.eachCountToLong() = this.fold(0L) { acc, _ -> acc + 1 }