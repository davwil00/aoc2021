package day08

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SevenSegmentSearchTest {

    private val testInput = """be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce""".lines()

    private val sevenSegmentSearch = SevenSegmentSearch()

	@Test
    fun `should count all instances of digits that have unique numbers of segments`() {
        val observations = sevenSegmentSearch.parseInput(testInput)
        assertThat(sevenSegmentSearch.countSegmentsWithKnownSizes(observations)).isEqualTo(26)
    }

    @Test
    fun `should decode output digits for input`() {
        val testObservation = SevenSegmentSearch.Observation(listOf("acedgfb", "cdfbe", "gcdfa", "fbcad", "dab", "cefabd", "cdfgeb", "eafb", "cagedb", "ab"), listOf("cdfeb", "fcadb", "cdfeb", "cdbaf"))
        assertThat(sevenSegmentSearch.findOutputValue(testObservation)).isEqualTo(5353)
    }

    @Test
    fun `should sum output digits`() {
        val observations = sevenSegmentSearch.parseInput(testInput)
        assertThat(sevenSegmentSearch.deriveMappings(observations)).isEqualTo(61229)
    }
}
