package day10

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class SyntaxScoringTest {

    private val syntaxScoring = SyntaxScoring()
    private val testInput = """[({(<(())[]>[[{[]{<()<>>
[(()[<>])]({[<{<<[]>>(
{([(<{}[<>[]}>{[]{[(<()>
(((({<>}<{<{<>}{[]{[]{}
[[<[([]))<([[{}[[()]]]
[{[{({}]{}}([{[{{{}}([]
{<[[]]>}<{[{[{[]{()[[[]
[<(<(<(<{}))><([]([]()
<{([([[(<>()){}]>(<<{{
<{([{{}}[<[[[<>{}]]]>[]]""".lines()

	@ParameterizedTest
    @CsvSource("([])", "{()()()}", "<([{}])>", "[<>({}){}[([])<>]]", "(((((((((())))))))))")
    fun `should find valid chunks`(chunk: String) {
        assertThat(syntaxScoring.validateChunk(chunk).valid).isTrue
    }

	@ParameterizedTest
    @CsvSource("(]", "{()()()>", "(((()))}", "<([]){()}[{}])")
    fun `should find invalid chunks`(chunk: String) {
        assertThat(syntaxScoring.validateChunk(chunk).valid).isFalse
    }

    @ParameterizedTest
    @CsvSource(
        "{([(<{}[<>[]}>{[]{[(<()>,}",
        "[[<[([]))<([[{}[[()]]],)",
        "[{[{({}]{}}([{[{{{}}([],]",
        "[<(<(<(<{}))><([]([](),)",
        "<{([([[(<>()){}]>(<<{{,>"
    )
    fun `should find invalid chunk`(chunk: String, illegalChar: Char) {
        val validationResult = syntaxScoring.validateChunk(chunk)
        assertThat(validationResult.valid).isFalse
        assertThat(validationResult.charInError?.closing).isEqualTo(illegalChar)
    }

    @Test
    fun `should calculate syntax score`() {
        assertThat(syntaxScoring.calculateSyntaxErrorScore(testInput)).isEqualTo(26397)
    }

    @Test
    fun `should calculate autocomplete score`() {
        assertThat(syntaxScoring.calculateAutocompleteScore(testInput)).isEqualTo(288957)
    }
}
