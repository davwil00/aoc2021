package day18

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import day18.Snailfish.SnailfishNumber
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import readTestInputLines
import utils.readInputLines

class SnailfishTest {

    private val snailfish = Snailfish()

	@Test
    fun `add snailfish numbers`() {
        //[1,2] + [[3,4],5] becomes [[1,2],[[3,4],5]].
        val sn1 = SnailfishNumber.fromString("[1,2]")
        val sn2 = SnailfishNumber.fromString("[[3,4],5]")
        val expected = SnailfishNumber.fromString("[[1,2],[[3,4],5]]")
        assertThat(sn1 + sn2).isEqualTo(expected)
    }

    @ParameterizedTest(name = "explodes {0}")
    @CsvSource(value = [
        "[[[[[9,8],1],2],3],4]|[[[[0,9],2],3],4]",
        "[7,[6,[5,[4,[3,2]]]]]|[7,[6,[5,[7,0]]]]",
        "[[[[[9,8],1],2],3],4]|[[[[0,9],2],3],4]",
        "[7,[6,[5,[4,[3,2]]]]]|[7,[6,[5,[7,0]]]]",
        "[[6,[5,[4,[3,2]]]],1]|[[6,[5,[7,0]]],3]",
        "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]|[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]",
        "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]|[[3,[2,[8,0]]],[9,[5,[7,0]]]]",
        "[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]|[[[[0,7],4],[[7,8],[6,0]]],[8,1]]",
        "[[[[6,7],[0,7]],[[7,[6,7]],[0,21]]],[[2,[11,10]],[[0,8],[8,0]]]]|[[[[6,7],[0,7]],[[13,0],[7,21]]],[[2,[11,10]],[[0,8],[8,0]]]]"
        ], delimiter = '|'
    )
    fun `explode snailfish numbers`(snailfishNumber: SnailfishNumber, expected: SnailfishNumber) {
        val exploded = snailfishNumber.explode()
        assertThat(exploded).isTrue
        assertThat(snailfishNumber).isEqualTo(expected)
    }

    @ParameterizedTest(name = "can split {0}")
    @CsvSource(
        "10,5,5",
        "11,5,6",
        "12,6,6"
    )
    fun `can split`(digit: Int, expectedLeft: Int, expectedRight: Int) {
        val splitNumber = SnailfishNumber.split(digit, SnailfishNumber(null))
        assertThat(splitNumber.leftValue).isEqualTo(expectedLeft)
        assertThat(splitNumber.rightValue).isEqualTo(expectedRight)
    }

    @Test
    fun split() {
        val snailfishNumber = SnailfishNumber.fromString("[[[[7,7],[7,8]],[[9,5],[8,0]]],[[[9,10],20],[8,[9,0]]]]")
        snailfishNumber.split()
        assertThat(snailfishNumber).isEqualTo(SnailfishNumber.fromString("[[[[7,7],[7,8]],[[9,5],[8,0]]],[[[9,[5,5]],20],[8,[9,0]]]]"))
    }

    @ParameterizedTest
    @CsvSource(
        "[[[[4,3],4],4],[7,[[8,4],9]]]|[1,1]|[[[[0,7],4],[[7,8],[6,0]]],[8,1]]",
        "[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]|[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]|[[[[6,7],[6,7]],[[7,7],[0,7]]],[[[8,7],[7,7]],[[8,8],[8,0]]]]",
        "[[[[6,7],[6,7]],[[7,7],[0,7]]],[[[8,7],[7,7]],[[8,8],[8,0]]]]|[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]|[[[[7,0],[7,7]],[[7,7],[7,8]]],[[[7,7],[8,8]],[[7,7],[8,7]]]]",
        "[[[[7,0],[7,7]],[[7,7],[7,8]]],[[[7,7],[8,8]],[[7,7],[8,7]]]]|[7,[5,[[3,8],[1,4]]]]|[[[[7,7],[7,8]],[[9,5],[8,7]]],[[[6,8],[0,8]],[[9,9],[9,0]]]]"
    , delimiter = '|')
    fun `add and reduce`(first: SnailfishNumber, second: SnailfishNumber, expected: SnailfishNumber) {
        val added = first + second
        snailfish.reduce(added)
        assertThat(added).isEqualTo(expected)
    }

    @ParameterizedTest
    @CsvSource(
        "testInput1.txt|[[[[3,0],[5,3]],[4,4]],[5,5]]",
        "testInput2.txt|[[[[5,0],[7,4]],[5,5]],[6,6]]",
        "testInput.txt|[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]"
    , delimiter = '|')
    fun `add and reduce list`(source: String, expected: SnailfishNumber) {
        val result = snailfish.addAndReduceList(readTestInputLines(18, source))
        assertThat(result).isEqualTo(expected)
    }

    @ParameterizedTest
    @CsvSource(
        "[[1,2],[[3,4],5]]|143",
        "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]|3488"
    , delimiter = '|')
    fun getMagnitude(input: SnailfishNumber, expected: Long) {
        assertThat(input.getMagnitude()).isEqualTo(expected)
    }

    @Test
    fun `get largest magnitude`() {
        assertThat(snailfish.findLargestMagnitude(readTestInputLines(18, "testInput3.txt")))
            .isEqualTo(3993)
    }

    @Test
    fun `get largest magnitude for full input`() {
        assertThat(snailfish.findLargestMagnitude(readInputLines(18)))
            .isEqualTo(4685)
    }
}