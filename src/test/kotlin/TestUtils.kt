import java.io.File

fun readTestInput(day: Int, fileName: String = "input.txt") : String {
    val paddedDay = day.toString().padStart(2, '0')
    return File("src/test/resources/day$paddedDay/$fileName").readText().trim()
}

fun readTestInputLines(day: Int, fileName: String = "input.txt") : List<String> {
    return readTestInput(day, fileName).lines()
}