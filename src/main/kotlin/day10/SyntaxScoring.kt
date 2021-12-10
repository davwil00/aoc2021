package day10

import day10.SyntaxScoring.ChunkChars.Companion.isOpening
import utils.readInputLines

class SyntaxScoring {

    fun calculateSyntaxErrorScore(chunks: List<String>): Long {
        return chunks.map(::validateChunk)
            .filterNot { it.valid }
            .sumOf { illegalChar -> illegalChar.charInError!!.syntaxPoints }
    }

    fun calculateAutocompleteScore(chunks: List<String>): Long {
        val corruptedChunks = chunks.map(::validateChunk)
            .filter { it.valid }

        val totals = corruptedChunks.map { chunk ->
            chunk.remainingChars.reversed().fold(0L) {
                total, remainingChar -> total * 5 + remainingChar.autocompletePoints
            }
        }

        return totals.sorted()[totals.size / 2]
    }

    fun validateChunk(chunk: String): ValidationResult {
        val openedChunks = mutableListOf<ChunkChars>()
        for (char in chunk) {
            if (isOpening(char)) {
                openedChunks.add(ChunkChars.fromOpeningChar(char))
            } else if (char == openedChunks.last().closing) {
                openedChunks.removeLast()
            } else {
                return ValidationResult(false, ChunkChars.fromClosingChar(char))
            }
        }

        return ValidationResult(true, openedChunks)
    }

    class ValidationResult(val valid: Boolean, val charInError: ChunkChars?, val remainingChars: List<ChunkChars> = listOf()) {
        constructor(valid: Boolean, remainingChars: List<ChunkChars>): this(valid, null, remainingChars)
    }

    enum class ChunkChars(val opening: Char, val closing: Char, val syntaxPoints: Long, val autocompletePoints: Long) {
        BRACKET('(', ')', 3, 1),
        SQUARE_BRACKET('[', ']', 57, 2),
        BRACE('{', '}', 1197, 3),
        TRIANGLE_BRACKET('<', '>', 25137, 4);

        companion object {
            fun fromOpeningChar(openingChar: Char) = values().first { it.opening == openingChar }
            fun fromClosingChar(closingChar: Char) = values().first { it.closing == closingChar }
            fun isOpening(char: Char) = values().any { it.opening == char }
        }
    }
}

fun main() {
    val input = readInputLines(10)
    val syntaxScoring = SyntaxScoring()
    val syntaxErrorScore = syntaxScoring.calculateSyntaxErrorScore(input)
    println("Syntax error score is $syntaxErrorScore")
    val autocompleteScore = syntaxScoring.calculateAutocompleteScore(input)
    println("Autocomplete score is $autocompleteScore")
}
